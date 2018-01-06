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

    private static final int DEFAULT_CAPACITY = 8;

    // Mutex.
    private final Object mMutex = new Object();

    // Resizing array.
    private volatile Object[] mItems;
    private volatile int mHead;
    private volatile int mTail;

    // For sampling.
    private Iterator<Item> mIterator;

    public RandomizedQueue() {
        mItems = new Object[DEFAULT_CAPACITY];
        mHead = mTail = 0;
        mIterator = iterator();
    }

    public boolean isEmpty() {
        synchronized (mMutex) {
            return mHead == mTail;
        }
    }

    public int size() {
        synchronized (mMutex) {
            return getSize();
        }
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null item.");
        }

        synchronized (mMutex) {
            // Overflow.
            if (--mHead < 0) {
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

            // Reset the sampling iterator.
            mIterator = iterator();
        }
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Cannot remove item from an empty deque.");
        }

        synchronized (mMutex) {
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
            int cursor = mHead + (int) Math.floor(size * Math.random());
            if (cursor >= mItems.length) cursor %= mItems.length;
            final Item chosen = (Item) mItems[cursor];

            // Step 2: Get the tail element.
            if (--mTail < 0) {
                mTail += mItems.length;
            }
            final Item tail = (Item) mItems[mTail];

            // Step 3: Exchange two and erase the tail.
            mItems[cursor] = tail;
            mItems[mTail] = null;

            // Recycle the memory.
            if (--size < mItems.length / 4) {
                shrinkCapacity(size);
            }

            // TODO: Probably update the iterator's cached tail so that the
            // TODO: uniform random distribution is guaranteed.
            // Reset the sample iterator.
            mIterator = iterator();

            return chosen;
        }
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Cannot remove item from an empty deque.");
        }

        synchronized (mMutex) {
            if (mIterator.hasNext()) {
                return mIterator.next();
            } else {
                mIterator = iterator();
                return mIterator.next();
            }
        }
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

        final Object[] a = new Object[newCapacity];

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
        final Object[] a = new Object[newCapacity];

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
        private final int mCachedTail;
        // For random access.
        private volatile int mCachedSize;

        private RandomizedIterator(int size) {
            mCachedSize = size;
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

            synchronized (mMutex) {
                int cursor = mHead + (int) Math.floor(mCachedSize * Math.random());
                if (cursor >= mItems.length) cursor %= mItems.length;
                final Item chosen = (Item) mItems[cursor];

                int cursorLast = mHead + (--mCachedSize);
                if (cursorLast >= mItems.length) cursorLast %= mItems.length;
                final Item last = (Item) mItems[cursorLast];

                mItems[cursor] = last;
                mItems[cursorLast] = chosen;

                return chosen;
            }
        }

        @Override
        public void remove() {
            if (checkIfOperationInvalid()) {
                throw new UnsupportedOperationException();
            } else if (checkIfConcurrentModification()) {
                throw new ConcurrentModificationException();
            }

            synchronized (mMutex) {
                // Adjust array.
                adjustElements();

                // Left-shift everything that is to the right of current.
                final int cursor = mHead + mCachedSize;
                final int rightToCursor = cursor + 1;
                System.arraycopy(
                        mItems, rightToCursor,
                        mItems, cursor, mItems.length - rightToCursor);
                mItems[mTail--] = null;
                mCachedSize = mTail;
            }
        }

        private void adjustElements() {
            if (mHead > mTail) {
                int n = getSize();
                final Object[] a = new Object[mItems.length];

                // For example:
                // a=[_,_,_,_, , ,_,_]
                //            t   h

                int rightN = mItems.length - mHead;
                System.arraycopy(mItems, mHead, a, 0, rightN);
                System.arraycopy(mItems, 0, a, rightN, mTail);

                mItems = a;

                mHead = 0;
                mTail = n;
            }
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
            return mCachedTail != mTail;
        }
    }

    /**
     * Iterator for iterating the element in LIFO order.
     * Note: This iterator cannot detect {@link ConcurrentModificationException}.
     */
    private class QueueIterator implements Iterator<Item> {

        int mCurrent;

        public QueueIterator(int head) {
            mCurrent = head;
        }

        @Override
        public boolean hasNext() {
            if (isEmpty()) return false;

            synchronized (mMutex) {
                int dist = mCurrent - mHead;
                if (dist < 0) {
                    dist += mItems.length;
                }

                return dist < getSize();
            }
        }

        @Override
        public Item next() {
            if (checkIfOperationInvalid()) {
                throw new NoSuchElementException();
            }

            synchronized (mMutex) {
                final Item item = (Item) mItems[mCurrent];

                if (++mCurrent >= mItems.length) {
                    mCurrent %= mItems.length;
                }

                return item;
            }
        }

        @Override
        public void remove() {
            if (checkIfOperationInvalid()) {
                throw new UnsupportedOperationException();
            }

            synchronized (mMutex) {
                // Adjust array.
                if (mHead > mTail) {
                    int n = getSize();
                    final Object[] a = new Object[mItems.length];

                    // For example:
                    // a=[_,_,_,_, , ,_,_]
                    //            t   h

                    int rightN = mItems.length - mHead;
                    System.arraycopy(mItems, mHead, a, 0, rightN);
                    System.arraycopy(mItems, 0, a, rightN, mTail);

                    mItems = a;

                    int offset = mCurrent - mHead;
                    if (offset < 0) offset += mItems.length;
                    mCurrent = offset;

                    mHead = 0;
                    mTail = n;
                }

                // Left-shift everything that is to the right of current.
                System.arraycopy(
                        mItems, mCurrent + 1,
                        mItems, mCurrent, mTail - mCurrent - 1);
                mItems[mTail--] = null;
            }
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

