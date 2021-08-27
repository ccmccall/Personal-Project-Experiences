package gitlet;

import java.util.NoSuchElementException;

public class Queue<String> {
    private Node<String> first;    // beginning of queue
    private Node<String> last;     // end of queue
    private int n;

    private static class Node<String> {
        private String item;
        private Node<String> next;
    }

    public Queue() {
        first = null;
        last  = null;
        n = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public void enqueue(String value) {
        Node<String> oldlast = last;
        last = new Node<String>();
        last.item = value;
        last.next = null;
        if (isEmpty()) {
            first = last;
        } else {
            oldlast.next = last;
        }
        n++;
    }

    public String dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        String item = first.item;
        first = first.next;
        n--;
        if (isEmpty()) {
            last = null;   // to avoid loitering
        }
        return item;
    }
}
