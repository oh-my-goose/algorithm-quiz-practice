package medium

import org.junit.Assert
import java.lang.RuntimeException
import java.util.*

/**
 * Please implement the [put], [delete], [contains], [floor], [ceiling], [dist],
 * [lca], [rotateRightAt], [rotateLeftAt] and [printInOrder] of an integer Binary
 * Search Tree.
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
            Assert.assertEquals("15 10 8 1 11 30 19 17 21 20 25 23 31", tree1.printPreOrder())
            Assert.assertEquals("1 8 10 11 15 17 19 20 21 23 25 30 31", tree1.printInOrder())

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

            // "lca" method test
            Assert.assertEquals(20, tree1.lca(18, 23))
            Assert.assertEquals(17, tree1.lca(1, 25))
            // "lca" not found test
            try {
                tree1.lca(17, 23)
                throw NoSuchElementException()
            } catch (ignored: Throwable) {
                // IGNORED
            }

            // "dist" method test
            Assert.assertEquals(8, tree1.dist(1, 23))
            Assert.assertEquals(3, tree1.dist(20, 23))
            Assert.assertEquals(4, tree1.dist(18, 23))
            // "dist" not found test
            try {
                tree1.dist(17, 23)
                throw NoSuchElementException()
            } catch (ignored: Throwable) {
                // IGNORED
            }

            // "rotateRightAt" method test
            val tree2 = BinarySearchTree()
            tree2.put(15)
            tree2.put(10)
            tree2.put(8)
            tree2.put(11)
            tree2.put(9)
            tree2.put(1)
            //       15
            //       /
            //      10
            //     / \
            //    8   11
            //   / \
            //  1   9
            Assert.assertEquals("15 10 8 1 9 11", tree2.printPreOrder())
            //       15    ->       15
            //       /     ->       /
            //     [10]    ->      8
            //     / \     ->     / \
            //    8   11   ->    1  [10]
            //   / \       ->        / \
            //  1   9      ->       9   11
            tree2.rotateRightAt(10)
            Assert.assertEquals("15 8 1 10 9 11", tree2.printPreOrder())
            //    [15]     ->        8
            //     /       ->       /  \
            //    8        ->      1   15
            //   / \       ->          / \
            //  1   10     ->        10
            //      / \    ->        / \
            //     9   11  ->       9   11
            tree2.rotateRightAt(15)
            Assert.assertEquals("8 1 15 10 9 11", tree2.printPreOrder())
        }
    }

    private var root: Node? = null

    // Solution #1 ////////////////////////////////////////////////////////////

    fun clear() {
        root = null
    }

    /**
     * Add an integer to the tree.
     */
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

    /**
     * Delete an integer from the tree.
     */
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

    /**
     * Check if an integer is in the tree.
     */
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

    /**
     * Find the distance between two integer in the tree.
     */
    fun dist(k: Int, l: Int): Int {
        // Find k
        val pathK = LinkedList<Int>()
        find(root, k, pathK)
        if (pathK.isEmpty()) throw NoSuchElementException()
        val stepsK = pathK.size

        // Find l
        val pathL = LinkedList<Int>()
        find(root, l, pathL)
        if (pathL.isEmpty()) throw NoSuchElementException()
        val stepsL = pathL.size

        // Find common steps
        var commonSteps = 0
        while (pathK.isNotEmpty() && pathL.isNotEmpty()) {
            val ancestorK = pathK.pollFirst()
            val ancestorL = pathL.pollFirst()

            if (ancestorK == ancestorL) {
                ++commonSteps
            } else {
                break
            }
        }

        return stepsK + stepsL - 2 * commonSteps
    }

    /**
     * Find the least common ancestor of two integer in the tree.
     */
    fun lca(k: Int, l: Int): Int {
        // Find k
        val pathK = LinkedList<Int>()
        find(root, k, pathK)

        // Find l
        val pathL = LinkedList<Int>()
        find(root, l, pathL)

        return lca(pathK, pathL)
    }

    private fun lca(q1: Deque<Int>, q2: Deque<Int>): Int {
        if (q1.isEmpty()) throw NoSuchElementException()
        if (q2.isEmpty()) throw NoSuchElementException()
        var ancestor = root?.value ?: throw NoSuchElementException()

        while (q1.isNotEmpty() && q2.isNotEmpty()) {
            val ancestorK = q1.pollFirst()
            val ancestorL = q2.pollFirst()

            if (ancestorK == ancestorL) {
                ancestor = ancestorK
            } else {
                break
            }
        }

        return ancestor
    }

    fun rotateLeftAt(k: Int) {
        //
        //    [P]      ->        Q
        //    / \      ->       / \
        //   A   Q     ->     [P]  C
        //      / \    ->     / \
        //     B   C   ->    A   B
        //
        val (pivot, pivotAncestor) = find(root, k)
        if (pivot == null) throw NoSuchElementException()

        // Pivot's right child is going to be the new pivot
        val pivotRight = pivot.right
        val pivotRightLeft = pivotRight?.left

        // Reassign the direct child of ancestor
        if (pivotAncestor == null) {
            root = pivotRight
        } else {
            if (pivotAncestor.left == pivot) {
                pivotAncestor.left = pivotRight
            } else {
                pivotAncestor.right = pivotRight
            }
        }

        // Exchange child from pivot to next pivot
        pivot.right = pivotRightLeft

        // Assign the pivot as child of new pivot
        pivotRight?.left = pivot
    }

    fun rotateRightAt(k: Int) {
        //
        //      [Q]    ->       P
        //      / \    ->      / \
        //     P   C   ->     A   Q
        //    / \      ->        / \
        //   A   B     ->       B   C
        //
        val (pivot, pivotAncestor) = find(root, k)
        if (pivot == null) throw NoSuchElementException()

        // Pivot's left child is going to be the new pivot
        val pivotLeft = pivot.left
        val pivotLeftRight = pivot.left?.right

        // Reassign the direct child of ancestor
        if (pivotAncestor == null) {
            root = pivotLeft
        } else {
            if (pivotAncestor.left == pivot) {
                pivotAncestor.left = pivotLeft
            } else {
                pivotAncestor.right = pivotLeft
            }
        }

        // Exchange child from pivot to next pivot
        pivot.left = pivotLeftRight

        // Assign the pivot as child of new pivot
        pivotLeft?.right = pivot
    }

    fun printPreOrder(): String {
        val q = ArrayDeque<Int>()
        printPreOrder(root, q)

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

    private fun printPreOrder(node: Node?, q: Queue<Int>) {
        when (node) {
            null -> {
                return
            }
            else -> {
                q.offer(node.value)
                printPreOrder(node.left, q)
                printPreOrder(node.right, q)
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

    /**
     * Find node and its ancestor.
     */
    private fun find(node: Node?, k: Int, q: Deque<Int>? = null): Pair<Node?, Node?> {
        return if (node == null) {
            // Not found at all!
            q?.clear()
            Pair(null, null)
        } else {
            q?.offer(node.value)

            if (k < node.value) {
                val (candidate, ancestor) = find(node.left, k , q)

                // Identify the pair of candidate and candidate's ancestor
                if (candidate != null && ancestor == null)
                    Pair(candidate, node)
                else
                    Pair(candidate, ancestor)
            } else if (k > node.value) {
                val (candidate, ancestor) = find(node.right, k , q)

                // Identify the pair of candidate and candidate's ancestor
                if (candidate != null && ancestor == null)
                    Pair(candidate, node)
                else
                    Pair(candidate, ancestor)
            } else {
                // Lucky, found it!
                Pair(node, null)
            }
        }
    }

    // Node ///////////////////////////////////////////////////////////////////

    class Node(var value: Int) {

        var left: Node? = null
        var right: Node? = null
    }
}
