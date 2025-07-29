package deque;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;



public class MaxArrayDequeTest {
    @Test
    public void maxTest0(){
        Comparator<Integer> cm = (a,b) -> a - b;
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(cm);
        mad.addFirst(1);
        mad.addFirst(5);
        mad.addFirst(2);
        mad.addFirst(6);
        int max = mad.max();
        assertEquals(6,max);
    }
    @Test
    public void maxTest1(){
        Comparator<Integer> cm = (a,b) -> a - b;
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(cm);
        mad.addFirst(1);
        mad.addFirst(5);
        mad.addFirst(2);
        mad.addFirst(2);
        int max = mad.max();
        assertEquals(5,max);
    }
    @Test
    public void maxTest2(){
        Comparator<Integer> cm = (a,b) -> a - b;
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(cm);
        Integer max = mad.max();
        assertNull(max);
    }
    @Test
    public void maxTest00(){
        Comparator<Integer> cm = (a,b) -> a - b;
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(cm);
        mad.addFirst(1);
        mad.addFirst(5);
        mad.addFirst(2);
        mad.addFirst(2);
        Integer max = mad.max(cm);
        assertEquals(5,(int)max);
    }
}
