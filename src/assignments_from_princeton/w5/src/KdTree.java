import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node mRoot = null;
    private int mSize = 0;

    /**
     * construct an empty set of points
     */
    public KdTree() {

    }

    /**
     * is the set empty?
     */
    public boolean isEmpty() {
        return mRoot == null;
    }

    /**
     * number of points in the set
     */
    public int size() {
        return mSize;
    }

    /**
     * add the value to the set (if it is not already in the set)
     */
    public void insert(Point2D p) {
        verifyPoint2D(p);

        mRoot = put(mRoot, p, 0);
    }

    /**
     * does the set contain value p?
     */
    public boolean contains(Point2D p) {
        verifyPoint2D(p);

        return get(p) != null;
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        draw(mRoot);
    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     */
    public Iterable<Point2D> range(RectHV rect) {
        verifyRectVH(rect);

        List<Point2D> pointsInRect = new ArrayList<>();
        range(mRoot, 0, rect, pointsInRect);

        return pointsInRect;
    }

    /**
     * a nearest neighbor in the set to value p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        verifyPoint2D(p);

        if (isEmpty()) {
            return null;
        } else {
            return nearest(mRoot, p, 0);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected / Private Methods ////////////////////////////////////////////

    private void verifyPoint2D(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }

    private void verifyRectVH(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
    }

    private Node put(Node node,
                     Point2D p,
                     int level) {
        // An in-order traversal.
        if (node == null) {
            ++mSize;
            return new Node(p);
        }

        final boolean isSplitVertically = (level % 2 == 0);
        final int cmp = comparePointVH(p, node.value, isSplitVertically);

        if (cmp <= 0) {
            node.left = put(node.left, p, level + 1);
        } else {
            node.right = put(node.right, p, level + 1);
        }

        return node;
    }

    private Point2D get(Point2D p) {
        // An in-order traversal.
        Node node = mRoot;
        if (node == null) return null;

        int level = 0;
        while (node != null) {
            final boolean isSplitVertically = (level % 2 == 0);

            // Visit current node.
            final int cmp = comparePointVH(p, node.value, isSplitVertically);
            if (cmp == 0) {
                return node.value;
            } else if (cmp < 0) {
                // Visit left subtree.
                node = node.left;
            } else {
                // Visit right subtree.
                node = node.right;
            }

            ++level;
        }

        return null;
    }

    private void range(Node node,
                       int level,
                       RectHV rect,
                       List<Point2D> pointsInRect) {
        // An in-order traversal.
        if (node == null) return;

        // Visit current node.
        final Point2D p = node.value;
        if (rect.contains(p)) {
            pointsInRect.add(p);
        }

        final boolean isSplitVertically = (level % 2 == 0);
        final boolean goLeft;
        final boolean goRight;
        if (isSplitVertically) {
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax()) {
                goLeft = true;
                goRight = true;
            } else if (p.x() < rect.xmin()) {
                goLeft = true;
                goRight = false;
            } else {
                goLeft = false;
                goRight = true;
            }
        } else {
            if (p.y() >= rect.ymin() && p.y() <= rect.ymax()) {
                goLeft = true;
                goRight = true;
            } else if (p.y() < rect.ymin()) {
                goLeft = true;
                goRight = false;
            } else {
                goLeft = false;
                goRight = true;
            }
        }

        // Visit left subtree.
        if (goLeft) {
            range(node.left, level + 1, rect, pointsInRect);
        }
        // Visit right subtree.
        if (goRight) {
            range(node.right, level + 1, rect, pointsInRect);
        }
    }

    private Point2D nearest(Node node,
                            Point2D targetPoint,
                            int level) {
        // A post-order traversal.
        if (node == null) return null;

        final boolean isSplitVertically = (level % 2 == 0);
        final int side = comparePointVH(targetPoint, node.value, isSplitVertically);

        // Visit left subtree.
        final Point2D nearestLeft;
        if (node.left != null &&
            side == comparePointVH(targetPoint, node.left.value, isSplitVertically)) {
            nearestLeft = nearest(node.left, targetPoint, level + 1);
        } else {
            nearestLeft = null;
        }

        // Visit right subtree.
        final Point2D nearestRight;
        if (node.right != null &&
            side == comparePointVH(targetPoint, node.right.value, isSplitVertically)) {
            nearestRight = nearest(node.right, targetPoint, level + 1);
        } else {
            nearestRight = null;
        }

        // Visit current node.
        Point2D nearest = node.value;
        if (nearestLeft != null &&
            targetPoint.distanceSquaredTo(nearestLeft) < targetPoint.distanceSquaredTo(nearest)) {
            nearest = nearestLeft;
        }
        if (nearestRight != null &&
            targetPoint.distanceSquaredTo(nearestRight) < targetPoint.distanceSquaredTo(nearest)) {
            nearest = nearestRight;
        }

        return nearest;
    }

    private void draw(Node node) {
        // An in-order traversal.
        if (node == null) return;

        final Point2D p = node.value;
        StdDraw.point(p.x(), p.y());

        if (node.left != null) {
            draw(node.left);
        }
        if (node.right != null) {
            draw(node.right);
        }
    }

    private int comparePointVH(Point2D thiz,
                               Point2D that,
                               boolean isSplitVertically) {
        final int cmp;
        if (isSplitVertically) {
            cmp = Double.compare(thiz.x(), that.x());
        } else {
            cmp = Double.compare(thiz.y(), that.y());
        }

        return cmp;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Clazz //////////////////////////////////////////////////////////////////

    private static class Node {

        final Point2D value;

        Node left = null;
        Node right = null;

        Node(Point2D p) {
            this.value = p;
        }
    }
}
