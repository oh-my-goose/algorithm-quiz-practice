public interface ISolver {

    boolean isSolvable();
    int moves();
    Iterable<Board> solution();
}
