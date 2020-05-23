package com.ga.folding;

public class Triplet<K, V, I> {

    private K key;
    private V value;
    private I id;

    public Triplet(K key, V value, I id) {
        this.key = key;
        this.value = value;
        this.id = id;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public I getId() {
        return id;
    }
}
