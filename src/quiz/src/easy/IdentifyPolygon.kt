package easy

import org.junit.Assert

/**
 * Identify whether four sides (given by four integers) can form a square, a
 * rectangle, or neither.
 *
 * Input: You will receive an list of strings, each containing four space-
 * separated integers, which represent the length of the sides of a polygon.
 * The input lines will follow the 'A B C D' order as in the following representation
 *
 * ```
 * |-----A-----|
 * |           |
 * |           |
 * D           B
 * |           |
 * |           |
 * |-----C-----|
 * ```
 *
 * Constraints: The four integers representing the sides will be such that:
 * -2000 <=X <= 2000 (Where X represents the integer)
 *
 * Example:
 *
 * ```
 * Input: [36, 30, 36, 30,
 *         15, 15, 15, 15,
 *         46, 96, 90, 100,
 *         86, 86, 86, 86,
 *         100, 200, 100, 200,
 *         -100, 200, -100, 200]
 * Output: [2, 2, 2]
 * ```
 *
 * Reference:
 *
 *  * https://github.com/malvee/Booking.com/blob/master/polygon.md
 *
 */
class IdentifyPolygon {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val solver = IdentifyPolygon()

            Assert.assertArrayEquals(
                intArrayOf(2, 2, 2),
                solver.getShape(
                    intArrayOf(36, 30, 36, 30,
                               15, 15, 15, 15,
                               46, 96, 90, 100,
                               86, 86, 86, 86,
                               100, 200, 100, 200,
                               -100, 200, -100, 200)))
        }
    }

    // Solution #1 ////////////////////////////////////////////////////////////

    fun getShape(nums: IntArray): IntArray {
        // Prepare the input
        val shapes = mutableListOf<MutableList<Int>>()
        var buffer = mutableListOf<Int>()
        nums.forEach {
            buffer.add(it)
            if (buffer.size == 4) {
                shapes.add(buffer)
                buffer = mutableListOf()
            }
        }

        var numOfSquares = 0
        var numOfRectangles = 0
        var numOfOtherPolygons = 0
        shapes.forEach { shape ->
            val a = shape[0]
            val b = shape[1]
            val c = shape[2]
            val d = shape[3]

            if (a < 0 || b < 0 || c < 0 || d < 0) {
                ++numOfOtherPolygons
            } else {
                if (a == b && b == c && c == d) {
                    ++numOfSquares
                } else if (a == c && b == d) {
                    ++numOfRectangles
                } else {
                    ++numOfOtherPolygons
                }
            }
        }

        return intArrayOf(numOfSquares, numOfRectangles, numOfOtherPolygons)
    }
}
