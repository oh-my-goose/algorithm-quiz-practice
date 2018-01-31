import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {

    // Given.
    private final Point[] mSortedPoints;

    private boolean mIsDirty;
    private final List<LineSegment> mSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null || points.length == 0) {
            throw new IllegalArgumentException();
        }

        final Point[] sortedClone = new Point[points.length];
        System.arraycopy(points, 0, sortedClone, 0, points.length);
        Arrays.sort(sortedClone);
        if (hasDuplicatedEntries(sortedClone)) {
            throw new IllegalArgumentException();
        }

        mSortedPoints = sortedClone;
        mIsDirty = true;
    }

    public int numberOfSegments() {
        if (mIsDirty) {
            findSegments(mSortedPoints);
            mIsDirty = false;
        }
        return mSegments.size();
    }

    public LineSegment[] segments() {
        if (mIsDirty) {
            findSegments(mSortedPoints);
            mIsDirty = false;
        }

        final LineSegment[] out = new LineSegment[mSegments.size()];
        mSegments.toArray(out);

        return out;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected / Private Methods ////////////////////////////////////////////

    boolean hasDuplicatedEntries(Point[] points) {
        // For example:
        // 1 2 3 3 4 5 6 6
        //     ^ ^     ^ ^
        // These are duplicates.

        Point prev = points[0];
        for (int i = 1; i < points.length; ++i) {
            if (points[i].compareTo(prev) == 0) {
                return true;
            }

            prev = points[i];
        }

        return false;
    }

    void findSegments(Point[] sortedPoints) {
        mSegments.clear();

        final LinkedList<Point> candidates = new LinkedList<>();
        for (int i = 0; i < sortedPoints.length; ++i) {
            final Point p = sortedPoints[i];

            // Sort the points including "p" by the slope.
            final int size = sortedPoints.length;
            final Point[] pointsBySlope = new Point[size];
            System.arraycopy(mSortedPoints, 0, pointsBySlope, 0, size);
            Arrays.sort(pointsBySlope, p.slopeOrder());

            // Add the points with same slope to the candidate pool.
            int j = 1;
            while (j < pointsBySlope.length) {
                final double referenceSlope = p.slopeTo(pointsBySlope[j]);

                // Point "p" and the point producing the reference slope must
                // be the candidates.
                candidates.add(pointsBySlope[j]);

                while (++j < pointsBySlope.length &&
                       p.slopeTo(pointsBySlope[j]) == referenceSlope) {
                    candidates.add(pointsBySlope[j]);
                }

                // If the pool size is greater than 3 (excluding "p"), create
                // a line segment.
                if (candidates.size() > 3 &&
                    // "p" must be smaller than the first (relatively smaller)
                    // point in the candidate pool.
                    p.compareTo(candidates.getFirst()) < 0) {
                    final Point max = candidates.getLast();

                    mSegments.add(new LineSegment(p, max));
                }

                // Reset the candidate pool.
                candidates.clear();
            }
        }
    }
}
