package deque;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class ArrayDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        ArrayDeque<String> ad1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", ad1.isEmpty());
        ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, ad1.size());
        assertFalse("lld1 should now contain 1 item", ad1.isEmpty());


        ad1.addLast("middle");
        assertEquals(2, ad1.size());

        ad1.addLast("back");
        assertEquals(3, ad1.size());

        System.out.println("Printing out deque: ");
        ad1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that ad is empty afterwards. */
    /* personally changed it to include test for size variable when adding + deleting values */
    public void addRemoveTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", ad1.isEmpty());
        assertEquals(0, ad1.size());

        ad1.addFirst(10);
        assertEquals(1, ad1.size());
        // should not be empty
        assertFalse("lld1 should contain 1 item", ad1.isEmpty());

        ad1.removeFirst();
        // should be empty
        assertEquals(0, ad1.size());
        assertTrue("lld1 should be empty after removal", ad1.isEmpty());

        ad1.addLast(15);
        ad1.removeLast();
        assertEquals(0, ad1.size());
        assertTrue("didn't work homie", ad1.isEmpty());
    }

    @Test
    public void resizeOneSizeUpTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(1);
        ad1.addLast(2);
        ad1.addFirst(3);
        ad1.addLast(4);
        ad1.addFirst(5);
        ad1.addLast(6);
        ad1.addFirst(7);
        ad1.addLast(8);

        /*this next add should cause a resize */
        ad1.addLast(9);
        assertEquals(ad1.size(), 9);
        ad1.printDeque();
        System.out.println(ad1.get(0));
    }

    @Test
    public void resizeOneSizeDown() { /* passed*/
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i=0; i<=8; i+=1) {
            ad1.addLast(55);
        }
        assertEquals(9, ad1.size(), 0.0);
        for (int i=0; i<6; i+=1) {
            ad1.removeFirst();
        }
        assertEquals(3, ad1.size(), 0.0);
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(3);

        ad1.removeLast();
        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeFirst();

        int size = ad1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create ArrayDeques with different parameterized types*/
    public void multipleParamTest() {

        ArrayDeque<String>  ad1 = new ArrayDeque<String>();
        ArrayDeque<Double>  ad2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> ad3 = new ArrayDeque<Boolean>();

        ad1.addFirst("string");
        ad2.addFirst(3.14159);
        ad3.addFirst(true);

        String s = ad1.removeFirst();
        double d = ad2.removeFirst();
        boolean b = ad3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, ad1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, ad1.removeLast());
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i = 0; i < 1000000; i++) {
            ad1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) ad1.removeFirst(), 0.0);
        }
        /*for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) ad1.removeLast(), 0.0);
        }*/
        for (double i=999999; i>700000; i-=1) {
            double x=ad1.removeLast();
            assertEquals(i, x, 0.0);
        }

    }
    @Test
    /*tests equals method for a few diff param types */
    public void equalsTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addLast(15);
        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        ad2.addFirst(15);
        assertTrue("oh no!", ad1.equals(ad2));

        ArrayDeque<String> ad4 = new ArrayDeque<>();
        ad4.addLast("yurr");
        ArrayDeque<String> ad5 = new ArrayDeque<>();
        ad5.addFirst("you tell em");
        assertFalse("oh no! again", ad4.equals(ad5));
    }

    @Test
    /*tests both the regular method and recursive method at diff points*/
   public void getBigTrialTest() {
       ArrayDeque<Integer> ad1= new ArrayDeque<>();
       for (int i=0; i<100; i++) {
           ad1.addLast(i);
       }
       for (int i=0; i<100; i++) {
           int item= ad1.get(i);
           assertEquals(item, i, 0.0);
       }
    }

    @Test
    /* checking resizing some more - based off of b03 autograder test */
    public void fillUpEmptyFillUpAgain() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i=1; i<101; i++) {
            ad1.addFirst(i);
        }
        for (int i=100; i>0; i--) {
            int x = ad1.removeFirst();
            assertEquals(i, x, 0.0);
        }
        assertEquals(0, ad1.size(), 0.0);

        for (int i=1; i<101; i++) {
            ad1.addLast(i);
        }
        for (int i=100; i>0; i--) {
            int x=ad1.removeLast();
            assertEquals(i, x, 0.0);
        }
        assertEquals(0, ad1.size(), 0.0);
    }

    @Test
    public void figuringOutProblemWithContains() {
        ArrayDeque<Integer>  ad1 = new ArrayDeque<>();
        for (int i = 0; i < 5; i++) {
            ad1.addLast(i);
        }
        ArrayDeque<Integer>  ad2 = new ArrayDeque<>();
        for (int i = 0; i < 5; i++) {
            ad2.addLast(i);
        }
        boolean test = ad1.equals(ad2);
        assertTrue(test);

        LinkedListDeque<Integer>  lld1 = new LinkedListDeque<>();
        for (int i = 0; i < 5; i++) {
            lld1.addLast(i);
        }
        LinkedListDeque<Integer>  lld2 = new LinkedListDeque<>();
        for (int i = 0; i < 5; i++) {
            lld2.addLast(i);
        }
        assertTrue(lld1.equals(lld2));
        /* Testing between classes one*/
        assertTrue(lld1.equals(ad1));
    }
    @Test
    public void checkingEqualsUnmatchedDeques() {
        ArrayDeque<Integer>  ad1 = new ArrayDeque<>();
        for (int i = 0; i < 5; i++) {
            ad1.addLast(i);
        }
        ArrayDeque<Integer>  ad2 = new ArrayDeque<>();
        for (int i = 5; i > 0; i--) {
            ad2.addLast(i);
        }
        boolean test = ad1.equals(ad2);
        assertFalse(test);

        LinkedListDeque<Integer>  lld1 = new LinkedListDeque<>();
        for (int i = 0; i < 5; i++) {
            lld1.addLast(i);
        }
        LinkedListDeque<Integer>  lld2 = new LinkedListDeque<>();
        for (int i = 0; i < 5; i++) {
            lld2.addLast(i);
        }
        assertTrue(lld1.equals(lld2));
        /* Testing between classes one*/
        assertTrue(lld1.equals(ad1));
    }
}
