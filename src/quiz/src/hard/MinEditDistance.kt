package hard

import org.junit.Assert

/**
 * Given two words word1 and word2, find the minimum number of operations required
 * to convert word1 to word2. You have the following 3 operations permitted on a
 * word:
 *
 * Insert a character
 * Delete a character
 * Replace a character
 *
 * Example 1:
 *
 * ```
 * Input: word1 = "horse", word2 = "ros"
 * Output: 3
 * Explanation:
 * horse -> rorse (replace 'h' with 'r')
 * rorse -> rose (remove 'r')
 * rose -> ros (remove 'e')
 * ```
 *
 * Example 2:
 *
 * ```
 * Input: word1 = "intention", word2 = "execution"
 * Output: 5
 * Explanation:
 * intention -> inention (remove 't')
 * inention -> enention (replace 'i' with 'e')
 * enention -> exention (replace 'n' with 'x')
 * exention -> exection (replace 'n' with 'c')
 * exection -> execution (insert 'u')
 * ```
 *
 * Note: Your algorithm should run in O(n) time and uses constant extra space.
 *
 * Reference:
 *
 *  * https://leetcode.com/problems/edit-distance/description/
 *
 */
class MinEditDistance {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val solver = MinEditDistance()

            Assert.assertEquals(0, solver.minDistance_sol1(
                "", ""))
            Assert.assertEquals(3, solver.minDistance_sol1(
                "", "ros"))
            Assert.assertEquals(2, solver.minDistance_sol1(
                "zoo",
                "o"))
            Assert.assertEquals(3, solver.minDistance_sol1(
                "horse", "ros"))
            Assert.assertEquals(5, solver.minDistance_sol1(
                "intention", "execution"))
            Assert.assertEquals(10, solver.minDistance_sol1(
                "zoologicoarchaeologist",
                "zoogeologist"))
        }
    }

    // Solution #1 ////////////////////////////////////////////////////////////

    /**
     * Beats 67%
     */
    fun minDistance_sol1(word1: String,
                         word2: String): Int {
        // DP vs DC:
        // 1. Optimal structure (Levenstein distance is the optimal structure).
        // 2. There is overlapping sub-problems.

        // For example,
        // Given s1: "horse", s2="ros", where "i" is insertion, "d" is deletion,
        // and "r" is replacement.
        //
        // For each character, you have 3 options, which means you at least have
        // 3^minSize(s1, s2) + diff(s1, s2) permutations?
        //
        // But the problem asks us to come up with the "minimum" edit distance,
        // which the most optimal solution. In essence, it tells us there is the
        // optimal structure for this problem, so it fits the 1st principle of
        // DP. Then we have to ask ourselves, is there any overlapping sub-problem?
        //
        // The answer is yes because the optimal edit steps is derived by the
        // previously optimal solution. And that's also because of an important
        // characteristic of the problem, the minimum cost of every step is ZERO!
        // It matches the 2nd principle of the DP.
        //
        // It turns out it is a DP problem! To solve it, we need a lookup table
        // for storing minimum edit distance derived from the sub-problem.
        //
        // It's a Levenshtein distance problem. Put the two strings in column-
        // row as below:
        //
        //          str2
        //
        //         \  |  |
        //          \ |  |     If it is str1 centric,
        // str1  -----+-----   1. The cost from left-top (diagonal) is REPLACE
        //            |v v     2. The cost from left is DELETE
        //       ---- |->      3. The cost from top is ADD
        //
        // This is the lookup table for the cost:
        //
        //    -  h  o  r  s  e
        // - [0] 1  2  3  4  5
        // r  1 [1] 2  2  3  4
        // o  2  2 [1] 2  3  4
        // s  3  3  2 [2][2][3]

        if (word1.isEmpty() && word2.isEmpty()) return 0

        val size1 = word1.length
        val size2 = word2.length
        val map = Array(size1 + 1) { _ ->
            IntArray(size2 + 1) { 0 }
        }

        for (i in 0..size1) {
            for (j in 0..size2) {
                if (i == 0 || j == 0) {
                    map[i][j] = Math.max(i, j)
                } else {
                    val c1 = word1[i - 1]
                    val c2 = word2[j - 1]
                    // The add cost and delete cost increase one every step
                    val costOfAdd = map[i][j - 1] + 1
                    val costOfDelete = map[i - 1][j] + 1
                    // The replace cost depends on whether the symbols are the
                    // same
                    val costOfReplace = map[i - 1][j - 1] + if (c1 == c2) 0 else 1

                    map[i][j] = Math.min(costOfReplace, Math.min(costOfAdd, costOfDelete))
                }
            }
        }

//        debug(map, word1, word2)

        return map[size1][size2]
    }

    private fun debug(map: Array<IntArray>,
                      word1: String,
                      word2: String) {
        val size1 = map.size
        val size2 = map[0].size
        val builder = StringBuilder(7 * (size1 + 1) * (size2 + 1) + 10)

        for (j in 0 until size2) {
            if (j == 0) {
                builder.append("           ")
            } else {
                builder.append("  ${word2[j - 1]}  ")
            }
        }
        builder.append(System.lineSeparator())

        for (i in 0 until size1) {
            if (i == 0) {
                builder.append("     ")
            } else {
                builder.append("  ${word1[i - 1]}  ")
            }

            for (j in 0 until size2) {
                builder.append(" %3d ".format(map[i][j]))
            }
            builder.append(System.lineSeparator())
        }

        println(builder)
    }
}
