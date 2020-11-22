package jsp.kotlin.mandelbrot

import jsp.kotlin.complex.Complex
import jsp.kotlin.complex.Complex.Companion.ZERO
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.absoluteValue

class MandelbrotSet(
    topLeft: Complex,
    botRight: Complex,
    imageWidth: Int,
    imageHeight: Int,
    private val limit: Int
) {
    private val x0 = topLeft.re
    private val y0 = topLeft.im

    private val width = (botRight.re - topLeft.re).absoluteValue
    private val height = (botRight.im - topLeft.im).absoluteValue

    private val xScale = width / imageWidth
    private val yScale = height / imageHeight

    fun iterationsFor(x: Int, y: Int): Int  = calculate(Complex(x0 + x * xScale, y0 - y * yScale))

    private fun calculate(c: Complex): Int {
        var z = ZERO
        for (i in 0 until limit) {
            z = z * z + c
            if (z.magnitude2 > 4.0)
                return i
        }
        return limit
    }
}

fun main(args: Array<String>) {
    val topLeft = Complex(-2.5, 1.0)
    val botRight = Complex(1.0, -1.0)

    val base = 2048
    val width: Int = base * 7 / 2
    val height = base * 2
    val limit = 250

    val m = MandelbrotSet(topLeft, botRight, width, height, limit)
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    for (y in 0 until height) {
        for (x in 0 until width) {
            val v = m.iterationsFor(x, y)
            val rgb = (if (v < limit) Color.WHITE else Color.BLACK).rgb
            image.setRGB(x, y, rgb)
        }
    }
    ImageIO.write(image, "png", File("mandelbrot.png"))
}
