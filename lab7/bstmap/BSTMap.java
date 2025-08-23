package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K,V>{

    private int size;
    private BSTNode root;

    public BSTMap(){
        size = 0;
        root = null;
    }

    private class BSTNode{

        private K key;
        private V value;
        private BSTNode left;
        private BSTNode right;

        public BSTNode(K key,V value,BSTNode left,BSTNode right){

            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public void setKey(K key){
            this.key = key;
        }

        public K getKey(){
            return key;
        }

        public void setValue(V value){
            this.value = value;
        }

        public V getValue(){
            return value;
        }

        public void setLeft(BSTNode left){
            this.left = left;
        }

        public BSTNode getLeft(){
            return left;
        }

        public void setRight(BSTNode right){
            this.right = right;
        }

        public BSTNode getRight(){
            return right;
        }

    }


    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKeyHelper(root,key);
    }

    private boolean containsKeyHelper(BSTNode node,K key){
        if(node == null){
            return false;
        }
        if(node.getKey().compareTo(key) == 0){
            return true;
        } else if (node.getKey().compareTo(key) > 0) {
            return containsKeyHelper(node.left,key);
        }else {
            return containsKeyHelper(node.right,key);
        }
    }

    @Override
    public V get(K key) {
        return getHelper(root,key);
    }

    private V getHelper(BSTNode node,K key){
        if(node == null){
            return null;
        } else if (node.getKey().compareTo(key) == 0) {
            return node.getValue();
        } else if (node.getKey().compareTo(key) > 0) {
            return getHelper(node.left,key);
        }else {
            return getHelper(node.right,key);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if(containsKey(key)){
            return;
        }
        root = putHelper(root,key,value);
        size++;
    }

    private BSTNode putHelper(BSTNode node,K key,V value){
        if(node == null){
            node = new BSTNode(key, value,null,null);
            return node;
        }
        else if(node.getKey().compareTo(key) > 0){
            node.left = putHelper(node.left,key,value);
            return node;
        }
        else if(node.getKey().compareTo(key) < 0){
            node.right = putHelper(node.right,key,value);
            return node;
        }
        return node;
    }

    @Override
    public V remove(K key) {
        Value<V> value = new Value<>();
        root = removeHelper(root,key,value);
        size--;
        return value.removeNodeValue;
    }

    private BSTNode removeHelper(BSTNode node,K key,Value<V> value){
        if(node == null){
            return null;
        } else if (node.getKey() == key) {
            value.removeNodeValue = node.getValue();
            if(node.left != null && node.right != null){

                K newKey = searchRightMin(node.right,value);
                V newValue = value.minRightNodeValue;
                value.removeNodeValue = node.getValue();

                node.setValue(newValue);
                node.setKey(newKey);

                Value<V> placeHolderInput = new Value<>();
                node.right = removeHelper(node.right,newKey,placeHolderInput);

                return node;
            } else if (node.left != null) {
                return node.left;
            } else if (node.right!= null) {
                return node.right;
            }else{
                return null;
            }
        } else if (node.getKey().compareTo(key) > 0) {
            node.left = removeHelper(node.left, key, value);
            return node;
        } else if (node.getKey().compareTo(key) < 0) {
            node.right = removeHelper(node.right,key,value);
            return node;
        }
        return node;
    }

    private K searchRightMin(BSTNode node,Value<V> value){
        if(node.left == null){
            value.minRightNodeValue = node.getValue();
            return node.getKey();
        }else {
            return searchRightMin(node.left,value);
        }
    }

    private static class Value<V>{
        public V removeNodeValue = null;
        public V minRightNodeValue = null;
    }

    @Override
    public V remove(K key, V value) {
        V currentValue = get(key);
        boolean valueMatches = (currentValue != null) && (currentValue.equals(value));

        if(valueMatches){
            remove(key);
            return currentValue;
        }
        return null;
    }

    private class BSTMapIterator implements Iterator<K>{
        Deque<BSTNode> Stack = new LinkedList<>();

        public BSTMapIterator(){
            pushLeftSpine(root);
        }

        private void pushLeftSpine(BSTNode node) {
            BSTNode currentNode = node;
            while (currentNode != null) {
                Stack.push(currentNode);
                currentNode = currentNode.left;
            }
        }


        @Override
        public boolean hasNext() {
            return !Stack.isEmpty();
        }

        @Override
        public K next() {
            BSTNode currentNode = Stack.pop();

            if(currentNode.right != null){
                pushLeftSpine(currentNode.right);
            }
            return currentNode.getKey();
        }
    }


    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new LinkedHashSet<>();
        for(K key : this){
            keySet.add(key);
        }
        return keySet;
    }

    public void printInOrder(){
        printHelper(root);
    }

    private void printHelper(BSTNode node){
        if(node == null){
            return;
        }
        printHelper(node.left);
        System.out.print("<" + node.key + "," + node.value + ">" + "=>");
        printHelper(node.right);
    }
}
