import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    // Given.
    private final Point[] mGivenPoints;

    private boolean mIsDirty;
    private final List<LineSegment> mSegments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {
        if (points == null || points.length == 0) {
            throw new IllegalArgumentException();
        } else if (hasDuplicatedEntries(points)) {
            throw new IllegalArgumentException();
        }

        mGivenPoints = points;
        mIsDirty = true;
    }

    public int numberOfSegments() {
        if (mIsDirty) {
            findSegments();
            mIsDirty = false;
        }
        return mSegments.size();
    }

    public LineSegment[] segments() {
        if (mIsDirty) {
            findSegments();
            mIsDirty = false;
        }

        final LineSegment[] out = new LineSegment[mSegments.size()];
        mSegments.toArray(out);

        return out;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected / Private Methods ////////////////////////////////////////////

    boolean hasDuplicatedEntries(Point[] points) {
        final Point[] copy = new Point[points.length];
        System.arraycopy(points, 0, copy, 0, points.length);
        Arrays.sort(copy);

        // For example:
        // 1 2 3 3 4 5 6 6
        //     ^ ^     ^ ^
        // These are duplicates.

        Point prev = copy[0];
        for (int i = 1; i < copy.length; ++i) {
            if (copy[i].compareTo(prev) == 0) {
                return true;
            }

            prev = copy[i];
        }

        return false;
    }

    void findSegments() {

        // For example:
        // Given p, q ,r ,s

        // #1, p - q
        // #2, p - r
        // #3, p - s
        // #4, q - r
        // #5, q - s
        // #6, r - s
        // Total number of iterations is 4 + 3 + 2 + 1 = 10

        for (int p = 0; p < mGivenPoints.length; ++p) {
            for (int q = p + 1; q < mGivenPoints.length; ++q) {
                for (int r = q + 1; r < mGivenPoints.length; ++r) {
                    for (int s = r + 1; s < mGivenPoints.length; ++s) {
                        Point pp = mGivenPoints[p];
                        Point qq = mGivenPoints[q];
                        Point rr = mGivenPoints[r];
                        Point ss = mGivenPoints[s];

                        double slopeP2Q = pp.slopeTo(qq);
                        double slopeQ2R = qq.slopeTo(rr);
                        double slopeR2S = rr.slopeTo(ss);

                        if (slopeP2Q == slopeQ2R &&
                            slopeQ2R == slopeR2S) {
                            mSegments.add(new LineSegment(pp, ss));
                        }
                    }
                }
            }
        }
    }
}
