import org.junit.Assert;

import java.util.Locale;

public class BruteCollinearPointsTest {

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
            new BruteCollinearPoints(pointsWithDuplicates);
            throw new Exception("Should have thrown the exception");
        } catch (IllegalArgumentException error) {
            // DO NOTHING.
        }

        // Collinear four points.
        Point[] collinear4Points = new Point[]{
            new Point(0, 0),
            new Point(1, 1),
            new Point(2, 2),
            new Point(3, 3),
            };
        Assert.assertEquals(1, new BruteCollinearPoints(collinear4Points).numberOfSegments());
        Assert.assertEquals("(0, 0) -> (3, 3)", new BruteCollinearPoints(collinear4Points).segments()[0].toString());

        // Collinear 10,000 points forming 35 line segments.
        long ts = System.currentTimeMillis();
        System.out.println(String.format(
            Locale.ENGLISH,
            "number of line segments is %d",
            new BruteCollinearPoints(sample10000).numberOfSegments()));
        System.out.println(String.format(
            Locale.ENGLISH,
            "took %d ms",
            System.currentTimeMillis() - ts));
    }
}
