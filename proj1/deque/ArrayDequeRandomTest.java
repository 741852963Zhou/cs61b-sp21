package deque;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Random;


public class ArrayDequeRandomTest {
    @Test
    public void Test(){

        deque.ArrayDeque<Integer> myDeque = new deque.ArrayDeque<>();
        ArrayDeque<Integer> deque = new ArrayDeque<>();

        int numOperations = 5000;
        Random random = new Random();

        for(int i = 0;i <= numOperations;i++){
            int operation = random.nextInt(4);
            if(operation==0){
                //addFirst
                int valueToAdd = random.nextInt(100);
                myDeque.addFirst(valueToAdd);
                deque.addFirst(valueToAdd);
                System.out.println("add"+valueToAdd);
            }
            if(operation==1){
                //addLast
                int valueToAdd = random.nextInt(100);
                myDeque.addLast(valueToAdd);
                deque.addLast(valueToAdd);
            }
            if(operation==2){
                //removeFirst
                Integer a = myDeque.removeFirst();
                Integer b = deque.removeFirst();
                assertEquals(a,b);
                System.out.println("removeFirst" + a);
            }
            if(operation==3){
                //removeLast
                Integer a = myDeque.removeLast();
                Integer b = deque.removeLast();
                assertEquals(a,b);
                System.out.println("removeLast" + a);
            }
            assertEquals(myDeque.size(),deque.size());
        }
    }
}
