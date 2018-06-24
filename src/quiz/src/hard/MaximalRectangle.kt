package hard

import org.junit.Assert
import java.util.*

/**
 * Given a 2D binary matrix filled with 0's and 1's, find the largest rectangle
 * containing only 1's and return its area.
 *
 *
 * Example:
 * m = 1 0 1 0 0
 *     1 0 1 1 1
 *     1 1 1 1 1
 *     1 0 0 1 0
 *
 * The maximal rectangle is:
 *
 * 1 0 1 0 0
 *    .-----.
 * 1 0|1 1 1|
 * 1 1|1 1 1|
 *    '-----'
 * 1 0 0 1 0
 *
 * Reference:
 *
 *  * https://leetcode.com/problems/maximal-rectangle/description/
 *
 */
class MaximalRectangle {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val startTs: Long = System.currentTimeMillis()

            val test = MaximalRectangle()

            // Solution 1
            for (v in sVerification) {
                Assert.assertEquals(
                    v.expected.toLong(),
                    test.maximalRectangle(v.matrix).toLong())
            }
            println(String.format(Locale.ENGLISH, "Solution 1 takes %d ms",
                                  System.currentTimeMillis() - startTs))
        }

        private val sVerification = arrayOf(
//            new Verification(6, new char[][]{
//                new char[]{'1', '0', '1', '0', '0'},
//                new char[]{'1', '0', '1', '1', '1'},
//                new char[]{'1', '1', '1', '1', '1'},
//                new char[]{'1', '0', '0', '1', '0'}
//            }),
            Verification(8, arrayOf(charArrayOf('1', '0', '1', '1', '0', '1'), charArrayOf('1', '1', '1', '1', '1', '1'), charArrayOf('0', '1', '1', '0', '1', '1'), charArrayOf('1', '1', '1', '0', '1', '0'), charArrayOf('1', '1', '0', '1', '1', '1'))))
    }

    // Solution #1 ////////////////////////////////////////////////////////////

    /**
     *
     *
     * Naive approach.
     * Beats 41% of other submission.
     *
     */
    private fun maximalRectangle(matrix: Array<CharArray>?): Int {
        if (matrix == null) return 0

        val maxRow = matrix.size
        if (maxRow == 0) return 0
        val maxCol = matrix[0].size
        if (maxCol == 0) return 0
        val lookupTable = IntArray(maxCol)

        var maxArea = 0

        for (row in 0 until maxRow) {
            for (col in 0 until maxCol) {
                val value = matrix[row][col] - '0'
                if (value > 0) {
                    lookupTable[col] += 1
                } else {
                    lookupTable[col] = 0
                }
            }
            maxArea = Math.max(
                maxArea,
                largestRectangleArea(lookupTable))
        }

        return maxArea
    }

    private fun largestRectangleArea(hist: IntArray): Int {
        // Create an empty stack. The stack holds indexes of hist[] array
        // The bars stored in stack are always in increasing order of their
        // heights.
        val s = Stack<Int>()

        // Initialize max area
        var maxArea = 0
        // To store top of stack
        var tp: Int
        // To store area with top bar as the smallest bar
        var areaWithTop: Int

        // Run through all bars of given histogram
        var i = 0
        while (i < hist.size) {
            // If this bar is higher than the bar on top stack, push it to stack
            if (s.empty() || hist[s.peek()] <= hist[i]) {
                s.push(i++)

                // If this bar is lower than top of stack, then calculate area of
                // rectangle with stack top as the smallest (or minimum height)
                // bar. 'i' is 'right index' for the top and element before top in
                // stack is 'left index'
            } else {
                tp = s.pop()  // store the top index

                val w = if (s.empty()) i else i - s.peek() - 1
                // Calculate the area with hist[tp] stack as smallest bar
                areaWithTop = hist[tp] * w

                // update max area, if needed
                if (maxArea < areaWithTop)
                    maxArea = areaWithTop
            }
        }

        // Now pop the remaining bars from stack and calculate area with every
        // popped bar as the smallest bar
        while (!s.empty()) {
            tp = s.pop()

            val w = if (s.empty()) i else i - s.peek() - 1
            areaWithTop = hist[tp] * w

            if (maxArea < areaWithTop)
                maxArea = areaWithTop
        }

        return maxArea
    }

    data class Verification(val expected: Int,
                            val matrix: Array<CharArray>) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Verification

            if (expected != other.expected) return false
            if (!Arrays.equals(matrix, other.matrix)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = expected
            result = 31 * result + Arrays.hashCode(matrix)
            return result
        }
    }
}
