import java.util.Scanner;

public class Permutation {

    public static void main(String[] args) {
        final Deque<String> deque = new Deque<>();

        System.out.println("Enter string to push to the Deque;" +
                           " - to pop from the Deque;" +
                           " eof to stop.");

        final Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            final String s = in.next();
            if (s.equalsIgnoreCase("eof")) {
                break;
            } else if (s.equalsIgnoreCase("-")) {
                deque.removeFirst();
            } else {
                deque.addFirst(s);
            }
            System.out.println(deque.toString());
        }
    }
}

