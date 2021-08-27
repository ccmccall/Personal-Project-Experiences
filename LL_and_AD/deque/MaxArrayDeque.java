package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> storedComparator;
    public MaxArrayDeque(Comparator<T> c) {  /*constructor */
        storedComparator = c;
    }
    public T max() {
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            int cmpVar = storedComparator.compare(get(i), get(maxIndex));
            if (cmpVar > 0) {
                maxIndex = i;
            }
        }
        return get(maxIndex);
    }
    public T max(Comparator<T> c) {
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            int cmpVar = c.compare(get(i), get(maxIndex));
            if (cmpVar > 0) {
                maxIndex = i;
            }
        }
        return get(maxIndex);
    }
}

/* Store the previous comparator
/* maxArrayDeque is an expanded version of an ArrayDeque, that has everythings in arrayDeque but
can return the max item in the ArrayDeque instance
You would decide the way the items are compared based off of the Comparator type input
**Each comparator has method compare(item1, item2)
 */
