package deque;

import java.util.Collection;
import deque.Deque;
import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>,Iterable<T>{

    public Node<T> sentinel;
    public int size;

   //Node是一个节点，那么节点就不应该作为链表的整体，节点客观上可以模拟链表的部分功能，实际上却是违背自然语言逻辑的，不能用局部代替整体
    public static class Node<T>{
        public Node<T> pre;
        public Node<T> next;
        public T item;

        public Node(T item,Node<T> next,Node<T> pre){
            this.item = item;
            this.next = next;
            this.pre = pre;
        }
    }


    private class lldIterator implements Iterator<T>{

        private Node<T> ptr;
        public  lldIterator(){
            ptr = sentinel.next;
        }
        @Override
        public boolean hasNext() {
            return ptr != sentinel;
        }

        @Override
        public T next() {
            T item = ptr.item;
            ptr = ptr.next;
            return item;
        }
    }

    public LinkedListDeque(){
        size = 0;
        sentinel = new Node<>(null,null,null);
        sentinel.next = sentinel;
        sentinel.pre = sentinel;
    }

    @Override
    public void addFirst(T item) {
        Node<T> currentNode = sentinel.next;
        sentinel.next = new Node<>(item,currentNode,sentinel);
        currentNode.pre = sentinel.next;
        size++;
    }

    @Override
    public void addLast(T item) {
        Node<T> currentNode = sentinel.pre;
        sentinel.pre = new Node<>(item,sentinel,currentNode);
        currentNode.next = sentinel.pre;
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
        for (T item : this) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if(sentinel.next == sentinel){
            return null;
        }
        Node<T> currentNode = sentinel.next;
        sentinel.next = currentNode.next;
        currentNode.next.pre = sentinel;
        size--;
        return currentNode.item;
    }

    @Override
    public T removeLast() {
        if(sentinel.pre == sentinel){
            return null;
        }
        Node<T> currentNode = sentinel.pre;
        sentinel.pre = currentNode.pre;
        currentNode.pre.next = sentinel;
        size--;
        return currentNode.item;
    }

    @Override
    public T get(int index) {
        Node<T> ptr = sentinel;
        for(int i = 0;i <= index;i++){
            ptr = ptr.next;
        }
        return ptr.item;
    }

    @Override
    public Iterator<T> iterator() {
        return new lldIterator();
    }

    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if(!(o instanceof Deque)){
            return false;
        }
        Deque<?> other = (Deque<?>) o;
        if(other.size() != size){
            return false;
        }
        for(int i = 0;i<size;i++){
            if(!(this.get(i).equals(other.get(i)))){
                return false;
            }
        }
        return true;
    }

    public T getRecursive(int index){
        return NodeRecursive(index,sentinel.next);
    }

    private T NodeRecursive(int index,Node<T> node){
        if(index == 0){
            return node.item;
        }
        return NodeRecursive(index - 1,node.next);
    }
}
