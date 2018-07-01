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
            // Test set #1 ////////////////////////////////////////////////////
            val tree1 = BinarySearchTree()

            // "put" method test
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

            // The tree looks like:
            //
            //          15
            //         /  \
            //       10    30
            //      / \    / \
            //     8  11  19  31
            //    /      / \
            //   1      17 21
            //            /  \
            //           20  25
            //              /
            //             23
            //

            // "contain" method test
            Assert.assertTrue(tree1.contains(17))
            Assert.assertFalse(tree1.contains(18))

            // "floor" method test
            try {
                tree1.floor(0)
                throw RuntimeException()
            } catch (ignored: Throwable) {
                // IGNORED.
            }
            Assert.assertEquals(11, tree1.floor(12))
            Assert.assertEquals(17, tree1.floor(18))
            Assert.assertEquals(21, tree1.floor(22))

            // "ceiling" method test
            Assert.assertEquals(1, tree1.ceiling(0))
            Assert.assertEquals(8, tree1.ceiling(3))
            Assert.assertEquals(17, tree1.ceiling(16))

            // "delete" method test
            // Case 1: no such integer
            tree1.delete(0)
            Assert.assertEquals("1 8 10 11 15 17 19 20 21 23 25 30 31", tree1.printInOrder())
            // Case 2: delete 11
            //
            //          15             ->          15
            //         /  \            ->         /  \
            //       10    30          ->       10    30
            //      / \    / \         ->      /      / \
            //     8 [11] 19  31       ->     8      19  31
            //    /      / \           ->    /      / \
            //   1      17 21          ->   1      17 21
            //            /  \         ->            /  \
            //           20  25        ->           20  25
            //              /          ->              /
            //             23          ->             23
            //
            tree1.delete(11)
            Assert.assertEquals("1 8 10 15 17 19 20 21 23 25 30 31", tree1.printInOrder())
            // Case 2: delete 19
            //
            //          15             ->          15
            //         /  \            ->         /  \
            //       10    30          ->       10    30
            //      /      / \         ->      /      / \
            //     8     [19] 31       ->     8      20  31
            //    /      / \           ->    /      / \
            //   1      17 21          ->   1      17 21
            //            /  \         ->               \
            //           20  25        ->               25
            //              /          ->              /
            //             23          ->             23
            //
            tree1.delete(19)
            Assert.assertEquals("1 8 10 15 17 20 21 23 25 30 31", tree1.printInOrder())
            // Case 2: put 18, delete 15
            //
            //          15             ->         [15]            ->          17
            //         /  \            ->         /  \            ->         /  \
            //       10    30          ->       10    30          ->       10    30
            //      /      / \         ->      /      / \         ->      /      / \
            //     8      19  31       ->     8      20  31       ->     8      20  31
            //    /      / \           ->    /      / \           ->    /      / \
            //   1      17 21          ->   1      17 21          ->   1      18 21
            //               \         ->           \   \         ->               \
            //               25        ->          [18] 25        ->               25
            //              /          ->              /          ->              /
            //             23          ->             23          ->             23
            //
            tree1.put(18)
            tree1.delete(15)
            Assert.assertEquals("1 8 10 17 18 20 21 23 25 30 31", tree1.printInOrder())
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

    fun delete(k: Int) {
        root = deleteNode(root, k)
    }

    private fun deleteNode(node: Node?, k: Int): Node? {
        return when {
            node == null -> null
            k < node.value -> {
                node.left = deleteNode(node.left, k)
                node
            }
            k > node.value -> {
                node.right = deleteNode(node.right, k)
                node
            }
            else -> {
                if (node.left == null && node.right == null) {
                    // No child
                    null
                } else if(node.left == null && node.right != null) {
                    // Promote the only right child
                    node.right
                } else if (node.left != null && node.right == null) {
                    // Promote the only left child
                    node.left
                } else {
                    // Has both children

                    // First, find the successor in the right sub-tree, where the
                    // successor is the smallest integer in the right sub-tree that
                    // is larger than current integer and smaller than the right
                    // child.
                    val successor = min(node.right!!)
                    successor.right = deleteMin(node.right)
                    // The successor must not has left child, so it's simple to
                    // assign the new left child to it.
                    successor.left = node.left

                    // Promote the successor
                    successor
                }
            }
        }
    }

    /**
     * Find minimum integer in the sub-tree of the [node].
     */
    private fun min(node: Node): Node {
        return node.left?.let { left ->
            min(left)
        } ?: node
    }

    /**
     * Delete the minimum integer in the sub-tree of the [node] and return the
     * new left child.
     */
    private fun deleteMin(node: Node?): Node? {
        return if (node == null)  {
            null
        } else if (node.left == null && node.right == null) {
            null
        } else if (node.left == null && node.right != null) {
            // Left child is null but right child is present
            node.right
        } else {
            // Left child is present and no matter whether the right child is
            // present or not
            node.left = deleteMin(node.left)
            node
        }
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
