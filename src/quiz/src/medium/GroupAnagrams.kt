package medium

import org.junit.Assert
import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap

/**
 * Given an array of strings, group anagrams together.
 *
 * Example 1:
 *
 * ```
 * Input: ["eat", "tea", "tan", "ate", "nat", "bat"]
 * Output: [
 *          ["ate", "eat","tea"],
 *          ["nat" ,"tan"],
 *          ["bat"]
 *         ]
 * ```
 *
 * Reference:
 *
 *  * https://leetcode.com/problems/group-anagrams/description/
 *
 */
class GroupAnagrams {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val solver = GroupAnagrams()

            Assert.assertEquals(
                AnagramGroup(arrayListOf(arrayListOf("ate", "eat","tea"),
                                         arrayListOf("nat" ,"tan"),
                                         arrayListOf("bat"))),
                AnagramGroup(solver.groupAnagrams1(arrayOf("eat", "tea", "tan", "ate", "nat", "bat"))))
        }
    }

    // Solution #1 ////////////////////////////////////////////////////////////

    /**
     * Beats 71%
     */
    fun groupAnagrams1(strs: Array<String>): List<List<String>> {
        // O(n).
        val map = HashMap<String, MutableList<String>>()
        for (str in strs) {
            // Ignore space
            val strWithoutSpace = str.replace(" ", "")
            val chars = strWithoutSpace.toCharArray()
            chars.sort()

            val key = String(chars)

            if (!map.containsKey(key)) map[key] = ArrayList()
            map[key]?.add(str)
        }

        return ArrayList(map.values)
    }

    // Verification ///////////////////////////////////////////////////////////

    class AnagramGroup(groups: List<List<String>>) {

        private val hashCodes = mutableListOf<Int>()

        init {
            groups.forEach { g ->
                val chars = g[0].toCharArray()
                Arrays.sort(chars)

                // Prepare the key regarding to ignoring space
                val code = String(chars).replace(" ", "").hashCode()

                hashCodes.add(code)
            }

            hashCodes.sort()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AnagramGroup

            if (hashCodes != other.hashCodes) return false

            return true
        }

        override fun hashCode(): Int {
            return hashCodes.hashCode()
        }
    }
}
