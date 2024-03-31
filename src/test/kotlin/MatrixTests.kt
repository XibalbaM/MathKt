import fr.xibalba.math.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

class MatrixTests {

    @Test
    fun matrix() {
        val matrix = Matrix(listOf(listOf(1, 2), listOf(3, 4)))
        assertEquals(matrix.rows, listOf(listOf(1, 2), listOf(3, 4)))
        assertEquals(matrix.columns, listOf(listOf(1, 3), listOf(2, 4)))
        assertThrows<IllegalArgumentException> { Matrix(listOf(listOf(1, 2), listOf(3))) }
        assertThrows<IllegalArgumentException> { Matrix(emptyList<List<String>>()) }
        assertEquals(matrix[0, 0], 1)
        assertEquals(matrix.hashCode(), matrix.rows.hashCode())
        assertEquals("⌈1, 2⌉\n⌊3, 4⌋\n", matrix.toString())
        assertEquals(matrix, Matrix(listOf(listOf(1, 2), listOf(3, 4))))
    }

    @Test
    fun squareMatrix() {
        val matrix = SquareMatrix(listOf(listOf(1, 2), listOf(3, 4)))
        assertEquals(matrix.mainDiagonal(), listOf(1, 4))
        assertThrows<IllegalArgumentException> { SquareMatrix(listOf(listOf(1, 2, 5), listOf(3, 4, 5))) }
        assertEquals(matrix, Matrix(listOf(listOf(1, 2), listOf(3, 4))))
    }

    @Test
    fun lineMatrix() {
        val matrix = LineMatrix(listOf(1, 2, 3))
        assertEquals(matrix, Matrix(listOf(listOf(1, 2, 3))))
    }

    @Test
    fun columnMatrix() {
        val matrix = LineMatrix(listOf(1, 2, 3))
        assertEquals(matrix, Matrix(listOf(listOf(1, 2, 3))))
    }

    @Test
    fun nullMatrix() {
        val matrix = nullMatrix(2, 2)
        assertEquals(matrix, Matrix(listOf(listOf(0f, 0f), listOf(0f, 0f))))
    }

    @Test
    fun nullSquareMatrix() {
        val matrix = nullSquareMatrix(2)
        assertEquals(matrix, Matrix(listOf(listOf(0f, 0f), listOf(0f, 0f))))
    }

    @Test
    fun diagonalMatrix() {
        val matrix = diagonalMatrix(1f, 2f)
        assertEquals(matrix, Matrix(listOf(listOf(1f, 0f), listOf(0f, 2f))))
    }

    @Test
    fun unitMatrix() {
        val matrix = unitMatrix(2)
        assertEquals(matrix, Matrix(listOf(listOf(1f, 0f), listOf(0f, 1f))))
    }

    @Test
    fun vec2() {
        val matrix = vec2(1, 2)
        assertEquals(matrix, Matrix(listOf(listOf(1), listOf(2))))
    }

    @Test
    fun vec3() {
        val matrix = vec3(1, 2, 3)
        assertEquals(matrix, Matrix(listOf(listOf(1), listOf(2), listOf(3))))
    }

    @Test
    fun plus() {
        val matrix = Matrix(listOf(listOf(1f, 2f), listOf(3f, 4f)))
        assertEquals(matrix + 1f, Matrix(listOf(listOf(2f, 3f), listOf(4f, 5f))))
        assertEquals(matrix + matrix, Matrix(listOf(listOf(2f, 4f), listOf(6f, 8f))))
        assertThrows<IllegalArgumentException> { matrix + Matrix(listOf(listOf(1f, 2f))) }
    }

    @Test
    fun minus() {
        val matrix = Matrix(listOf(listOf(1f, 2f), listOf(3f, 4f)))
        assertEquals(matrix - 1f, Matrix(listOf(listOf(0f, 1f), listOf(2f, 3f))))
        assertEquals(matrix - matrix, Matrix(listOf(listOf(0f, 0f), listOf(0f, 0f))))
        assertThrows<IllegalArgumentException> { matrix - Matrix(listOf(listOf(1f, 2f))) }
    }

