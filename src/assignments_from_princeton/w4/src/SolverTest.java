import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import org.junit.Assert;

public class SolverTest {

    public static void main(String[] args) {
        SolverByOther solver;

        // "test/puzzle00.txt": 2 moves
        solver = new SolverByOther(new Board(loadPuzzleTiles("test/puzzle02.txt")));
        StdOut.println("test/puzzle02.txt: " + solver.moves());
        Assert.assertEquals(2, solver.moves());

        // "test/puzzle2x2-00.txt": 6
        solver = new SolverByOther(new Board(loadPuzzleTiles("test/puzzle2x2-06.txt")));
        StdOut.println("test/puzzle2x2-06.txt: " + solver.moves());
        Assert.assertEquals(6, solver.moves());

        // "test/puzzle2x2-unsolvable1.txt": not solvable
        solver = new SolverByOther(new Board(loadPuzzleTiles("test/puzzle2x2-unsolvable1.txt")));
        StdOut.println("test/puzzle2x2-unsolvable1.txt: " + solver.moves());
        Assert.assertEquals(-1, solver.moves());

        // "test/puzzle4x4-42.txt": not solvable
        solver = new SolverByOther(new Board(loadPuzzleTiles("test/puzzle22.txt")));
        StdOut.println("test/puzzle22.txt: " + solver.moves());
        Assert.assertEquals(22, solver.moves());

    }

    private static int[][] loadPuzzleTiles(String filename) {
        In in = new In(filename);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        return tiles;
    }
}
