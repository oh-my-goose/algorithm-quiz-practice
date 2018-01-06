import java.util.Iterator;
import java.util.Locale;

public class RandomizedQuqueTest {

    public static void main(String[] args) {
        System.out.println("=== Test1 ===");
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
        System.out.println("=== Test2 ===");
        final int size = 65;
        final int[] check = new int[size];
        final RandomizedQueue<Integer> d2 = new RandomizedQueue<>();

        // Enqueue
        for (int i = 0; i < size; ++i) {
            d2.enqueue(i);
        }

        // Sample.
        for (int i = 0; i < size; ++i) {
            int j = d2.sample();
            ++check[j];
        }

        // Verify the result.
        for (int count : check) {
            assert count == 1;
        }
        System.out.println("Confirm the sampling didn't collide.");

        // Test 3: Iteration.
        System.out.println("=== Test2 ===");
        final RandomizedQueue<String> d3 = new RandomizedQueue<>();
        d3.enqueue("3");
        d3.enqueue("2");
        d3.enqueue("1");

        final Iterator<String> it3 = d3.iterator();
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", it3.next(), d3));
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", it3.next(), d3));
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", it3.next(), d3));
        assert !it3.hasNext();
    }
}
