import java.util.Iterator;

public class DequeTest {

    public static void main(String[] args) {
        // Test 1: double the capacity and shrink it.
        System.out.println("=== Test1 ===");
        final Deque<String> d1 = new Deque<>();
        assert d1.size() == 0;
        assert d1.isEmpty();
        d1.addFirst("3");
        d1.addFirst("2");
        d1.addFirst("1");
        d1.addLast("4");
        d1.addLast("5");
        d1.addLast("6");
        d1.addLast("7");
        d1.addLast("8");
        d1.addLast("9");
        d1.addLast("10");

        assert d1.size() == 10;
        assert d1.removeFirst().equalsIgnoreCase("1");
        assert d1.removeFirst().equalsIgnoreCase("2");
        assert d1.removeLast().equalsIgnoreCase("10");
        assert d1.removeFirst().equalsIgnoreCase("3");
        assert d1.removeLast().equalsIgnoreCase("9");
        assert d1.removeLast().equalsIgnoreCase("8");
        assert d1.removeLast().equalsIgnoreCase("7");
        assert d1.removeLast().equalsIgnoreCase("6");
        assert d1.size() == 2;

        // Test 2: Iteration.
        System.out.println("=== Test2 ===");
        final Deque<String> d2 = new Deque<>();
        d2.addFirst("3");
        d2.addFirst("2");
        d2.addLast("4");
        d2.addFirst("1");
        d2.addLast("5");
        d2.addLast("6");

        int count = 0;
        final Iterator<String> it2 = d2.iterator();
        while (it2.hasNext()) {
            final String s = it2.next();

            System.out.println(s);

            ++count;
        }
        assert count == 6;

        // Test 3: ...
        System.out.println("=== Test3 ===");
        final Deque<Integer> d3 = new Deque<>();
        d3.addFirst(2);
        d3.addFirst(1);

        final Iterator<Integer> it3 = d3.iterator();
        it3.next();
        it3.next();
    }
}
