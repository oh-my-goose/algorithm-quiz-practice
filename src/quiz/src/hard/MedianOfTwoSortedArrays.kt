package hard

import org.junit.Assert

/**
 * There are two sorted arrays nums1 and nums2 of size m and n respectively.
 * Find the median of the two sorted arrays.
 * <br></br>
 * The overall run time complexity should be O(log (m+n)).
 *
 *
 * Example:
 * <pre>
 * case 1:
 * nums1 = [1, 3]
 * nums2 = [2]
 *
 * The median is 2.0
 *
 * case 2:
 * nums1 = [1, 2]
 * nums2 = [3, 4]
 *
 * The median is (2 + 3)/2 = 2.5
</pre> *
 * Reference:
 *
 *  * https://leetcode.com/problems/median-of-two-sorted-arrays/description/
 *
 */
class MedianOfTwoSortedArrays {

    // Solution #1 ////////////////////////////////////////////////////////////

    fun findMedianSortedArrays(nums1: IntArray,
                               nums2: IntArray): Double {
        // A median is the number in the middle of an array, where the length
        // of left part is equal to the length of the right part.
        //
        // So we need to find a pair of pivots in the two arrays that the total
        // length of the two left parts is equal to the length of the two right
        // parts.
        //
        // For example:
        // nums1: a1, a2, a3, a4
        //                  ^   <-------------.
        // nums2: b1, b2, b3, b4, b5, b6      +-- pair of the pivots.
        //              ^   <-----------------'
        //
        //     left     |      right
        // a1, a2, a3   |  a4
        // b1, b2       |  b3, b4, b5, b6
        //
        // Where all the element of the left part is less than all the elements
        // of the right part!
        // But how to find the pivots???
        // Because the arrays are sorted, a3 < a4 and b2 < b3 are guaranteed.
        // In addition to that, a3 < b3 and b2 < a4 must be also guaranteed!
        //
        // So we got three criteria to fulfill, summarized as:
        // 1. The length of two merged left parts is equal to the length of two
        //    merged right part.
        // 2. The max value of left sub-array #1 is less than the min value of
        //    right sub-array #2 (a3 < b3).
        // 3. The max value of left sub-array #2 is less than the min value of
        //    right sub-array #1 (b2 < a4).
        //
        // With criterion #1, we only need to traverse the shorter array!
        //
        // Once we get the pivots, we could get the median by this formula:
        //
        // For even length:
        // median = avg(max(left1, left2),
        //              min(right1, right2))
        //
        // For odd length:
        // median = max(left1, left2)

        val shorter: IntArray
        val longer: IntArray
        if (nums1.size < nums2.size) {
            shorter = nums1
            longer = nums2
        } else {
            shorter = nums2
            longer = nums1
        }

        val isEven = (shorter.size + longer.size) % 2 == 0

        // nums1:    a1   a2   a3   a4
        // pivot:  0    1    2    3    4    5    6
        // nums2:    b1   b2   b3   b4   b5   b6

        var begin = 0
        var end = shorter.size
        val half = (shorter.size + longer.size + 1) / 2
        var pivotInShorter = getPivot(begin, end)
        var pivotInLonger = half - pivotInShorter

        while (end > begin) {
            if (numOf(shorter, pivotInShorter - 1) < numOf(longer, pivotInLonger + 1) && numOf(longer, pivotInLonger - 1) < numOf(shorter, pivotInShorter + 1)) {
                // The pivots are founded!!!
                break
            } else {
                if (numOf(shorter, pivotInShorter - 1) > numOf(longer, pivotInLonger + 1)) {
                    // Move pivot to left.
                    if (end - begin == 1) {
                        end = begin
                    } else {
                        end = pivotInShorter
                    }
                } else {
                    // Move pivot to right.
                    begin = pivotInShorter
                }

                pivotInShorter = getPivot(begin, end)
                pivotInLonger = half - pivotInShorter
            }
        }

        if (isEven) {
            val leftMax = Math.max(numOf(shorter, pivotInShorter - 1),
                    numOf(longer, pivotInLonger - 1))
            val rightMin = Math.min(numOf(shorter, pivotInShorter + 1),
                    numOf(longer, pivotInLonger + 1))

            return (leftMax + rightMin).toDouble() / 2f
        } else {
            return Math.max(numOf(shorter, pivotInShorter - 1),
                    numOf(longer, pivotInLonger - 1)).toDouble()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected / Private Methods ////////////////////////////////////////////

    private fun getPivot(begin: Int, end: Int): Int {
        // Plus one would make the half lean to right, which means the half would
        // never approach the left margin and we need to handle that corner cases.
        return (begin + end + 1) / 2
    }

    private fun numOf(nums: IntArray, index: Int): Int {
        return if (index < 0) {
            Integer.MIN_VALUE
        } else if (index >= nums.size) {
            Integer.MAX_VALUE
        } else {
            nums[index]
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val test = MedianOfTwoSortedArrays()

            Assert.assertEquals(2.0,
                    test.findMedianSortedArrays(
                            intArrayOf(1, 3),
                            intArrayOf(2)),
                    0.0)
            Assert.assertEquals(2.5,
                    test.findMedianSortedArrays(
                            intArrayOf(1, 2),
                            intArrayOf(3, 4)),
                    0.0)
        }
    }
}
