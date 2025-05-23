package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimeSpecial() {
        IntList lst = IntList.of(2, 1, 3, 5, 7, 11, 13, 17, 19);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 1 -> 9 -> 25 -> 49 -> 121 -> 169 -> 289 -> 361", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimeZero(){
        IntList lst = IntList.of(0, 0, 0, 0);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("0 -> 0 -> 0 -> 0",lst.toString());
        assertFalse(changed);
    }
}
