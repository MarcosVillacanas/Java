package material.map;

import java.util.*;

public class HashTableMapSC<K, V> implements Map<K, V> {

    private class HashEntry<T, U> implements Entry<T, U> {

        T key;
        U value;

        public HashEntry(T key, U value) { this.key = key; this.value = value; }

        @Override
        public T getKey() { return key; }

        @Override
        public U getValue() { return value; }

        public void setValue(U value) { this.value = value; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HashEntry<T, U> hashEntry = (HashEntry<T, U>) o;
            return Objects.equals(key, hashEntry.key) && Objects.equals(value, hashEntry.value);
        }

        @Override
        public int hashCode() { return Objects.hash(key, value); }
    }

    private int prime, size, capacity;
    private long scale, shift;
    private List<HashEntry<K, V>>[] buckets;

    public HashTableMapSC () {
        this.prime = 109345121;
        this.size = 0;
        this.capacity = 11;
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
        this.buckets = (List<HashEntry<K,V>>[]) new List[this.capacity];
    }
    public HashTableMapSC (int capacity) {
        this.prime = 109345121;
        this.size = 0;
        this.capacity = capacity;
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
        this.buckets = (List<HashEntry<K,V>>[]) new List[this.capacity];
    }
    public HashTableMapSC (int prime, int capacity) {
        this.prime = prime;
        this.size = 0;
        this.capacity = capacity;
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
        this.buckets = (List<HashEntry<K,V>>[]) new List[this.capacity];
    }

    private void checkKey (K key) throws RuntimeException {
        if (key == null) throw new RuntimeException("Invalid key: null");
    }

    public int hashValue (K key) {
        return (int) (Math.abs((key.hashCode() * this.scale + this.shift) % this.prime) % this.capacity);
    }

    private HashEntry<K, V> findEntry (K key) {
        int hashValue = this.hashValue(key);
        if (this.buckets[hashValue] == null || this.buckets[hashValue].isEmpty()) return new HashEntry<>(null, null);
        else for (HashEntry<K, V> he : this.buckets[hashValue]) if (he.getKey().equals(key)) return he;
        return new HashEntry<>(null, null);
    }

    private void rehash (int newCapacity) {
        List<HashEntry<K, V>>[] newBuckets = (List<HashEntry<K, V>>[]) new List[newCapacity];
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
        this.capacity = newCapacity;
        for (List<HashEntry<K, V>> l : this.buckets) {
            if (l != null) {
                for (HashEntry<K, V> he : l) {
                    int index = this.hashValue(he.getKey());
                    if (newBuckets[index] == null) newBuckets[index] = new LinkedList<>();
                    newBuckets[index].add(he);
                }
            }
        }
        this.buckets = newBuckets;
    }

    @Override
    public int size() { return this.size; }

    @Override
    public boolean isEmpty() { return this.size == 0; }

    @Override
    public V put(K key, V value) throws RuntimeException {
        this.checkKey(key);
        HashEntry<K, V> he = this.findEntry(key);
        if (he.getKey() != null) {
            V oldValue = he.getValue();
            he.setValue(value);
            return oldValue;
        }
        this.size++;
        if (this.size >= (this.capacity * 0.75)) this.rehash(this.capacity * 2);

        int hashValue = this.hashValue(key);
        if (this.buckets[hashValue] == null) this.buckets[hashValue] = new LinkedList<>();
        this.buckets[hashValue].add(new HashEntry<>(key, value));
        return null;
    }

    @Override
    public V get(K key) { this.checkKey(key); return this.findEntry(key).getValue(); }

    @Override
    public V remove(K key) {
        this.checkKey(key);
        HashEntry<K, V> he = this.findEntry(key);
        if (he.getValue() != null) {
            this.buckets[this.hashValue(key)].remove(he);
            this.size--;
            return he.getValue();
        }
        return null;
    }

    @Override
    public Iterable<K> keys() {
        List<K> keys = new LinkedList<>();
        Arrays.stream(this.buckets).iterator().forEachRemaining(l ->
        { if (l != null) l.forEach(he -> keys.add(he.getKey())); } );
        return keys;
    }

    @Override
    public Iterable<V> values() {
        List<V> values = new LinkedList<>();
        Arrays.stream(this.buckets).iterator().forEachRemaining(l ->
        { if (l != null) l.forEach(he -> values.add(he.getValue())); } );
        return values;
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        List<Entry<K, V>> entries = new LinkedList<>();
        Arrays.stream(this.buckets).iterator().forEachRemaining(l -> { if (l != null) entries.addAll(l); } );
        return entries;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() { return new HashTableMapSCIterator<K, V>(this.buckets); }

    private class HashTableMapSCIterator<K, V> implements Iterator<Entry<K, V>> {

        private Queue<HashEntry<K, V>> q;
        private int localIndex, globalIndex;
        private HashEntry<K, V> next;
        private List<HashEntry<K, V>>[] buckets;

        public HashTableMapSCIterator(List<HashEntry<K, V>>[] b) {
            this.q = new LinkedList<>();
            this.next = this.getNext();
            this.buckets = b;
        }

        private HashEntry<K,V> getNext() {
            if (globalIndex >= this.buckets.length) return new HashEntry<>(null, null);
            if (this.buckets[globalIndex] == null || this.buckets[globalIndex].isEmpty()) {
                globalIndex++;
                return this.getNext();
            }
            if (localIndex >= this.buckets[globalIndex].size()) {
                localIndex = 0;
                globalIndex++;
                return this.getNext();
            }
            return this.buckets[globalIndex].get(localIndex++);
        }

        @Override
        public boolean hasNext() { return this.next.getKey() != null; }

        @Override
        public HashEntry<K, V> next() {
            HashEntry<K, V> toReturn = this.next;
            this.next = this.getNext();
            return toReturn;
        }
    }
}