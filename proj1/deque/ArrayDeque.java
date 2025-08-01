package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {

    private int size;
    private T[] array;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        size = 0;
        array = (T[]) new Object[8];
        nextFirst = 3;
        nextLast = 4;
    }

    private class ArrayDequeIterator implements Iterator<T> {
        int count = 0;
        int ptr = (nextFirst + 1) % array.length;

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public T next() {
            T item = array[ptr];
            ptr = (ptr + 1) % array.length;
            count++;
            return item;
        }
    }

    private void resize(int size) {
        if (size == array.length) {
            T[] newArray = (T[]) new Object[array.length * 2];
            T[] copyArray = (T[]) new Object[array.length];
            int first = (nextFirst + 1) % array.length;
            System.arraycopy(array, first, copyArray, 0, array.length - first);
            System.arraycopy(array, 0, copyArray, array.length - first, first);
            System.arraycopy(copyArray, 0, newArray, array.length / 2, array.length);
            nextFirst = array.length / 2 - 1;
            nextLast = nextFirst + 1 + size;
            array = newArray;
        }
    }

    private void removeResize(int newSize) { // 参数名建议改一下，避免歧义
        if (size <= array.length / 4 && array.length > 8) { // 增加一个最小长度限制
            T[] newArray = (T[]) new Object[array.length / 2];
            int first = (nextFirst + 1) % array.length;
            int last = (nextLast - 1 + array.length) % array.length;

            if (first <= last) {
                // 情况1：数据没有绕圈，一次复制即可
                System.arraycopy(array, first, newArray, 0, size);
            } else {
                // 情况2：数据绕圈了，需要分两次复制
                int headLength = array.length - first;
                System.arraycopy(array, first, newArray, 0, headLength);
                System.arraycopy(array, 0, newArray, headLength, size - headLength);
            }
            array = newArray;
            nextFirst = array.length - 1;
            nextLast = size;
        }
    }

    @Override
    public void addFirst(T item) {
        array[nextFirst] = item;
        nextFirst = (array.length + nextFirst - 1) % array.length;
        size++;
        resize(size);
    }

    @Override
    public void addLast(T item) {
        array[nextLast] = item;
        nextLast = (nextLast + 1) % array.length;
        size++;
        resize(size);
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (T item : this) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size > 0) {
            int first = (nextFirst + 1) % array.length;
            T firstItem = array[first];
            array[first] = null;
            nextFirst = (array.length + nextFirst + 1) % array.length;
            size--;
            removeResize(size);
            return firstItem;
        }
        return null;
    }

    @Override
    public T removeLast() {
        if (size > 0) {
            int Last = (nextLast - 1 + array.length) % array.length;
            T lastItem = array[Last];
            array[Last] = null;
            nextLast = (nextLast - 1 + array.length) % array.length;
            size--;
            removeResize(size);
            return lastItem;
        }
        return null;
    }

    @Override
    public T get(int index) {
        return array[(nextFirst + 1 + index) % array.length];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<?> other = (Deque<?>) o;
        if (other.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!(this.get(i).equals(other.get(i)))) {
                return false;
            }
        }
        return true;
    }
}
