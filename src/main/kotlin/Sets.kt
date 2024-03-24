package fr.xibalba.math

fun Set<Float>.subsets(): Set<Set<Float>> {
    if (isEmpty()) return setOf(emptySet())
    val first = first()
    val rest = this - first
    return rest.subsets().map { it + first }.toMutableSet() + rest.subsets()
}

fun Set<Float>.permutations(): List<List<Float>> {
    if (size == 1) return listOf(toList())
    val result = mutableListOf<List<Float>>()
    for (element in this) {
        val rest = this - element
        result += rest.permutations().map { it + element }
    }
    return result
}

fun List<Float>.permutationSign(): Int {
    var sign = 1
    for (i in indices) {
        for (j in i + 1 until size) {
            if (this[i] > this[j]) sign *= -1
        }
    }
    return sign
}