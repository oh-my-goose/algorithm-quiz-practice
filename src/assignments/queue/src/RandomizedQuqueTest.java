import java.util.Iterator;
import java.util.Locale;

public class RandomizedQuqueTest {

    public static void main(String[] args) {
        // Test 1: double the capacity and shrink it.
        final RandomizedQueue<String> d1 = new RandomizedQueue<>();
        assert d1.size() == 0;
        assert d1.isEmpty();
        d1.enqueue("1");
        d1.enqueue("2");
        d1.enqueue("3");
        d1.enqueue("4");
        d1.enqueue("5");
        d1.enqueue("6");
        d1.enqueue("7");
        d1.enqueue("8");
        d1.enqueue("9");
        d1.enqueue("10");

        // Show the content.
        System.out.println(String.format(
                Locale.ENGLISH, "queue=%s", d1));

        assert d1.size() == 10;
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", d1.dequeue(), d1));
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", d1.dequeue(), d1));
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", d1.dequeue(), d1));
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", d1.dequeue(), d1));
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", d1.dequeue(), d1));
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", d1.dequeue(), d1));
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", d1.dequeue(), d1));
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", d1.dequeue(), d1));
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", d1.dequeue(), d1));
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", d1.dequeue(), d1));
        assert d1.size() == 0;

        // Test 2: Sampling.


        // Test 2: Iteration.
//        final Deque<String> d2 = new Deque<>();
//        d2.addFirst("3");
//        d2.addFirst("2");
//        d2.addLast("4");
//        d2.addFirst("1");
//        d2.addLast("5");
//        d2.addLast("6");
//
//        int count = 0;
//        final Iterator<String> it2 = d2.iterator();
//        while (it2.hasNext()) {
//            final String s = it2.next();
//
//            System.out.println(s);
//
//            ++count;
//        }
//        assert count == 6;
    }
}
