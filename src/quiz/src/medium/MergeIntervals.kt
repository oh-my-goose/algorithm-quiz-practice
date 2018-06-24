package medium

import org.junit.Assert

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
        if (intervals.isEmpty()) return emptyList()
        else if (intervals.size == 1) return intervals

        // Sort in the ascending order of interval's start
        intervals.sortedWith(Comparator { a, b ->
            return@Comparator when {
                a.start > b.start -> -1
                a.start < b.start -> 1
                else -> 0
            }
        })

        val mergedIntervals = mutableListOf<Interval>()
        var start = intervals[0].start
        var end = intervals[0].end
        for (i in 1..intervals.lastIndex) {
            val current = intervals[i]

            if (current.start <= end) {
                // Extend the interval because there is a overlap
                start = Math.min(start, current.start)
                end = Math.max(end, current.end)
            } else {
                // No overlap found, add new interval
                mergedIntervals.add(Interval(start = start, end = end))
                // Reset the cache
                start = current.start
                end = current.end
            }
        }

        mergedIntervals.add(Interval(start = start, end = end))

        return mergedIntervals
    }

    data class Interval(var start: Int = 0,
                        var end: Int = 0) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Interval

            if (start != other.start) return false
            if (end != other.end) return false

            return true
        }

        override fun hashCode(): Int {
            var result = start
            result = 31 * result + end
            return result
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val tester = MergeIntervals()

            // Input:
            // [-------------------]
            //       [-----]
            // +-+-+-+-+-+-+-+-+-+-+-...
            // 0 1 2 3 4 5 6 7 8 9 10
            Assert.assertEquals(
                listOf(Interval(start = 1, end = 10)),
                tester.merge(listOf(
                    Interval(start = 1, end = 10),
                    Interval(start = 3, end = 6))))

            // Input:
            //   [---]
            //             [-]
            //           [-]
            //                 [-]
            //                 [-]
            //                   [-]
            // +-+-+-+-+-+-+-+-+-+-+-...
            // 0 1 2 3 4 5 6 7 8 9 10
            Assert.assertEquals(
                listOf(Interval(start = 1, end = 3),
                       Interval(start = 5, end = 7),
                       Interval(start = 8, end = 10)),
                tester.merge(listOf(
                    Interval(start = 1, end = 3),
                    Interval(start = 6, end = 7),
                    Interval(start = 5, end = 6),
                    Interval(start = 8, end = 9),
                    Interval(start = 8, end = 9),
                    Interval(start = 9, end = 10))))
        }
    }
}
