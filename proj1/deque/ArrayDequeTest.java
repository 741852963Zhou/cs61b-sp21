package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void addFirstTest(){
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(1);
        a.addFirst(2);
        a.addFirst(3);
        assertEquals(3,(int)a.get(0));
        assertEquals(2,(int)a.get(1));
        assertEquals(1,(int)a.get(2));
    }
    @Test
    public void addLastTest(){
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addLast(1);
        a.addLast(2);
        a.addLast(3);
        assertEquals(1,(int)a.get(0));
        assertEquals(2,(int)a.get(1));
        assertEquals(3,(int)a.get(2));
    }
    @Test
    public void isEmptyTest(){
        ArrayDeque<Integer> a = new ArrayDeque<>();
        assertTrue(a.isEmpty());
        a.addLast(1);
        assertFalse(a.isEmpty());
        a.removeFirst();
        assertTrue(a.isEmpty());
        a.addFirst(1);
        assertFalse(a.isEmpty());
        a.removeLast();
        assertTrue(a.isEmpty());
    }
    @Test
    public void sizeTest(){
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(1);
        a.addFirst(2);
        a.addFirst(3);
        assertEquals(3,a.size());
    }
    @Test
    public void RemoveFirstTest(){
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(1);
        a.addFirst(2);
        a.addFirst(3);
        assertEquals(3,a.size());
        a.removeFirst();
        assertEquals(2,a.size());
        assertEquals(2,(int)a.get(0));
    }
    @Test
    public void RemoveLastTest(){
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(1);
        a.addFirst(2);
        a.addFirst(3);
        assertEquals(3,a.size());
        a.removeLast();
        assertEquals(2,a.size());
        assertEquals(2,(int) a.get(a.size()-1));
    }
    @Test
    public void RandomizedTest(){
        int Numoperations= 5000;

    }
}
