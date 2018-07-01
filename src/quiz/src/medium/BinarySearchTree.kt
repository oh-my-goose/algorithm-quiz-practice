package medium

import org.junit.Assert
import java.util.*

/**
 * Please implement the [put], [delete], [contains], [floor], [ceiling], and
 * [printInOrder] of an integer Binary Search Tree.
 *
 * Example:
 *
 * ```
 *                 A
 *               /   \
 *              B     C
 *             /\    /  \
 *                  D    E
 *                 / \  / \
 *                     F
 * ```
 *
 * Reference:
 *
 *  * PUT, CONTAINS, ... https://www.coursera.org/learn/algorithms-part1/lecture/7An9B/binary-search-trees
 *  * DELETE, https://www.coursera.org/learn/algorithms-part1/lecture/PWZAr/deletion-in-bsts
 *
 */
class BinarySearchTree {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val tree1 = BinarySearchTree()

            tree1.put(15)
            tree1.put(10)
            tree1.put(8)
            tree1.put(1)
            tree1.put(11)
            tree1.put(30)
            tree1.put(19)
            tree1.put(17)
            tree1.put(21)
            tree1.put(25)
            tree1.put(20)
            tree1.put(23)
            tree1.put(31)
            Assert.assertEquals("1 8 10 11 15 17 19 20 21 23 25 30 31", tree1.printInOrder())
        }
    }

    private var root: Node? = null

    // Solution #1 ////////////////////////////////////////////////////////////

    fun put(key: Int) {
        if (root == null) {
            root = Node(key)
        } else {
            var n = root

            while (n != null) {
                if (key < n.key) {
                    if (n.left == null) {
                        n.left = Node(key)
                        break
                    } else {
                        n = n.left
                    }
                } else if (key > n.key) {
                    if (n.right == null) {
                        n.right = Node(key)
                        break
                    } else {
                        n = n.right
                    }
                }
            }
        }
    }

    fun delete(key: Int) {
        TODO()
    }

    fun contains(key: Int): Boolean {
        TODO()
    }

    fun floor(key: Int): Boolean {
        TODO()
    }

    fun ceiling(key: Int): Boolean {
        TODO()
    }

    fun printInOrder(): String {
        val q = ArrayDeque<Int>()
        printInOrder(root, q)

        val builder = StringBuilder()
        while (q.isNotEmpty()) {
            val num = q.pop()

            builder.append(num)
            if (q.isNotEmpty()) {
                builder.append(" ")
            }
        }

        return builder.toString()
    }

    private fun printInOrder(node: Node?, q: Queue<Int>) {
        when (node) {
            null -> {
                return
            }
            else -> {
                printInOrder(node.left, q)
                q.offer(node.key)
                printInOrder(node.right, q)
            }
        }
    }

    // Node ///////////////////////////////////////////////////////////////////

    class Node(var key: Int) {

        var left: Node? = null
        var right: Node? = null
    }
}
