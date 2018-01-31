import edu.princeton.cs.algs4.StdRandom;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Locale;

public class FastCollinearPointsTest {

    public static void main(String[] args) throws Exception {
        final Point[] sample10000 = SamplePoints.get10000Points();

        // Duplicate points test.
        Point[] pointsWithDuplicates = new Point[]{
            new Point(-1, -1),
            new Point(0, 2),
            new Point(2, 2),
            new Point(2, 0),
            new Point(-1, -1),
            new Point(2, 1)
        };
        try {
            new FastCollinearPoints(pointsWithDuplicates);
            throw new Exception("Should have thrown the exception");
        } catch (IllegalArgumentException error) {
            // DO NOTHING.
        }

        // Collinear N points.
        Point[] collinearNPoints = new Point[]{
            new Point(-5, -5),
            new Point(-4, -4),
            new Point(-3, -3),
            new Point(-2, -2),
            new Point(-1, -1),
            new Point(0, 0),
            new Point(1, 1),
            new Point(2, 2),
            new Point(3, 3),
            new Point(4, 4),
            new Point(5, 5),
            // Only one point not collinear with the others.
            new Point(1, 2)
            };
        StdRandom.shuffle(collinearNPoints);
        Assert.assertEquals(1, new FastCollinearPoints(collinearNPoints).numberOfSegments());
        Assert.assertEquals("(-5, -5) -> (5, 5)", new FastCollinearPoints(collinearNPoints).segments()[0].toString());

        // Collinear 10,000 points forming 35 line segments.
        long ts = System.currentTimeMillis();
//        System.out.println(String.format(
//            Locale.ENGLISH,
//            "number of line segments is %d",
//            new FastCollinearPointsOther(sample10000).numberOfSegments()));
        System.out.println(String.format(
            Locale.ENGLISH,
            "number of line segments is %d",
            new FastCollinearPoints(sample10000).numberOfSegments()));
        System.out.println(String.format(
            Locale.ENGLISH,
            "took %d ms",
            System.currentTimeMillis() - ts));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected / Private Methods ////////////////////////////////////////////

}
