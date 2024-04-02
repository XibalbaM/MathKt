package fr.xibalba.math

open class Matrix<T>(val rows: List<List<T>>) {
    val columns get() = rows[0].indices.map { i -> rows.map { it[i] } }

    init {
        require(rows.isNotEmpty()) { "Matrix must have at least one row" }
        require(rows.all { it.size == rows[0].size }) { "All rows must have the same size" }
    }

    open operator fun get(i: Int, j: Int) = rows[i][j]

    override fun toString() : String {
        val stringBuilder = StringBuilder()
        for (row in rows) {
            stringBuilder.append(row.joinToString(", ", "|" , "|\n"))
        }
        stringBuilder[0] = '⌈'
        stringBuilder[rows.first().joinToString(", ", "|" , "|").length - 1] = '⌉'
        stringBuilder[stringBuilder.length - rows.last().joinToString(", ", "|" , "|").length - 1] = '⌊'
        stringBuilder[stringBuilder.length - 2] = '⌋'
        return stringBuilder.toString()
    }
    override fun equals(other: Any?) = other is Matrix<*> && rows == other.rows
    override fun hashCode() = rows.hashCode()
    open fun create(rows: List<List<T>>) = Matrix(rows)
}
open class SquareMatrix<T>(rows: List<List<T>>) : Matrix<T>(rows) {
    init {
        require(rows.size == rows[0].size) { "Matrix must be square" }
    }

    fun mainDiagonal() = rows.indices.map { i -> rows[i][i] }
    override fun create(rows: List<List<T>>) = SquareMatrix(rows)
}
open class LineMatrix<T>(private val line: List<T>) : Matrix<T>(listOf(line)), List<T> by line {
    override fun create(rows: List<List<T>>) = LineMatrix(rows.first())
}
open class ColumnMatrix<T>(private val column: List<T>) : Matrix<T>(column.map { listOf(it) }), List<T> by column {
    override fun create(rows: List<List<T>>) = ColumnMatrix(rows.map { it.first() })
}
fun nullMatrix(rows: Int, columns: Int) = Matrix(List(rows) { List(columns) { 0f } })
fun nullSquareMatrix(size: Int) = SquareMatrix(List(size) { List(size) { 0f } })
fun diagonalMatrix(vararg values : Float) : SquareMatrix<Float> {
    val rows = nullSquareMatrix(values.size).rows.map { it.toMutableList() }.toMutableList()
    for (index in values.indices) {
        rows[index][index] = values[index]
    }
    return SquareMatrix(rows)
}
fun unitMatrix(size: Int) = diagonalMatrix(*FloatArray(size) { 1f })
class Vec2<T>(val x: T, val y: T) : ColumnMatrix<T>(listOf(x, y)) {
    override fun create(rows: List<List<T>>) = Vec2(rows[0][0], rows[1][0])
}
fun <T> vec2(x: T, y: T) = Vec2(x, y)
class Vec3<T>(val x: T, val y: T, val z: T) : ColumnMatrix<T>(listOf(x, y, z)) {
    override fun create(rows: List<List<T>>) = Vec3(rows[0][0], rows[1][0], rows[2][0])
}
fun <T> vec3(x: T, y: T, z: T) = Vec3(x, y, z)
class Vec4<T>(val x: T, val y: T, val z: T, val w: T) : ColumnMatrix<T>(listOf(x, y, z, w))
fun <T> vec4(x: T, y: T, z: T, w: T) = Vec4(x, y, z, w)

typealias Vec2f = Vec2<Float>
typealias Vec3f = Vec3<Float>
typealias Vec4f = Vec4<Float>

inline operator fun <reified T : Matrix<Float>> T.plus(other: Float) = this.create(this.rows.map { row -> row.map { it + other } }) as T
inline operator fun <reified T : Matrix<Float>> T.plus(other: T): T {
    require(this.rows.size == other.rows.size && this.rows[0].size == other.rows[0].size)
    return this.create(this.rows.zip(other.rows).map { (row1, row2) -> row1.zip(row2).map { (a, b) -> a + b } }) as T
}

inline operator fun <reified T : Matrix<Float>> T.minus(other: Float) = this.create(this.rows.map { row -> row.map { it - other } }) as T
inline operator fun <reified T : Matrix<Float>> T.minus(other: T): T {
    require(this.rows.size == other.rows.size && this.rows[0].size == other.rows[0].size)
    return this.create(this.rows.zip(other.rows).map { (row1, row2) -> row1.zip(row2).map { (a, b) -> a - b } }) as T
}

inline operator fun <reified T : Matrix<Float>> T.times(other: Float) = this.create(this.rows.map { row -> row.map { it * other } }) as T
operator fun <T : LineMatrix<Float>> T.times(other: ColumnMatrix<Float>): Float {
    require(this.size == other.size)
    return (0..<this.size).sumOf { this[it].toDouble() * other[it].toDouble() }.toFloat()
}
inline operator fun <reified T : Matrix<Float>> T.times(other: Matrix<Float>): Matrix<Float> {
    require(this.rows[0].size == other.rows.size)
    val data = this.rows.map { row -> other.columns.map { column -> row.zip(column).sumOf { (a, b) -> a.toDouble() * b }.toFloat() } }
    return Matrix(data)
}

inline operator fun <reified T : Matrix<Float>> T.div(other: Float) = this.create(this.rows.map { row -> row.map { it / other } }) as T

inline operator fun <reified T : Matrix<Float>> T.rem(other: Float) = this.create(this.rows.map { row -> row.map { it % other } }) as T

operator fun <T : Matrix<Float>> T.contains(other: Float): Boolean {
    return this.rows.any { row -> row.any { it == other } }
}
operator fun <T : Matrix<Float>> T.contains(other: Matrix<Float>): Boolean {
    return other.rows.flatten().all { it in this }
}

inline fun <reified T : Matrix<Float>> T.transpose() = this.create(this.columns) as T

fun pow(matrix: Matrix<Float>, power: Int): Matrix<Float> {
    require(matrix.rows.size == matrix.columns.size)
    require(power >= 0)
    return if (power == 0) unitMatrix(matrix.rows.size) else matrix * pow(matrix, power - 1)
}
fun SquareMatrix<Float>.determinant(): Float {
    val permutations = (1..this.rows.size).map { it.toFloat() }.toSet().permutations()
    return permutations.sumOf { permutation ->
        permutation.permutationSign() * this.rows.indices.map { i -> this[permutation[i].toInt() - 1, i] }.fold(1.0) { acc, elem -> acc * elem }
    }.toFloat()
}
fun SquareMatrix<Float>.minor(i: Int, j: Int = i): Float {
    val rows = this.rows.toMutableList()
    rows.removeAt(i)
    val columns = rows[0].indices.map { k -> rows.map { it[k] } }.toMutableList()
    columns.removeAt(j)
    return SquareMatrix(columns).determinant()
}
fun SquareMatrix<Float>.cofactor(i: Int, j: Int = i): Float {
    return this.minor(i, j) * if ((i + j) % 2 == 0) 1 else -1
}
fun SquareMatrix<Float>.comatrix() : SquareMatrix<Float> {
    val rows = this.rows.indices.map { i -> this.rows.indices.map { j -> this.cofactor(i, j) } }
    return SquareMatrix(rows)
}
fun SquareMatrix<Float>.adjugate() = this.comatrix().transpose()
fun SquareMatrix<Float>.inverse(): SquareMatrix<Float> {
    val determinant = this.determinant()
    require(determinant != 0f) { "Matrix is not invertible" }
    return this.adjugate() * (1 / determinant)
}