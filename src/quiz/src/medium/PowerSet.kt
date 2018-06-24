package medium

import org.junit.Assert

import java.util.ArrayList

/**
 * Given a set of distinct integers, nums, return all possible subsets.
 * <br></br>
 * Note: The solution set must not contain duplicate subsets.
 *
 *
 * For example,
 * <pre>
 * If nums = [1,2,3], a solution is:
 * [
 * [3],
 * [1],
 * [2],
 * [1,2,3],
 * [1,3],
 * [2,3],
 * [1,2],
 * []
 * ]
</pre> *
 * Reference:
 *
 *  * https://leetcode.com/problems/subsets/description/
 *
 */
class PowerSet {

    // Solution #1 ////////////////////////////////////////////////////////////

    private fun powerSets(nums: IntArray): List<List<Int>> {
        return powerSetsInternal(nums, nums.size)
    }

    private fun powerSetsInternal(nums: IntArray, size: Int): List<List<Int>> {
        // Stop when the input is empty.
        if (size <= 0) {
            val empty = ArrayList<List<Int>>()

            empty.add(ArrayList())

            return empty
        }

        val subSize = size - 1
        val trimmed = nums[subSize]

        // Recursively calculate the sub-sets.
        val subSets = powerSetsInternal(nums, subSize)

        // Add the sub-sets to the sets.
        val sets = ArrayList(subSets)

        // Calculate the rest permutation by combining the trimmed element with
        // the sub-sets.
        for (subSet in subSets) {
            val subSetCopy = ArrayList(subSet)

            subSetCopy.add(trimmed)

            sets.add(subSetCopy)
        }

        return sets
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val solver = PowerSet()

            Assert.assertEquals(2, solver.powerSets(intArrayOf(1)).size)
        }
    }
}
