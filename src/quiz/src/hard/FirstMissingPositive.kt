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

            Assert.assertEquals(5, solver.firstMissingPositive(
                intArrayOf(4, -10, 3, 1, -8, 2)))

            Assert.assertEquals(9, solver.firstMissingPositive(
                intArrayOf(1, 2, 3, 4, 5, 6, 7, 8)))
        }
    }

    // Solution #1 ////////////////////////////////////////////////////////////

    /**
     * Beats 71%
     */
    fun firstMissingPositive(nums: IntArray): Int {
        // 1. One way is to use quick-sort and the time complexity is O(n*log(n)),
        // which is relatively close to O(n).
        //
        // 2. Use in place heap sort, which also has the time complexity O(n*log(n)).
        //
        // 3. Another way is to benefit from the nature of consecutively climbing
        // integer sequence, which is the index indicates the value of index+1
        // should be here.
        //
        // We scan the elements, for each element, we find the home of the value
        // by putting the value to position of index of value-1, which literally is
        // the swap of i-th and (nums[i] - 1)th elements!

        // For example,
        // Given input like [4,-10,3,1,-8,2]
        // Where i is the index; j is the value at the index minus 1
        //
        // n: [4,-10,3,1,-8,2]
        // i=0, j=3, n[i]=4, n[j]=1 => swap (take "4" home)
        //
        // n: [1,-10,3,4,-8,2]
        // i=0, j=0, n[i]=1, n[j]=1 => ++i
        // i=1, j=-11 => ++i
        // i=2, j=2, n[i]=3, n[j]=3 => ++i
        // i=3, j=3, n[i]=4, n[j]=4 => ++i
        // i=4, j=-9 => ++i
        // i=5, j=1, n[i]=2, n[j]=-10 => swap (take "2" home)
        //
        // n: [1,2,3,4,-8,-10]
        //
        // Then just walk through the sorted sequence, you'd be finding the missing
        // positive easily

        print("Given: ")
        print(nums)

        var i = 0
        while (i < nums.size) {
            // The index of the other element matching the value-1 at position, i.
            val j = nums[i] - 1

            if (j >= 0 && j < nums.size &&
                nums[i] != nums[j]) {
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
        println("---")

        i = 0
        while (i < nums.size && nums[i] == i + 1) ++i

        return i + 1
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
