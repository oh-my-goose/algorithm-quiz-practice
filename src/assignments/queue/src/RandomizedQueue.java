import edu.princeton.cs.algs4.StdRandom;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Use a circular resizing array to hold the data, for example:
 * <pre>
 * Given initial capacity: [ , , , , , , , ]
 *                          h (head)
 *                          t (tail)
 * enqueue =>  a=[ , , ,_]
 *                t     h
 * enqueue =>  a=[ , ,_,_]
 *                t   h
 * enqueue =>  a=[ ,_,_,_]
 *                t h
 * enqueue =>  a=[_,_,_,_, , , , ]
 *                h       t
 * enqueue =>  a=[_,_,_,_, , , ,_]
 *                        t     h
 * dequeue =>  a=[_,_,_, , , , ,_]
 *                      t       h
 * dequeue =>  a=[_,_, , , , , ,_]
 *                    t         h
 * dequeue =>  a=[_, , , , , , ,_]
 *                  t           h
 * dequeue =>  a=[_, , , ]
 *                h t
 * </pre>
 * The iteration of enqueue when worst case:
 * N + (2 + 4 + 8 + 16 + ... + M)
 * |<--   log(N) times   -->|
 * <p>
 * where:
 * 2 + 4 + 8 + ... + M
 * = 2 * (1 + 2 + 4 + ... + M/2)
 * = 2 * (2^M - 1)
 * <p>
 * So:
 * N + (2 + 4 + 8 + 16 + ... + M)
 * = N + 2*(2^log(N) - 1)
 * = 3N - 2
 * ~= 3N
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    // Resizing array.
    private Item[] mItems;
    private int mHead;
    private int mTail;

    public RandomizedQueue() {
        mItems = (Item[]) new Object[1 << 3];
        mHead = 0;
        mTail = 0;
    }

    public boolean isEmpty() {
        return mHead == mTail;
    }

    public int size() {
        return getSize();
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null item.");
        }

        // Decrease head.
        --mHead;
        // Overflow.
        if (mHead < 0) {
            // In ArrayDeque implementation, it is written in one line as:
            //
            //   head = (head - 1) & (elements.length - 1)
            //
            // It works because the elements.length is the powers of TWO!
            // elements.length - 1 is sort of a complement operation.
            // This value could be used to get the positive remainder of the
            // HEAD.
            mHead += mItems.length;
        }
        mItems[mHead] = item;

        // Ensure the pool is large enough.
        if (mHead == mTail) {
            doubleCapacity();
        }
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Cannot remove item from an empty deque.");
        }

        // Example:
        // a=[ , , ,_,_,_,_,_]
        //    t     h
        //
        // Random choose one element:
        // a=[ , , ,_,_,c,_,_]
        //    t     h
        //
        // Switch with the tail element with the random chosen one:
        // a=[ , , ,_,_,_,_,c]
        //    t     h
        //
        // Return the chosen one and update tail cursor.
        // a=[ , , ,_,_,_,_, ]
        //          h       t

        // Step 1: Randomly choose an element.
        int size = getSize();
        int cursor = mHead + StdRandom.uniform(size);
        if (cursor >= mItems.length) cursor %= mItems.length;
        final Item chosen = mItems[cursor];

        // Step 2: Get the tail element.
        --mTail;
        if (mTail < 0) {
            mTail += mItems.length;
        }
        final Item tail = mItems[mTail];

        // Step 3: Exchange two and erase the tail.
        mItems[cursor] = tail;
        mItems[mTail] = null;

        // Decrease size.
        --size;

        // Recycle the memory
        if (size < mItems.length / 4) {
            shrinkCapacity(size);
        }

        return chosen;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Cannot remove item from an empty deque.");
        }

        final int size = getSize();
        int cursor = mHead + StdRandom.uniform(size);
        if (cursor >= mItems.length) {
            cursor %= mItems.length;
        }

        return mItems[cursor];
    }

    public Iterator<Item> iterator() {
        return new RandomizedIterator(getSize());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        Iterator<Item> it = getQueueIterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                builder.append(it.next().toString());
                builder.append(", ");
            }
        } else {
            builder.append("empty");
        }

        return builder.toString();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected / Private Methods ////////////////////////////////////////////

    private void doubleCapacity() {
        // Make it twice larger.
        final int size = mItems.length;
        final int newCapacity = mItems.length << 1;
        // Check overflow.
        if (newCapacity < 0) {
            throw new IllegalStateException("Sorry, the deque is too large.");
        }

        final Item[] a = (Item[]) new Object[newCapacity];

        // For example:
        // a=[_,_,_,_, , ,_,_]
        //            t   h
        //
        // or
        //
        // a=[ , ,_,_,_,_,_,_]
        //    t   h
        //
        // ...
        //
        // to:
        // a=[_,_,_,_,_,_, , ]
        //    h           t

        int rightN = mItems.length - mHead;
        System.arraycopy(mItems, mHead, a, 0, rightN);
        System.arraycopy(mItems, 0, a, rightN, mHead);

        mItems = a;
        mHead = 0;
        mTail = size;
    }

    private void shrinkCapacity(final int size) {
        // For example:
        // a=[_,_, , , , , , , , , , , , , ,_]
        //        t                         h
        //
        // or
        //
        // a=[ , ,_,_,_, , , , , , , , , , , ]
        //        h     t
        //
        // To:
        // a=[_,_,_, , , , , ]
        //    h     t

        final int capacity = mItems.length;
        final int newCapacity = capacity >> 1;
        final Item[] a = (Item[]) new Object[newCapacity];

        for (int i = mHead, j = 0; i < mHead + size; ++i, ++j) {
            int k = i >= mItems.length ? i % mItems.length : i;
            a[j] = mItems[k];
        }

        mItems = a;
        mHead = 0;
        mTail = size;
    }

    private int getSize() {
        int diff = mTail - mHead;
        if (diff > 0) {
            return diff;
        } else if (diff < 0) {
            return mItems.length + diff;
        } else {
            return 0;
        }
    }

    private Iterator<Item> getQueueIterator() {
        return new QueueIterator(mHead);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Clazz //////////////////////////////////////////////////////////////////

    /**
     * For iterating the elements in a random order.
     */
    private class RandomizedIterator implements Iterator<Item> {

        // For detecting concurrent modification, where if the cached tail is
        // not equal to the tail, there must be a ConcurrentModificationException.
        private final int mCachedHead;
        private final int mCachedTail;
        // For random access.
        private int mCachedSize;
        private int[] mIndices;

        private RandomizedIterator(int size) {
            mCachedSize = size;

            mIndices = new int[size];
            for (int i = 0; i < size; ++i) {
                mIndices[i] = i;
            }

            mCachedHead = mHead;
            mCachedTail = mTail;
        }

        @Override
        public boolean hasNext() {
            if (checkIfConcurrentModification()) {
                throw new ConcurrentModificationException();
            }

            return mCachedSize > 0;
        }

        @Override
        public Item next() {
            if (checkIfOperationInvalid()) {
                throw new NoSuchElementException();
            } else if (checkIfConcurrentModification()) {
                throw new ConcurrentModificationException();
            }

            final int r = StdRandom.uniform(mCachedSize);
            int cursor = mHead + mIndices[r];
            if (cursor >= mItems.length) cursor %= mItems.length;
            final Item chosen = mItems[cursor];

            // Decrease size.
            --mCachedSize;

            // Exchange the chosen one and the last.
            mIndices[r] = mIndices[mCachedSize];
            mIndices[mCachedSize] = -1;

            return chosen;
        }

        @Override
        public void remove() {
            // The requirement of the assignment.
            throw new UnsupportedOperationException();
        }

        private boolean checkIfOperationInvalid() {
            if (isEmpty()) return true;

            // Valid example:
            // a=[ , ,_,_]
            //    t   h
            //
            // Invalid example:
            // a=[ , ,_,_]
            //        h
            //        t

            return mCachedSize <= 0;
        }

        private boolean checkIfConcurrentModification() {
            return mCachedHead != mHead ||
                   mCachedTail != mTail;
        }
    }

    /**
     * Iterator for iterating the element in LIFO order.
     * Note: This iterator cannot detect {@link ConcurrentModificationException}.
     */
    private class QueueIterator implements Iterator<Item> {

        int mCurrent;

        QueueIterator(int head) {
            mCurrent = head;
        }

        @Override
        public boolean hasNext() {
            if (isEmpty()) return false;

            int dist = mCurrent - mHead;
            if (dist < 0) {
                dist += mItems.length;
            }

            return dist < getSize();
        }

        @Override
        public Item next() {
            if (checkIfOperationInvalid()) {
                throw new NoSuchElementException();
            }

            final Item item = mItems[mCurrent++];

            if (mCurrent >= mItems.length) {
                mCurrent %= mItems.length;
            }

            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private boolean checkIfOperationInvalid() {
            if (isEmpty()) return true;

            // For example:
            // a=[_,_,_,_, , ,_,_]
            //            t   h

            if (mHead < mTail) {
                return mCurrent < mHead || mCurrent >= mTail;
            } else {
                return mCurrent >= mTail && mCurrent < mHead;
            }
        }
    }
}

