package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
      AListNoResizing<Integer> NoBugItem = new AListNoResizing<Integer>();
      BuggyAList<Integer> BugItem = new BuggyAList<Integer>();
      for(int i = 4;i <= 6; i++){
        NoBugItem.addLast(i);
        BugItem.addLast(i);
      }
      for(int j = 0;j <= 2; j++){
        assertEquals(NoBugItem.removeLast(),BugItem.removeLast());
      }
    }
    @Test
    public void randomizedTest(){
      AListNoResizing<Integer> L = new AListNoResizing<>();
      BuggyAList<Integer> Buggy = new BuggyAList<>();

      int N = 500;
      for (int i = 0; i < N; i += 1) {
        int operationNumber = StdRandom.uniform(0, 4);
        if (operationNumber == 0) {
          // addLast
          int randVal = StdRandom.uniform(0, 100);
          L.addLast(randVal);
          Buggy.addLast(randVal);
          System.out.println("addLast(" + randVal + ")");
          assertEquals(L.getLast(),Buggy.getLast());
        } else if (operationNumber == 1) {
          // size
          int size = L.size();
          System.out.println("size: " + size);

          int Buggysize = Buggy.size();
          System.out.println("size: " + Buggysize);
          assertEquals(size,Buggysize);
        } else if (operationNumber == 2 && L.size() != 0){
          int Last = L.getLast();
          System.out.println("getLast(" + Last + ")");

          int BuggyLast = Buggy.getLast();
          System.out.println("getLast(" + BuggyLast + ")");
          assertEquals(Last,BuggyLast);
        }
        else if (operationNumber == 3 && L.size() != 0){
          int removedLast = L.removeLast();
          System.out.println("removeLast(" + removedLast + ")");
          int BuggyremovedeLast = Buggy.removeLast();
          System.out.println("removeLast(" + BuggyremovedeLast +")");
          assertEquals(removedLast,BuggyremovedeLast);
        }
      }
    }
}
