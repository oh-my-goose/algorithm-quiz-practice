import javax.lang.model.element.VariableElement;
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
    private Object[] mItems;
    private int mHead;
    private int mTail;

    // For sampling.
    private RandomOrderIterator mIterator = new RandomOrderIterator();

    public RandomizedQueue() {
        mItems = new Object[DEFAULT_CAPACITY];
        mHead = mTail = 0;
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
                mIterator = new RandomOrderIterator();
                return mIterator.next();
            }
        }
    }

    public Iterator<Item> iterator() {
        return new RandomOrderIterator();
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

    public Iterator<Item> getQueueIterator() {
        return new QuequeIterator();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Clazz //////////////////////////////////////////////////////////////////

    private class RandomOrderIterator implements Iterator<Item> {

        int mCurrent;

        private RandomOrderIterator() {
            // EMPTY.
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
                    int n = size();
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

    private class QuequeIterator implements Iterator<Item> {

        int mCurrent;

        public QuequeIterator() {
            mCurrent = mHead;
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
                    int n = size();
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

