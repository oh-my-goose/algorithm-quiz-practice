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
        final int size = 4;
        final int[] check1 = new int[size];
        final int[] check2 = new int[size];
        final RandomizedQueue<Integer> d2_1 = new RandomizedQueue<>();
        final RandomizedQueue<Integer> d2_2 = new RandomizedQueue<>();

        // Enqueue
        for (int i = 0; i < size; ++i) {
            d2_1.enqueue(i);
            d2_2.enqueue(i);
        }

        // Two parallel sampling iterations.
        Iterator<Integer> it2_1 = d2_1.iterator();
        Iterator<Integer> it2_2 = d2_2.iterator();
        for (int i = 0; i < size; ++i) {
            int v1 = it2_1.next();
            ++check1[v1];

            int v2 = it2_2.next();
            ++check2[v2];
        }
        // Verify the result.
        for (int count : check1) {
            assert count == 1;
        }
        for (int count : check2) {
            assert count == 1;
        }
        System.out.println("Confirm the sampling didn't collide.");

        // Test 3: Iteration.
        System.out.println("=== Test3 ===");
        final RandomizedQueue<String> d3 = new RandomizedQueue<>();
        d3.enqueue("1");
        d3.enqueue("2");

        final Iterator<String> it3 = d3.iterator();
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", it3.next(), d3));
        System.out.println(String.format(
                Locale.ENGLISH, "e=%s, queue=%s", it3.next(), d3));
        assert !it3.hasNext();
    }
}
