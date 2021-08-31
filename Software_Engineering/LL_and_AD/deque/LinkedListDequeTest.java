package deque;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        LinkedListDeque<String> lld1 = new LinkedListDeque<>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());


		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();
    }

    @Test
    public void printDequeTester() {
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        L.addLast(0);
        L.addLast(1);
        L.addFirst(2);
        L.addLast(3);
        L.addLast(4);
        L.addFirst(5);
        L.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    /* personally changed it to include test for size variable when adding + deleting values */
    public void addRemoveTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());
		assertEquals(0, lld1.size());

		lld1.addFirst(10);
        assertEquals(1, lld1.size());
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
        assertEquals(0, lld1.size());
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());

		lld1.addLast(15);
		lld1.removeLast();
		assertEquals(0, lld1.size());
		assertTrue("didn't work homie", lld1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }

    }

    /*tests equals method for a few diff param types */
    /*public void equalsTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(15);
        LinkedListDeque<Integer> lld2 = new LinkedListDeque<>();
        lld2.addFirst(15);
        assertTrue("oh no!", lld1.equals(lld2));

        LinkedListDeque<String> lld4 = new LinkedListDeque<>();
        lld4.addLast("yurr");
        LinkedListDeque<String> lld5 = new LinkedListDeque<>();
        lld5.addFirst("you tell em");
        assertTrue("oh no! again", lld1.equals(lld2));
    } */

    @Test
    /*tests both the regular method and recursive method at diff points*/
    public void getRegularRecursiveTest() {
        LinkedListDeque<Integer> lld1= new LinkedListDeque<>();
        assertEquals(null, lld1.get(0));

        lld1.addLast(15);
        lld1.addFirst(23);
        assertEquals(lld1.get(1), 15, 0.0);
        assertEquals(null, lld1.get(20));

        /*now testing getRecursive */
        LinkedListDeque<Integer> lld2 = new LinkedListDeque<>();
        assertEquals(null, lld2.getRecursive(0));

        lld2.addLast(15);
        lld2.addFirst(23);
        assertEquals(15, lld2.getRecursive(1), 0.0);
        assertEquals(null,  lld2.getRecursive(20));
    }
    @Test
    /*getRecursive and get comparison test over randomized trial testing */
    public void getAndGetRecursiveLargeTrialTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        LinkedListDeque<Integer> lld2 = new LinkedListDeque<>();
        for (int i=0; i<100; i++) {
            lld1.addLast(i);
            lld2.addLast(i);
        }
        for (int i=0; i<100; i++) {
            int d1= lld1.get(i);
            int d2 = lld2.getRecursive(i);
            assertEquals(d1, d2, 0.0);
        }
    }
}