    @Test
    fun times() {
        val matrix = Matrix(listOf(listOf(1f, 2f), listOf(3f, 4f)))
        assertEquals(matrix * 2f, Matrix(listOf(listOf(2f, 4f), listOf(6f, 8f))))
        assertEquals(matrix * matrix, Matrix(listOf(listOf(7f, 10f), listOf(15f, 22f))))
        assertThrows<IllegalArgumentException> { matrix * Matrix(listOf(listOf(1f, 2f))) }
    }

    @Test
    fun div() {
        val matrix = Matrix(listOf(listOf(1f, 2f), listOf(3f, 4f)))
        assertEquals(matrix / 2f, Matrix(listOf(listOf(0.5f, 1f), listOf(1.5f, 2f))))
    }

    @Test
    fun rem() {
        val matrix = Matrix(listOf(listOf(1f, 2f), listOf(3f, 4f)))
        assertEquals(matrix % 2f, Matrix(listOf(listOf(1f, 0f), listOf(1f, 0f))))
    }

    @Test
    fun contains() {
        val matrix = Matrix(listOf(listOf(1f, 2f), listOf(3f, 4f)))
        assertTrue(1f in matrix)
        assertTrue(matrix in matrix)
        assertTrue(Matrix(listOf(listOf(1f, 3f))) in matrix)
        assertFalse(5f in matrix)
        assertFalse(Matrix(listOf(listOf(1f, 5f))) in matrix)
    }

    @Test
    fun transpose() {
        val matrix = Matrix(listOf(listOf(1f, 2f), listOf(3f, 4f)))
        assertEquals(matrix.transpose(), Matrix(listOf(listOf(1f, 3f), listOf(2f, 4f))))
    }

    @Test
    fun pow() {
        val matrix = SquareMatrix(listOf(listOf(1f, 2f), listOf(3f, 4f)))
        assertEquals(pow(matrix, 0), unitMatrix(2))
        assertEquals(pow(matrix, 1), matrix)
        assertEquals(pow(matrix, 2), Matrix(listOf(listOf(7f, 10f), listOf(15f, 22f))))
    }

    @Test
    fun determinant() {
        val matrix = SquareMatrix(listOf(listOf(1f, 2f), listOf(3f, 4f)))
        assertEquals(matrix.determinant(), -2f)
    }

    @Test
    fun minor() {
        val matrix = SquareMatrix(listOf(listOf(1f, 2f, 3f), listOf(4f, 5f, 6f), listOf(7f, 8f, 9f)))
        assertEquals(matrix.minor(0, 0), SquareMatrix(listOf(listOf(5f, 6f), listOf(8f, 9f))).determinant())
    }

    @Test
    fun cofactor() {
        val matrix = SquareMatrix(listOf(listOf(1f, 2f, 3f), listOf(4f, 5f, 6f), listOf(7f, 8f, 9f)))
        assertEquals(matrix.cofactor(0, 0), SquareMatrix(listOf(listOf(5f, 6f), listOf(8f, 9f))).determinant())
        assertEquals(matrix.cofactor(0, 1), -SquareMatrix(listOf(listOf(4f, 6f), listOf(7f, 9f))).determinant())
    }

    @Test
    fun comatrix() {
        val matrix = SquareMatrix(listOf(listOf(1f, 2f, 3f), listOf(4f, 5f, 6f), listOf(7f, 8f, 9f)))
        assertEquals(matrix.comatrix(), SquareMatrix(listOf(listOf(-3f, 6f, -3f), listOf(6f, -12f, 6f), listOf(-3f, 6f, -3f))))
    }

    @Test
    fun adjugate() {
        val matrix = SquareMatrix(listOf(listOf(1f, 2f, 3f), listOf(4f, 5f, 6f), listOf(7f, 8f, 9f)))
        assertEquals(matrix.adjugate(), SquareMatrix(listOf(listOf(-3f, 6f, -3f), listOf(6f, -12f, 6f), listOf(-3f, 6f, -3f))).transpose())
    }

    @Test
    fun inverse() {
        val matrix = SquareMatrix(listOf(listOf(1f, 2f, 3f), listOf(0f, 1f, 4f), listOf(5f, 6f, 0f)))
        assertEquals(matrix.inverse(), SquareMatrix(listOf(listOf(-24f, 18f, 5f), listOf(20f, -15f, -4f), listOf(-5f, 4f, 1f))))
    }
}