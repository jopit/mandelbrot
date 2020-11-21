package jsp.kotlin.mandelbrot

import jsp.kotlin.complex.Complex
import jsp.kotlin.complex.Complex.Companion.ZERO
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun generate(width: Int, height: Int, limit: Int = 1000): Array<IntArray> {
    val data = Array(height) {
        IntArray(width)
    }

    val x0 = -2.5
    val y0 = 1.0

    val xscale = 3.5 / width
    val yscale = 2.0 / height

    for (h in 0 until height) {
        for (w in 0 until width) {
            data[h][w] = calculate(Complex(x0 + w * xscale, y0 - h * yscale), limit)
        }
    }

    return data
}

fun calculate(c: Complex, limit: Int): Int {
    var z = ZERO
    for (i in 0 until limit) {
        z = z * z + c
        if (z.magnitude2 > 4.0)
            return i
    }
    return limit
}

fun main(args: Array<String>) {
    val base = 2048
    val width: Int = base * 7 / 2
    val height = base * 2
    val limit = 1000

    val data = generate(width, height, limit)
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    for (y in 0 until height) {
        for (x in 0 until width) {
            val v = data[y][x]
            val rgb = (if (v < limit) Color.WHITE else Color.BLACK).rgb
            image.setRGB(x, y, rgb)
        }
    }
    ImageIO.write(image, "png", File("mandelbrot.png"))
}
