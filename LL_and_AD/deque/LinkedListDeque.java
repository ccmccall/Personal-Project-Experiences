package deque;
import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private TypeNode sentinel;
    private class TypeNode {
        private T item;
        private TypeNode next;
        private TypeNode previous;

        TypeNode(TypeNode p, T i, TypeNode n) {
            previous = p;
            item = i;
            next = n;
        }
    }
    public LinkedListDeque() {
        sentinel = new TypeNode(null, null, null);
        sentinel.previous = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }
    @Override
    public void addFirst(T x) {
        size += 1;
        if (sentinel.next == null | sentinel.next == sentinel) {
            TypeNode newFirstNode = new TypeNode(sentinel, x, sentinel);
            sentinel.previous = newFirstNode;
            sentinel.next = newFirstNode;
        } else {
            TypeNode origFirstNode = sentinel.next;
            TypeNode newFirstNode = new TypeNode(sentinel, x, origFirstNode);
            origFirstNode.previous = newFirstNode;
            sentinel.next = newFirstNode;
        }
    }
    @Override
    public T removeFirst() {
        if (sentinel.next == sentinel | sentinel.next == null) {
            return null;
        } else {
            TypeNode firstNode = sentinel.next;
            TypeNode secondNode = sentinel.next.next;
            secondNode.previous = sentinel;
            sentinel.next = secondNode;
            size -= 1;
            return firstNode.item;
        }
    }
    @Override
    public T removeLast() {
        if (sentinel.previous == sentinel | sentinel.previous == null) {
            return null;
        } else {
            TypeNode lastNode = sentinel.previous;
            TypeNode secondToLast = sentinel.previous.previous;
            sentinel.previous = secondToLast;
            secondToLast.next = sentinel;
            size -= 1;
            return lastNode.item;
        }
    }
    @Override
    public void addLast(T x) {
        size += 1;
        if (sentinel.previous == null | sentinel.previous == sentinel) {
            TypeNode newLastNode = new TypeNode(sentinel, x, sentinel);
            sentinel.previous = newLastNode;
            sentinel.next = newLastNode;
        } else {
            TypeNode origLastNode = sentinel.previous;
            TypeNode newLastNode = new TypeNode(origLastNode, x, sentinel);
            sentinel.previous = newLastNode;
            origLastNode.next = newLastNode;
        }
    }
    @Override
    public T get(int i) {
        if (i > size() - 1) {
            return null;
        }
        TypeNode p = sentinel.next;
        int counter = 0;
        while (counter != i) {
            p = p.next;
            counter += 1;
        }
        return p.item;
    }
    public T getRecursive(int i) {
        if (sentinel.next == null) {
            return null;
        }
        TypeNode p = sentinel.next;
        return getRecursive(p, i);
    }

    private T getRecursive(TypeNode p, int i) {
        if (i == 0) {
            return p.item;
        }
        return getRecursive(p.next, i - 1);
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public void printDeque() {
        TypeNode p = sentinel.next;
        while (p.item != null) {
            System.out.print(p.item);
            System.out.print(" ");
            p = p.next;
        }
        System.out.println();
    }

    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }
    private class LinkedListIterator implements Iterator<T> {
        private TypeNode p;
        LinkedListIterator() {
            p = sentinel.next;
        }
        public boolean hasNext() {
            /* don't have to check if p.item is null*/
            /* have a null check for it*/
            /*don't really have base case for if there's no more values*/
            /*think might have fixed it */
            return (p.item != null && p.item != sentinel);
        }
        public T next() {
            T returnItem = p.item;
            p = p.next;
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
