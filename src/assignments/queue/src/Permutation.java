import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

public class Permutation {

    public static void main(String[] args) {
        if (args.length == 0 ||
            args[0].equalsIgnoreCase("-h") ||
            args[0].equalsIgnoreCase("--help")) {
            System.out.print("Permutation 3 < duplicates.txt");
        }

        final int printSize = Integer.parseInt(args[0]);

        final RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (StdIn.isEmpty()) {
            final String s = StdIn.readString();
            if (s.equalsIgnoreCase("eof")) {
                break;
            } else {
                queue.enqueue(s);
            }
        }

        int i = 0;
        final Iterator<String> it = queue.iterator();
        while (it.hasNext() && i++ < printSize) {
            System.out.println(it.next());
        }
    }
}

