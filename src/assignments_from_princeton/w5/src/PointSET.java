import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class PointSET {

    private final RedBlackBST<Point2D, Point2D> mSet;

    /**
     * construct an empty set of points
     */
    public PointSET() {
        mSet = new RedBlackBST<>();
    }

    /**
     * is the set empty?
     */
    public boolean isEmpty() {
        return mSet.isEmpty();
    }

    /**
     * number of points in the set
     */
    public int size() {
        return mSet.size();
    }

    /**
     * add the value to the set (if it is not already in the set)
     */
    public void insert(Point2D p) {
        verifyPoint2D(p);

        mSet.put(p, p);
    }

    /**
     * does the set contain value p?
     */
    public boolean contains(Point2D p) {
        verifyPoint2D(p);

        return mSet.contains(p);
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        for (Point2D key : mSet.keys()) {
            StdDraw.point(key.x(), key.y());
        }
    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        List<Point2D> range = new ArrayList<>();
        for (Point2D key : mSet.keys()) {
            if (rect.contains(key)) {
                range.add(key);
            }
        }

        return range;
    }

    /**
     * a nearest neighbor in the set to value p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        verifyPoint2D(p);

        Point2D nearest = null;
        for (Point2D key : mSet.keys()) {
            if (nearest == null ||
                key.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
                nearest = key;
            }
        }

        return nearest;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected / Private Methods ////////////////////////////////////////////

    private void verifyPoint2D(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }
}