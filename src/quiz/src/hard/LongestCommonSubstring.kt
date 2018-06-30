package hard

import org.junit.Assert

/**
 * Given two strings ‘X’ and ‘Y’, find the length of the longest common substring.
 *
 * Example:
 * Input : X = "GeeksforGeeks", y = "GeeksQuiz"
 * Output : 5
 * The longest common substring is "Geeks" and is of
 * length 5.
 *
 * Input : X = "abcdxyz", y = "xyzabcd"
 * Output : 4
 * The longest common substring is "abcd" and is of
 * length 4.
 *
 * Input : X = "zxabcdezy", y = "yzabcdezx"
 * Output : 6
 * The longest common substring is "abcdez" and is of
 * length 6.
 *
 * Reference:
 *
 *  * http://www.geeksforgeeks.org/longest-common-substring/
 *
 */
class LongestCommonSubstring {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val solver = LongestCommonSubstring()

            //   G e e k s f o r G e e k s
            // G 1 0 0 0 0 0 0 0 1 0 0 0 0
            // e 0 2 0 0 0 0 0 0 0 2 1 0 0
            // e 0 1 3 0 0 0 0 0 0 1 3 0 0
            // k 0 0 0 4 0 0 0 0 0 0 0 4 0
            // s 0 0 0 0 5 0 0 0 0 0 0 0 5
            // Q 0 0 0 0 0 0 0 0 0 0 0 0 0
            // u 0 0 0 0 0 0 0 0 0 0 0 0 0
            // i 0 0 0 0 0 0 0 0 0 0 0 0 0
            // z 0 0 0 0 0 0 0 0 0 0 0 0 0
            Assert.assertEquals(5, solver.longestCommonSubstring("GeeksforGeeks",
                                                                 "GeeksQuiz").toLong())

            //   a b c d x y z
            // x 0 0 0 0 1 0 0
            // y 0 0 0 0 0 2 0
            // z 0 0 0 0 0 0 3
            // a 1 0 0 0 0 0 0
            // b 0 2 0 0 0 0 0
            // c 0 0 3 0 0 0 0
            // d 0 0 0 4 0 0 0
            Assert.assertEquals(4, solver.longestCommonSubstring("abcdxyz",
                                                                 "xyzabcd").toLong())

            //   a b x d x y z
            // x 0 0 1 0 1 0 0
            // y 0 0 0 0 0 2 0
            // z 0 0 0 0 0 0 3
            // a 1 0 0 0 0 0 0
            // b 0 2 0 0 0 0 0
            // c 0 0 0 0 0 0 0
            // d 0 0 0 1 0 0 0
            Assert.assertEquals(3, solver.longestCommonSubstring("abxdxyz",
                                                                 "xyzabcd").toLong())

            //   z x a b c d e z y
            // y 0 0 0 0 0 0 0 0 1
            // z 1 0 0 0 0 0 0 1 0
            // a 0 0 1 0 0 0 0 0 0
            // b 0 0 0 2 0 0 0 0 0
            // c 0 0 0 0 3 0 0 0 0
            // d 0 0 0 0 0 4 0 0 0
            // e 0 0 0 0 0 0 5 0 0
            // z 1 0 0 0 0 0 0 6 0
            // x 0 2 0 0 0 0 0 0 0
            Assert.assertEquals(6, solver.longestCommonSubstring("zxabcdezy",
                                                                 "yzabcdezx").toLong())
        }
    }

    // Solution #1 ////////////////////////////////////////////////////////////

    private fun longestCommonSubstring(word1: String, word2: String): Int {
        // For example:
        // Given: a b x c x y z
        //    and x y z a b c
        //
        // The simple way is to consider all substrings of first string:
        // a b x c x y z
        // *
        //   *
        // * *
        //     *
        //   * *
        // * * *
        //       *
        //     * *
        //   * * *
        // * * * *
        //         *
        //       * *
        //     * * *
        //   * * * *
        // * * * * *
        //           *
        //         * *
        //       * * *
        //     * * * *
        //   * * * * *
        // * * * * * *
        //             *
        //           * *
        //         * * *
        //       * * * *
        //     * * * * *
        //   * * * * * *
        // * * * * * * *
        //
        // For every substring above, check if it is a substring in the second
        // string:
        //
        //       a b x c x y z
        //       | |
        // x y z a b c d
        //
        // a b x c x y z
        //     |
        //     x y z a b c d
        //
        //     a b x c x y z
        //           |
        // x y z a b c d
        //
        // a b x c x y z
        //         | | |
        //         x y z a b c d
        //
        // Thus time complexity is, O(n^2 * m)
        //
        // You might also notice there are some duplicate comparison, for example:
        // "ab" and "abx". We already check "ab" when checking "abx". We could
        // improve that by memorizing the result.
        // So essentially, it is a Dynamic Programming problem.
        //
        // T[n][m] represents length of common substring
        //
        //    | a | b | x | d | x | y | z
        // ---+---+---+---+---+---+---+---
        //  x | 0 | 0 | 1 | 0 | 1 | 0 | 0
        // ---+---+---+---+---+---+---+---
        //  y | 0 | 0 | 0 | 0 | 0 | 2 | 0
        // ---+---+---+---+---+---+---+---
        //  z | 0 | 0 | 0 | 0 | 0 | 0 | 3
        // ---+---+---+---+---+---+---+---
        //  a | 1 | 0 | 0 | 0 | 0 | 0 | 0
        // ---+---+---+---+---+---+---+---
        //  b | 0 | 2 | 0 | 0 | 0 | 0 | 0
        // ---+---+---+---+---+---+---+---
        //  c | 0 | 0 | 0 | 0 | 0 | 0 | 0
        // ---+---+---+---+---+---+---+---
        //
        // And the maximum number in the matrix is 3, where it is the length of
        // "xyz".

        val n = word1.length
        val m = word2.length
        val map = Array(n) { IntArray(m) }
        var maxLength = 0
        for (i in 0 until n) {
            for (j in 0 until m) {
                val c1 = word1[i]
                val c2 = word2[j]

                if (c1 == c2) {
                    // If the current characters are identical:
                    // a. 1 + length of previous common suffix.
                    // b. 1 if there is no previous common suffix.
                    if (i - 1 >= 0 && j - 1 >= 0) {
                        map[i][j] = map[i - 1][j - 1] + 1
                    } else {
                        map[i][j] = 1
                    }
                } else {
                    // If the current characters are different.
                    map[i][j] = 0
                }

                maxLength = Math.max(maxLength, map[i][j])
            }
        }

        return maxLength
    }
}
