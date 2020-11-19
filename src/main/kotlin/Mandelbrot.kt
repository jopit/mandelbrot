package jsp.kotlin.mandelbrot

class Complex(val re: Double, val im: Double) {
    fun conjugate() = Complex(re, -im)

    operator fun plus(other: Complex) = Complex(re + other.re, im + other.im)
    operator fun minus(other: Complex) = Complex(re - other.re, im - other.im)
    operator fun times(other: Complex) = Complex((re * other.re) - (im * other.im), (re * other.im + im * other.re))
}

fun generate(width: Int, height: Int): Array<IntArray> {
    val data = Array(height) {
        IntArray(width)
    }


    return data
}