package medium

import org.junit.Assert

import java.util.Locale
import java.util.Stack

/**
 * Given a collection of intervals, merge all overlapping intervals.
 *
 * Example 1:
 *
 * Input: [[1,3],[2,6],[8,10],[15,18]]
 * Output: [[1,6],[8,10],[15,18]]
 * Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
 *
 * Example 2:
 *
 * Input: [[1,4],[4,5]]
 * Output: [[1,5]]
 * Explanation: Intervals [1,4] and [4,5] are considered overlapping.
 *
 * Reference:
 *
 *  * https://leetcode.com/problems/maximal-rectangle/description/
 *
 */
class MergeIntervals {

    // Solution #1 ////////////////////////////////////////////////////////////

    fun merge(intervals: List<Interval>): List<Interval> {
        TODO()
    }

    class Interval(var start: Int = 0,
                   var end: Int = 0)

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
        }
    }
}
