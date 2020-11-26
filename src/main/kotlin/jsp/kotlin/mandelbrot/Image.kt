package jsp.kotlin.mandelbrot

import jsp.kotlin.complex.Complex
import jsp.kotlin.complex.Complex.Companion.ZERO
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.absoluteValue

/**
 * Maps from pixel coordinates to the corresponding point on the complex plane
 */
class PixelMapper(
    private val topLeft: Complex,
    botRight: Complex,
    imageWidth: Int,
    imageHeight: Int,
) {

    private val xScale = (botRight.re - topLeft.re).absoluteValue / imageWidth
    private val yScale = (botRight.im - topLeft.im).absoluteValue / imageHeight

    fun toComplex(pixel: Pixel) = Complex(topLeft.re + pixel.x * xScale, topLeft.im - pixel.y * yScale)
}

data class Pixel(val x: Int, val y: Int) {
    companion object {
        fun pixels(width: Int, height: Int): Sequence<Pixel> =
            (0 until width).asSequence().flatMap { x ->
                (0 until height).asSequence().map { y ->
                    Pixel(x, y)
                }
            }
    }
}

fun iterations(c: Complex, limit: Int): Int {
    var z = ZERO
    for (i in 0 until limit) {
        z = z * z + c
        if (z.magnitude2 > 4.0)
            return i
    }
    return limit
}

fun main(args: Array<String>) {
    val topLeft = Complex(-2.5, 1.0)
    val botRight = Complex(1.0, -1.0)

    val imageWidth: Int = (1024 * 4)
    val imageHeight: Int = (imageWidth *
            (botRight.im - topLeft.im).absoluteValue / (botRight.re - topLeft.re).absoluteValue).toInt()

    println("imageWidth: $imageWidth, imageHeight: $imageHeight")

    val limit = 250

    val pixelMapper = PixelMapper(topLeft, botRight, imageWidth, imageHeight)
    val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB)

    Pixel.pixels(imageWidth, imageHeight)
        .map { pixel -> Pair(pixel, pixelMapper.toComplex(pixel)) }
        .map { (pixel, complex) -> Pair(pixel, iterations(complex, limit)) }
        .map { (pixel, iterations) -> Pair(pixel, (if (iterations < limit) Color.WHITE else Color.BLACK).rgb) }
        .forEach { (pixel, rgb) -> image.setRGB(pixel.x, pixel.y, rgb) }
    ImageIO.write(image, "png", File("mandelbrot.png"))
}
