package jsp.kotlin.mandelbrot

import jsp.kotlin.complex.Complex
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.time.Duration
import javax.imageio.ImageIO
import kotlin.math.absoluteValue
import kotlin.math.log2
import kotlin.math.pow
import kotlin.system.measureTimeMillis

data class Pixel(val x: Int, val y: Int) {
    companion object {
        fun region(width: Int, height: Int): Sequence<Pixel> =
            (0 until width).asSequence().flatMap { x ->
                (0 until height).asSequence().map { y ->
                    Pixel(x, y)
                }
            }
    }
}

/** Returns a function to map from pixel coordinates to the corresponding point on the complex plane */
fun createPixelMap(topLeft: Complex, botRight: Complex, imageWidth: Int, imageHeight: Int): (pixel: Pixel) -> Complex {
    val xScale = (botRight.re - topLeft.re).absoluteValue / imageWidth
    val yScale = (botRight.im - topLeft.im).absoluteValue / imageHeight
    return { pixel: Pixel -> Complex(topLeft.re + pixel.x * xScale, topLeft.im - pixel.y * yScale) }
}

/** Returns a function to map a complex number to a color */
fun createColorMap(limit: Int): (c: Complex) -> Color {

    fun iterate(c: Complex, optimize: Boolean = true): Pair<Complex, Int> {
        if (optimize) {
            var x = c.re
            var y = c.im
            var x2 = x * x
            var y2 = y * y
            var iterations = 1
            while (x2 + y2 <= 4.0 && iterations < limit) {
                y = 2.0 * x * y + c.im
                x = x2 - y2 + c.re
                x2 = x * x
                y2 = y * y
                iterations += 1
            }
            return Pair(Complex(x, y), iterations)
        } else {
            var z = c
            for (iterations in 1 until limit) {
                if (z.magnitude2 > 4.0)
                    return Pair(z, iterations)
                z = z.squared + c
            }
            return Pair(z, limit)
        }
    }

    // Produces a smooth color
    // see https://stackoverflow.com/questions/369438/smooth-spectrum-for-mandelbrot-set-rendering
    fun color(c: Complex): Color {
        val (zn, iterations) = iterate(c, optimize = true)
        if (iterations >= limit)
            return Color.BLACK
        val nsmooth = ((iterations + 1 - log2(log2(zn.magnitude))) / limit).toFloat()
        val brightness = nsmooth.pow(1.0f / 4.0f)
        val saturation = 1.0f - brightness
        val rgb = Color.HSBtoRGB(0.60f, saturation, brightness)
        return Color(rgb)
    }

    return { color(it) }
}

fun main(args: Array<String>) {
    val topLeft = Complex(-2.0, 1.25)
    val botRight = Complex(1.0, -1.25)

    val imageWidth: Int = if (args.size > 0)
        args[0].toInt()
    else
        1024 * 8
    val imageHeight: Int = (imageWidth *
            (botRight.im - topLeft.im).absoluteValue / (botRight.re - topLeft.re).absoluteValue).toInt()
    val limit = 1000

    println("Image width: $imageWidth, Image height: $imageHeight, Limit: $limit")

    val pixelToComplex = createPixelMap(topLeft, botRight, imageWidth, imageHeight)
    val complexToColor = createColorMap(limit)
    val color = { pixel: Pixel -> complexToColor(pixelToComplex(pixel)) }

    val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB)
    val timeInMillis = measureTimeMillis {
        for (pixel in Pixel.region(imageWidth, imageHeight)) {
            image.setRGB(pixel.x, pixel.y, color(pixel).rgb)
        }
    }
    println("Generation took ${Duration.ofMillis(timeInMillis)}")
    ImageIO.write(image, "png", File("mandelbrot.png"))
}
