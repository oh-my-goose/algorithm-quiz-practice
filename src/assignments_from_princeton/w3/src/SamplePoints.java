import edu.princeton.cs.algs4.In;

import java.util.Locale;

public class SamplePoints {

    public static Point[] get10000Points() {
        System.out.println("Loading sample points...");

        // read the n points from a file
        In in = new In("input10000.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        System.out.println(
            String.format(Locale.ENGLISH,
                          "Found %d sample points",
                          points.length));

        return points;
    }
}
