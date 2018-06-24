package hard

import org.junit.Assert

/**
 * Given an unsorted integer array, find the smallest missing positive integer.
 *
 * Example 1:
 *
 * ```
 * Input: [1,2,0]
 * Output: 3
 * ```
 * Example 2:
 *
 * ```
 * Input: [3,4,-1,1]
 * Output: 2
 * ```
 *
 * Example 3:
 *
 * ```
 * Input: [7,8,9,11,12]
 * Output: 1
 * ```
 *
 * Note: Your algorithm should run in O(n) time and uses constant extra space.
 *
 * Reference:
 *
 *  * https://leetcode.com/problems/first-missing-positive/description/
 *
 */
class FirstMissingPositive {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val solver = FirstMissingPositive()

            Assert.assertEquals(3, solver.firstMissingPositive(
                intArrayOf(1, 2, 0)))

            Assert.assertEquals(3, solver.firstMissingPositive(
                intArrayOf(0, 1, 2)))

            Assert.assertEquals(2, solver.firstMissingPositive(
                intArrayOf(3, 4, -1, 1)))

            Assert.assertEquals(1, solver.firstMissingPositive(
                intArrayOf(7, 8, 9, 11, 12)))
        }
    }

    // Solution #1 ////////////////////////////////////////////////////////////

    fun firstMissingPositive(nums: IntArray): Int {
        // 1. One way is to use quick-sort and the time complexity is O(nlog(n)),
        // which is relatively close to O(n).
        //
        // 2. Another way is to benefit from the nature of consecutively climbing
        // integer sequence, which is the element value should be equal to index+1
        // for the best case.
        // We scan the elements, for each element visiting, we check if the value
        // matches the expected position. e.g., Given [1,2], the element values
        // matches positions.
        // If the value doesn't match, we swap the element at i, with value v, with
        // another element at v until nothing could be swapped.
        // We have to define what makes nothing to be swapped:
        // a. The index of two elements are the same.
        // b. The index of two elements' values are the same.

        // Input:           [1,3,6,4,1,2]
        // Expected output: [1,2,3,4,1,6]

        print("Given: ")
        print(nums)

        var i = 0
        while (i < nums.size) {
            // The index of the other element matching the value-1 at position, i.
            val j = nums[i]
            if (j > 0 && j < nums.size &&
                i != j && nums[i] != nums[j]) {
                // Swap nums[i] and nums[j]
                val tmp = nums[i]
                nums[i] = nums[j]
                nums[j] = tmp
            } else {
                ++i
            }
        }

        print("Sorted: ")
        print(nums)

        var missing = 1
        for (i in 0..nums.lastIndex) {
            val num = nums[i]
            if (num > 0) {
                if (missing == num) {
                    ++missing
                } else {
                    break
                }
            }
        }

        return missing
    }

    private fun print(nums: IntArray) {
        print("[")
        nums.forEachIndexed { i, v ->
            print("$v")
            if (i < nums.lastIndex) {
                print(",")
            }
        }
        println("]")
    }
}
