package jsp.kotlin.complex

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.*

class ComplexTest : StringSpec({

    "Canary test should pass" {
        true shouldBe true
    }

    "Complex conjugate of (0 + 0i) is (0 + 0i)" {
        val conjugate = Complex(0, 0).conjugate
        conjugate.re shouldBe 0
        conjugate.im shouldBe 0
    }

    "Complex conjugate of (10 + 10i) is (10 - 10i)" {
        val conjugate = Complex(10, 10).conjugate
        conjugate.re shouldBe 10
        conjugate.im shouldBe -10
    }

    "Addition: (1 + 1i) + (1 + 1i) is (2 + 2i)" {
        val result = Complex(1, 1) + Complex(1, 1)
        result.re shouldBe 2
        result.im shouldBe 2
    }

    "Subtraction: (1 + 1i) - (1 + 1i) is (0 + 0i)" {
        val result = Complex(1, 1) - Complex(1, 1)
        result.re shouldBe 0
        result.im shouldBe 0
    }

    "Multiplication:(2 + 3i) * (4 + 5i) is (-7 + 22i)" {
        val result = Complex(2, 3) * Complex(4, 5)
        result.re shouldBe -7
        result.im shouldBe 22
    }

    "Division: (23 + 2i) / (4 - 5i) is (2 + 3i)" {
        val result = Complex(23, 2) / Complex(4, -5)
        result.re shouldBe 2
        result.im shouldBe 3
    }

    "Magnitude of (3 + 4i) is 5" {
        Complex(3, 4).magnitude shouldBe 5
    }

    "Scalar multiplication: (2 + 3i) * 2 is (4 + 6i)" {
        val result = Complex(2, 3) * 2
        result.re shouldBe 4
        result.im shouldBe 6
    }

    "Scalar multiplication: 2 * (2 + 3i) is (4 + 6i)" {
        val result = 2 * Complex(2, 3)
        result.re shouldBe 4
        result.im shouldBe 6
    }

    "Squared value is correct" {
        val c = Complex(2, 3)
        c.squared.re shouldBe -5.0
        c.squared.im shouldBe 12.0
    }
})
