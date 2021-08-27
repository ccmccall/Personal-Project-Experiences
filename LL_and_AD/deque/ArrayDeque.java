package deque;
import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private T[] items;
    private int nextLast;
    private int nextFirst;
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        /*consider starting front is pos 4, starting end is pos 5 */
        nextFirst = 4;
        nextLast = 5;
    }
    @Override
    public void addFirst(T i) {
        if (size == items.length) {
            resize(size * 2);
        }
        if (items[nextFirst] == null) {
            size += 1;
            items[nextFirst] = i;
            nextFirst -= 1;
            if (nextFirst < 0) {
                nextFirst = items.length - 1;
            }
        }
    }
    @Override
    public void addLast(T i) {
        if (size == items.length) { /*items is equal to a private array */
            resize(size * 2);
        }
        if (items[nextLast] == null) {
            size += 1;
            items[nextLast] = i;
            nextLast += 1;
            if (nextLast >= items.length) {
                nextLast -= items.length;
            }
        }
    }
    @Override
    public T removeLast() {
        if (get(size - 1) == null) {
            return null;
        }
        if (items.length > 8) {
            if ((size - 1) < (items.length / 4)) {
                resize(items.length / 2);
            }
        }
        T x = get(size - 1);
        if (nextLast == 0) {
            nextLast += items.length;
        }
        items[nextLast - 1] = null;
        size -= 1;
        nextLast -= 1;
        if (nextLast < 0) {
            nextLast = items.length - 1;
        }
        return x;
    }
    @Override
    public T removeFirst() {
        if (get(0) == null) {
            return null;
        }
        if (items.length > 8) {
            if ((size - 1) < (items.length / 4)) {
                resize(items.length / 2);
            }
        }
        T x = get(0);
        if (nextFirst + 1 >= items.length) {
            nextFirst -= items.length;
        }
        items[nextFirst + 1] = null;
        size -= 1;
        nextFirst += 1;
        if (nextFirst >= items.length) {
            nextFirst -= items.length;
        }
        return x;
    }
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int newNextLast = 5;
        for (int i = 0; i < size(); i += 1) {
            if (newNextLast >= a.length) {
                newNextLast -= a.length;
            }
            a[newNextLast] = get(i);
            newNextLast += 1;
        }
        items = a;
        nextFirst = 4;
        nextLast = newNextLast;
    }
    @Override
    public T get(int i) {
        int indexOfItem = nextFirst + 1 + i;
        if (indexOfItem >= items.length) {
            indexOfItem -= items.length;
        }
        if (items[indexOfItem] == null) {
            return null;
        }
        return items[indexOfItem];
    }
    @Override
    public void printDeque() {
        for (int count = 1; count <= size; count += 1) {
            int indexOfItem = nextFirst + count;
            if ((indexOfItem) >= items.length) {
                indexOfItem -= items.length;
            }
            System.out.print(items[indexOfItem]);
            System.out.print(" ");
        }
        System.out.println();
    }
    @Override
    public int size() {
        return size;
    }
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    private class ArrayDequeIterator implements Iterator<T> {
        private int positionIndex;
        ArrayDequeIterator() {
            positionIndex = 0;
        }
        public boolean hasNext() {
            return positionIndex < size;
        }
        public T next() {
            T returnItem = get(positionIndex);
            positionIndex += 1;
            return returnItem;
        }
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof Deque)) {
            return false;
        }
        Deque<T> o = (Deque<T>) other;
        if (o.size() != this.size()) {
            return false;
        }
        int count = 0;
        for (T item : this) {
            if (!item.equals(o.get(count))) {
                return false;
            }
            count += 1;
        }
        return true;
    }
}
