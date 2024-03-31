import fr.xibalba.math.permutationSign
import fr.xibalba.math.permutations
import fr.xibalba.math.subsets
import kotlin.test.Test

class SetsTests {

    @Test
    fun subsets() {
        val set = setOf(1f, 2f, 3f)
        val subsets = set.subsets()
        assert(subsets.size == 8)
        assert(subsets.contains(emptySet()))
        assert(subsets.contains(setOf(1f)))
        assert(subsets.contains(setOf(2f)))
        assert(subsets.contains(setOf(3f)))
        assert(subsets.contains(setOf(1f, 2f)))
        assert(subsets.contains(setOf(1f, 3f)))
        assert(subsets.contains(setOf(2f, 3f)))
        assert(subsets.contains(setOf(1f, 2f, 3f)))
    }

    @Test
    fun permutations() {
        val set = setOf(1f, 2f, 3f)
        val permutations = set.permutations()
        assert(permutations.size == 6)
        assert(permutations.contains(listOf(1f, 2f, 3f)))
        assert(permutations.contains(listOf(1f, 3f, 2f)))
        assert(permutations.contains(listOf(2f, 1f, 3f)))
        assert(permutations.contains(listOf(2f, 3f, 1f)))
        assert(permutations.contains(listOf(3f, 1f, 2f)))
        assert(permutations.contains(listOf(3f, 2f, 1f)))
    }

    @Test
    fun permutationSign() {
        val permutation = listOf(1f, 3f, 2f)
        assert(permutation.permutationSign() == -1)
        val permutation2 = listOf(3f, 2f, 1f)
        assert(permutation2.permutationSign() == 1)
        val permutations = setOf(1f, 2f, 3f).permutations()
        assert(permutations.sumOf { it.permutationSign() } == 0)
    }
}