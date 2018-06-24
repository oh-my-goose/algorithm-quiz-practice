package medium

import org.junit.Assert

import java.util.Collections
import java.util.HashSet

/**
 * Given a string s, find the longest palindromic substring in s. You may assume
 * that the maximum length of s is 1000.
 *
 * This is case sensitive, for example "Aa" is not considered a palindrome here.
 *
 * For example:
 * Input: "babad"
 * Output: "bab"
 * Note: "aba" is also a valid answer.
 *
 * Input: "cbbd"
 * Output: "bb"
 * References:
 *
 *  * https://leetcode.com/problems/longest-palindromic-substring/description/
 *
 */
class LongestPalindromicSubstring {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val test = LongestPalindromicSubstring()

            verify(arrayOf("bab", "aba"),
                    test.longestPalindrome_Sol1("babad"))
            verify(arrayOf("ccabbbacc"),
                    test.longestPalindrome_Sol1("babadcctcaabbbbbccabbbacc"))
            verify(arrayOf("xrcrx"),
                    test.longestPalindrome_Sol1(
                            "cyyoacmjwjubfkzrrbvquqkwhsxvmytmjvbborrtoi" +
                                    "yotobzjmohpadfrvmxuagbdczsjuekjrmcwyaovpio" +
                                    "gspbslcppxojgbfxhtsxmecgqjfuvahzpgprscjwwu" +
                                    "twoiksegfreortttdotgxbfkisyakejihfjnrdngkw" +
                                    "jxeituomuhmeiesctywhryqtjimwjadhhymydlsmcp" +
                                    "ycfdzrjhstxddvoqprrjufvihjcsoseltpyuaywgio" +
                                    "cfodtylluuikkqkbrdxgjhrqiselmwnpdzdmpsvbfi" +
                                    "mnoulayqgdiavdgeiilayrafxlgxxtoqskmtixhbyj" +
                                    "ikfmsmxwribfzeffccczwdwukubopsoxliagenzwkb" +
                                    "iveiajfirzvngverrbcwqmryvckvhpiioccmaqoxgm" +
                                    "bwenyeyhzhliusupmrgmrcvwmdnniipvztmtklihob" +
                                    "bekkgeopgwipihadswbqhzyxqsdgekazdtnamwzbit" +
                                    "wfwezhhqznipalmomanbyezapgpxtjhudlcsfqondo" +
                                    "iojkqadacnhcgwkhaxmttfebqelkjfigglxjfqegxp" +
                                    "cawhpihrxydprdgavxjygfhgpcylpvsfcizkfbqzdn" +
                                    "mxdgsjcekvrhesykldgptbeasktkasyuevtxrcrxmi" +
                                    "ylrlclocldmiwhuizhuaiophykxskufgjbmcmzpogp" +
                                    "myerzovzhqusxzrjcwgsdpcienkizutedcwrmowwol" +
                                    "ekockvyukyvmeidhjvbkoortjbemevrsquwnjoaikh" +
                                    "bkycvvcscyamffbjyvkqkyeavtlkxyrrnsmqohyyqx" +
                                    "zgtjdavgwpsgpjhqzttukynonbnnkuqfxgaatpilrr" +
                                    "xhcqhfyyextrvqzktcrtrsbimuokxqtsbfkrgoiznh" +
                                    "iysfhzspkpvrhtewthpbafmzgchqpgfsuiddjkhnwc" +
                                    "hpleibavgmuivfiorpteflholmnxdwewj"))

