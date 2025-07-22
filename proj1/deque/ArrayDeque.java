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
        if(size == array.length){
            T[] NewArray = (T []) new Object[array.length*2];
            T[] copyArray = (T []) new Object[array.length];
            int first = (nextFirst+1)%array.length;
            System.arraycopy(array,first,copyArray,0,array.length-first);
            System.arraycopy(array,0,copyArray,array.length-first,first);
            System.arraycopy(copyArray,0,NewArray,array.length/2,array.length);
            nextFirst = array.length/2 - 1;
            nextLast = nextFirst + 1 +size;
            array = NewArray;
        }
    }
    private void RemoveResize(int size){
        if(size <= array.length / 4 && size > 0){
            T[] NewArray = (T []) new Object[array.length/2];
            int index = (nextFirst+1)%array.length;
            int k = 0;
            for(int i = 0;i < size;i++){
                NewArray[k] = array[index];
                index = (index + 1)%array.length;
                k++;
            }
            nextFirst = NewArray.length - 1;
            nextLast = size;
            array = NewArray;
        }
    }

    @Override
    public void addFirst(T item) {
        array[nextFirst] = item;
        nextFirst = (array.length + nextFirst - 1) % array.length;
        size++;
        ReSize(size);
    }

    @Override
    public void addLast(T item) {
        array[nextLast] = item;
        nextLast = (nextLast + 1) % array.length;
        size++;
        ReSize(size);
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
        for(T item : this){
            System.out.print(item+" ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if(size>0) {
            int first = (nextFirst+1)%array.length;
            T FirstItem = array[first];
            array[first] = null;
            nextFirst = (array.length + nextFirst + 1) % array.length;
            size--;
            RemoveResize(size);
            return FirstItem;
        }
        return null;
    }

    @Override
    public T removeLast() {
        if(size>0){
            int Last = (nextLast-1+array.length)%array.length;
            T LastItem = array[Last];
            array[Last] = null;
            nextLast = (nextLast - 1 + array.length) % array.length;;
            size--;
            RemoveResize(size);
            return LastItem;
        }
        return null;
    }

    @Override
    public T get(int index) {
        return array[(nextFirst+1+index)%array.length];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
}
