package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>,Iterable<T>{

    private int size;
    private T[] array;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque(){
        size = 0;
        array = (T []) new Object[8];
        nextFirst = 3;
        nextLast = 4;
    }

    private class ArrayDequeIterator implements Iterator<T>{

        int ptr = nextFirst+1;

        @Override
        public boolean hasNext() {
            return ptr != nextLast;
        }

        @Override
        public T next() {
            T item = array[ptr];
            ptr++;
            return item;
        }
    }

    private void ReSize(int size){
        if(size <= array.length / 4){
            T[] NewArray = (T []) new Object[array.length/2];
            System.arraycopy(array,nextFirst+1,NewArray,nextFirst-array.length/4+1,size);
            nextFirst = nextFirst-array.length/4;
            nextLast = nextFirst+1+size;
            array = NewArray;
        }
        if(nextFirst == -1 || nextLast == array.length){
            T[] NewArray = (T []) new Object[array.length*2];
            System.arraycopy(array,nextFirst+1, NewArray,nextFirst+array.length/2 ,size);
            nextFirst = nextFirst + array.length/2 - 1;
            nextLast = nextFirst+size+1;
            array = NewArray;
        }
    }

    @Override
    public void addFirst(T item) {
        ReSize(size);
        array[nextFirst] = item;
        nextFirst--;
        size++;
    }

    @Override
    public void addLast(T item) {
        ReSize(size);
        array[nextLast] = item;
        nextLast++;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {

    }

    @Override
    public T removeFirst() {
        int first = nextFirst+1;
        T FirstItem = array[first];
        array[first] = null;
        nextFirst++;
        size--;
        ReSize(size);
        return FirstItem;
    }

    @Override
    public T removeLast() {
        int Last = nextLast-1;
        T LastItem = array[Last];
        array[Last] = null;
        nextLast--;
        size--;
        ReSize(size);
        return LastItem;
    }

    @Override
    public T get(int index) {
        return array[index+nextFirst+1];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
}