            verify(arrayOf("bab", "aba"),
                    test.longestPalindrome_Sol2("babad"))
            verify(arrayOf("ccabbbacc"),
                    test.longestPalindrome_Sol2("babadcctcaabbbbbccabbbacc"))
            verify(arrayOf("xrcrx"),
                    test.longestPalindrome_Sol2(
                            "cyyoacmjwjubfkzrrbvquqkwhsxvmytmjvbborrtoi" +
                                    "yotobzjmohpadfrvmxuagbdczsjuekjrmcwyaovpio" +
                                    "gspbslcppxojgbfxhtsxmecgqjfuvahzpgprscjwwu" +
                                    "twoiksegfreortttdotgxbfkisyakejihfjnrdngkw" +
                                    "jxeituomuhmeiesctywhryqtjimwjadhhymydlsmcp" +
                                    "ycfdzrjhstxddvoqprrjufvihjcsoseltpyuaywgio" +
                                    "cfodtylluuikkqkbrdxgjhrqiselmwnpdzdmpsvbfi" +
                                    "mnoulayqgdiavdgeiilayrafxlgxxtoqskmtixhbyj" +
                                    "ikfmsmxwribfzeffccczwdwukubopsoxliagenzwkb" +
                                    "iveiajfirzvngverrbcwqmryvckvhpiioccmaqoxgm" +
                                    "bwenyeyhzhliusupmrgmrcvwmdnniipvztmtklihob" +
                                    "bekkgeopgwipihadswbqhzyxqsdgekazdtnamwzbit" +
                                    "wfwezhhqznipalmomanbyezapgpxtjhudlcsfqondo" +
                                    "iojkqadacnhcgwkhaxmttfebqelkjfigglxjfqegxp" +
                                    "cawhpihrxydprdgavxjygfhgpcylpvsfcizkfbqzdn" +
                                    "mxdgsjcekvrhesykldgptbeasktkasyuevtxrcrxmi" +
                                    "ylrlclocldmiwhuizhuaiophykxskufgjbmcmzpogp" +
                                    "myerzovzhqusxzrjcwgsdpcienkizutedcwrmowwol" +
                                    "ekockvyukyvmeidhjvbkoortjbemevrsquwnjoaikh" +
                                    "bkycvvcscyamffbjyvkqkyeavtlkxyrrnsmqohyyqx" +
                                    "zgtjdavgwpsgpjhqzttukynonbnnkuqfxgaatpilrr" +
                                    "xhcqhfyyextrvqzktcrtrsbimuokxqtsbfkrgoiznh" +
                                    "iysfhzspkpvrhtewthpbafmzgchqpgfsuiddjkhnwc" +
                                    "hpleibavgmuivfiorpteflholmnxdwewj"))
        }

        private fun verify(expect: Array<String>, actual: String?) {
            val answers = HashSet<String>()

            Collections.addAll(answers, *expect)

            Assert.assertTrue(answers.contains(actual))
        }
    }

    // Solution #2 ////////////////////////////////////////////////////////////

    /**
     * Beats 9% on LeetCode.
     */
    private fun longestPalindrome_Sol2(s: String): String? {
        val n = s.length
        var res: String? = null

        // For example: "babad"
        //
        // You have to visit every possible substring. And for every possible
        // substring, you need to traverse every single characters to tell
        // whether it can form a palindromic substring or not. For example:
        //
        //  b | a | b | a | d
        // ---+---+---+---+---
        //  +
        //  +---+
        //  +--(-)--+           "a" <------------------------------------------.
        //  +-----------+                                                      |
        //  +---------------+                                                  |
        //      +               You might notice there is a duplicated visit, "a".
        //      +---+
        //      +-------+
        //      +-----------+
        //          +
        //          +---+
        //          +-------+
        //              +
        //              +---+
        //                  +
        //
        // That leads this problem to a Dynamic Programming problem:
        //
        // dp(b, e) represents whether s(b ... e) can form a palindromic substring,
        // dp(b, e) is true when s(b) equals to s(e) and s(b+1 ... e-1) is a
        // palindromic substring.
        // When we found a palindrome, check if it's the longest one.
        //
        // According to the previous paradigm, we need a recursive function to
        // visit every possible substring and label the range as the substring is a
        // palindrome, and that's probably not efficient.
        //
        // The previous paradigm is more like a top-to-bottom traversal, and it
        // requires recursive calls, a visit lookup table and a palindromic
        // substring lookup table.
        //
        // If we do a bottom-to-up traversal, we only need a palindromic substring
        // lookup table.
        //
        // So let's change the order of traversing the elements to make it
        // efficient:
        //  b | a | b | a | d
        // ---+---+---+---+---
        //  +
        //      +              <-------------------------.
        //  +---+                                        |
        //          +                                    |
        //      +---+                                    |
        //  +--(-)--+          You have already visited "a" previously
        //              +
        //          +---+      <---------------------------.
        //      +-------+                                  |
        //  +-----------+                                  |
        //                  +                              |
        //              +---+                              |
        //          +-------+                              |
        //      +--(-----)--+  Another visited substring, "ba"
        //  +---------------+
        //
        // Time complexity O(n^2).

        //     end
        //    | b | a | b | a | d
        // ---+---+---+---+---+---
        //  b | t       t
        //  a |     t       t
        //  b |         t
        //  a |             t
        //  d |                 t
        // begin

        val dp = Array(n) { BooleanArray(n) }

        for (end in 0 until n) {
            for (start in end downTo 0) {
                dp[start][end] = s.substring(start, start + 1) == s.substring(end, end + 1) && (end - start < 3 || dp[start + 1][end - 1])

                if (dp[start][end] && (res == null || end - start + 1 > res.length)) {
                    res = s.substring(start, end + 1)
                }
            }
        }

        return res
    }

    // Solution #1 ////////////////////////////////////////////////////////////

    /**
     * Beats ?% on LeetCode.
     */
    private fun longestPalindrome_Sol1(s: String?): String? {
        if (s == null) {
            return null
        } else if (s.length == 1) {
            return s
        }

        // Search for all palindromes.
        // Time complexity: approximately n^3
        var longest: String? = null
        for (i in 0 until s.length) {
            for (j in s.length downTo i + 1) {
                // If the substring length is even smaller than the cache
                // longest one.
                if (longest != null && j - i + 1 < longest.length)
                    break

                val ss = s.substring(i, j)
                if (isValidPalindrome(ss)) {
                    if (longest == null || ss.length > longest.length) {
                        longest = ss
                    }
                }
            }
        }

        return longest
    }

    private fun isValidPalindrome(s: String): Boolean {
        var i = 0
        var j = s.length - 1
        while (i < j) {
            if (s[i++] != s[j--]) {
                return false
            }
        }

        return true
    }
}
