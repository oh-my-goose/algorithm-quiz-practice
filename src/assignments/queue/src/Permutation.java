import java.util.Iterator;
import java.util.Scanner;

public class Permutation {

    public static void main(String[] args) {
        if (args.length == 0 ||
            args[0].equalsIgnoreCase("-h") ||
            args[0].equalsIgnoreCase("--help")) {
            System.out.print("Permutation 3 < duplicates.txt");
        }

        final RandomizedQueue<String> queue = new RandomizedQueue<>();
        final Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            final String s = in.next();
            if (s.equalsIgnoreCase("eof")) {
                break;
            } else {
                queue.enqueue(s);
            }
        }

        final Iterator<String> it = queue.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}

