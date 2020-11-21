package jsp.kotlin.complex

import kotlin.math.sqrt

operator fun Complex.times(d: Double): Complex = Complex(re * d, im * d)
operator fun Complex.times(i: Int): Complex =  this * i.toDouble()
operator fun Complex.times(l: Long): Complex =  this * l.toDouble()

operator fun Double.times(c: Complex): Complex = Complex(c.re * this, c.im * this)
operator fun Int.times(c: Complex): Complex =  this.toDouble() * c
operator fun Long.times(c: Complex): Complex =  this.toDouble() * c

class Complex(val re: Double, val im: Double) {

    constructor(r: Int, i: Int): this(r.toDouble(), i.toDouble())
    constructor(r: Long, i: Long): this(r.toDouble(), i.toDouble())

    val conjugate: Complex
        get() = Complex(re, -im)

    val magnitude2: Double
        get() = re * re + im * im

    val magnitude: Double
        get() = sqrt(magnitude2)

    operator fun plus(other: Complex) = Complex(re + other.re, im + other.im)
    operator fun minus(other: Complex) = Complex(re - other.re, im - other.im)
    operator fun times(other: Complex) = Complex((re * other.re) - (im * other.im), (re * other.im + im * other.re))
    operator fun div(other: Complex): Complex {
        val denom = other.re * other.re + other.im * other.im
        val r = (re * other.re + im * other.im) / denom
        val i = (im * other.re - re * other.im) / denom
        return Complex(r, i)
    }

    companion object {
        val ZERO = Complex(0.0, 0.0)
    }
}
