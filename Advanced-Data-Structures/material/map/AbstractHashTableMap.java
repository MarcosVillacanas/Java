package material.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

abstract public class AbstractHashTableMap<K, V> implements Map<K, V> {

    protected class HashEntry<K, V> implements Entry<K, V> {

        protected K key;
        protected V value;


        public HashEntry (K k, V v) { this.key = k; this.value = v; }

        @Override
        public K getKey() { return this.key; }

        @Override
        public V getValue() { return this.value; }

        public V setValue (V v) { V oldVal = this.value; this.value = v; return oldVal;}
    }

    private long scale, shift;
    private int prime, size, capacity;
    private HashEntry<K, V>[] buckets;
    private HashEntry<K, V> AVAILABLE;

    public AbstractHashTableMap () {
        this.prime = 109345121;
        this.size = 0;
        this.capacity = 11;
        this.buckets = (HashEntry<K, V>[]) new HashEntry[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
        this.AVAILABLE = new HashEntry<>(null, null);
    }

    public AbstractHashTableMap (int capacity) {
        this.prime = 109345121;
        this.size = 0;
        this.capacity = capacity;
        this.buckets = (HashEntry<K, V>[]) new HashEntry[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
        this.AVAILABLE = new HashEntry<>(null, null);
    }

    public AbstractHashTableMap (int capacity, int prime) {
        this.prime = prime;
        this.size = 0;
        this.capacity = capacity;
        this.buckets = (HashEntry<K, V>[]) new HashEntry[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
        this.AVAILABLE = new HashEntry<>(null, null);
    }

    private void checkKey (K key) throws RuntimeException {
        if (key == null) throw new RuntimeException("Invalid key: null");
    }

    @Override
    public int size() { return this.size; }

    @Override
    public boolean isEmpty() { return this.size == 0; }

    private class HashEntryIndex {

        int index;
        boolean found;

        public HashEntryIndex(int index, boolean found) { this.index = index;this.found = found; }

        public int getIndex() { return index; }

        public boolean isFound() { return found; }
    }

    private int hashValue (K key) {
        return (int) ((Math.abs(this.scale * key.hashCode() + this.shift) % this.prime) % this.capacity);
    }

    private HashEntryIndex findEntry (K key) {
        this.checkKey(key);
        int hashCode = this.hashValue(key), index = hashCode, retry = 0, avail = -1;
        do {
            Entry<K, V> e = this.buckets[index];
            if (e == null) {
                if (avail < 0) avail = index; // key not present
                break;
            }
            else if (key.equals(e.getKey()))
                return new HashEntryIndex(index, true);
            else if (e == AVAILABLE)
                if (avail < 0)
                    avail = index;
            retry++;
            index = (hashCode + offset(retry, key)) % this.capacity;

        } while (retry < this.capacity);

        return new HashEntryIndex(avail, false);
    }

    abstract protected int offset(int retry, K key);

    @Override
    public V put(K key, V value) {
        HashEntryIndex e = this.findEntry(key);

        if (e.isFound()) return this.buckets[e.getIndex()].setValue(value);
        else if (this.size >= this.capacity / 2) {
            this.rehash();
            e = this.findEntry(key);
        }
        this.buckets[e.getIndex()] = new HashEntry<>(key, value);
        this.size++;
        return null;
    }

    private void rehash() {
        this.capacity *= 2;
        HashEntry<K, V> [] oldB = this.buckets;
        this.buckets = (HashEntry<K, V>[]) new HashEntry[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(this.prime - 1) + 1;
        this.shift = rand.nextInt(this.prime);
        for (HashEntry<K, V> he : oldB) {
            if (he != null && he != this.AVAILABLE) {
                HashEntryIndex e = this.findEntry(he.getKey());
                this.buckets[e.getIndex()] = he;
            }
        }
    }

    @Override
    public V get(K key) {
        HashEntryIndex e = this.findEntry(key);

        if (e.isFound()) return this.buckets[e.getIndex()].getValue();
        else return null;
    }

    @Override
    public V remove(K key) {
        HashEntryIndex e = this.findEntry(key);
        if (e.isFound()) {
            V oldVal = this.buckets[e.getIndex()].getValue();
            this.buckets[e.getIndex()] = this.AVAILABLE;
            this.size--;
            return oldVal;
        }
        return null;
    }

    @Override
    public Iterable<K> keys() {
        List<K> keys = new ArrayList<>();
        for (HashEntry<K, V> he : this.buckets) {
            if (he != null && he != this.AVAILABLE) keys.add(he.getKey());
        }
        return keys;
    }

    @Override
    public Iterable<V> values() {
        List<V> values = new ArrayList<>();
        for (HashEntry<K, V> he : this.buckets) {
            if (he != null && he != this.AVAILABLE) values.add(he.getValue());
        }
        return values;
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        List<Entry<K, V>> entries = new ArrayList<>();
        for (HashEntry<K, V> he : this.buckets) {
            if (he != null && he != this.AVAILABLE) entries.add(he);
        }
        return entries;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }

    private class HashTableMapIterator<T, U> implements Iterator<Entry<T, U>> {

        private int pos;
        private HashEntry<T, U> [] buckets;
        private Entry<K, V> AVAILABLE;

        public HashTableMapIterator(HashEntry<T, U>[] buckets, Entry<K, V> AVAILABLE, int capacity) {
            this.buckets = buckets;
            if (capacity == 0) this.pos = this.buckets.length; //hasNext -> false
            else {
                this.pos = 0;
                this.goToNextElement(0);
                this.AVAILABLE = AVAILABLE;
            }
        }

        private void goToNextElement (int start) {
            final int n = this.buckets.length;
            this.pos = start;
            while (this.pos < n &&
                    (this.buckets[this.pos] == null || this.buckets[this.pos] == this.AVAILABLE))
                this.pos++;
        }

        @Override
        public boolean hasNext() { return this.pos < this.buckets.length; }

        @Override
        public Entry<T, U> next() {
            int currentPos = this.pos;
            this.goToNextElement(this.pos + 1);
            return this.buckets[currentPos];
        }
    }
}
