package medium

import org.junit.Assert
import java.lang.RuntimeException
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
            val tree = BinarySearchTree()

            // "put" method test
            tree.put(15)
            tree.put(10)
            tree.put(8)
            tree.put(1)
            tree.put(11)
            tree.put(30)
            tree.put(19)
            tree.put(17)
            tree.put(21)
            tree.put(25)
            tree.put(20)
            tree.put(23)
            tree.put(31)
            Assert.assertEquals("1 8 10 11 15 17 19 20 21 23 25 30 31", tree.printInOrder())

            // "contain" method test
            Assert.assertTrue(tree.contains(17))
            Assert.assertFalse(tree.contains(18))

            // "floor" method test
            try {
                tree.floor(0)
                throw RuntimeException()
            } catch (ignored: Throwable) {
                // IGNORED.
            }
            Assert.assertEquals(11, tree.floor(12))
            Assert.assertEquals(17, tree.floor(18))
            Assert.assertEquals(21, tree.floor(22))

            // "ceiling" method test
            Assert.assertEquals(1, tree.ceiling(0))
            Assert.assertEquals(8, tree.ceiling(3))
            Assert.assertEquals(17, tree.ceiling(16))

            tree.clear()
        }
    }

    private var root: Node? = null

    // Solution #1 ////////////////////////////////////////////////////////////

    fun clear() {
        root = null
    }

    fun put(value: Int) {
        if (root == null) {
            root = Node(value)
        } else {
            var n = root

            while (n != null) {
                if (value < n.value) {
                    if (n.left == null) {
                        n.left = Node(value)
                        break
                    } else {
                        n = n.left
                    }
                } else if (value > n.value) {
                    if (n.right == null) {
                        n.right = Node(value)
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

    private fun deleteMin(node: Node) {
        TODO()
    }

    fun contains(k: Int): Boolean {
        var n = root
        while (n != null) {
            n = if (k < n.value) {
                n.left
            } else if (k > n.value) {
                n.right
            } else {
                break
            }
        }

        return n?.value == k
    }

    /**
     * Find the largest number in the tree that is equal or less then [k]
     */
    fun floor(k: Int): Int {
        return floor(root, k)?.value ?: throw NoSuchElementException()
    }

    private fun floor(node: Node?, k: Int): Node? {
        return when (node) {
            null -> null
            else -> {
                when {
                    k == node.value -> {
                        // Lucky, k is the element of the tree
                        node
                    }
                    k < node.value -> {
                        // The candidate could be in the left sub-tree
                        floor(node.left, k)
                    }
                    else -> {
                        // The candidate could be in the right sub-tree; If no
                        // candidate is found, node is the largest number that
                        // is equal or less than k
                        floor(node.right, k) ?: node
                    }
                }
            }
        }
    }

    /**
     * Find the smallest number in the tree that is equal or larger then [k]
     */
    fun ceiling(k: Int): Int {
        return ceiling(root, k)?.value ?: throw NoSuchElementException()
    }

    private fun ceiling(node: Node?, k: Int): Node? {
        return when (node) {
            null -> null
            else -> {
                when {
                    k == node.value -> {
                        // Lucky, k is the element of the tree
                        node
                    }
                    k > node.value -> {
                        // The candidate could be in the right sub-tree
                        ceiling(node.right, k)
                    }
                    else -> {
                        // The candidate could be in the right sub-tree; If no
                        // candidate is found, node is the largest number that
                        // is equal or less than k
                        ceiling(node.left, k) ?: node
                    }
                }
            }
        }
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
                q.offer(node.value)
                printInOrder(node.right, q)
            }
        }
    }

    // Node ///////////////////////////////////////////////////////////////////

    class Node(var value: Int) {

        var left: Node? = null
        var right: Node? = null
    }
}
