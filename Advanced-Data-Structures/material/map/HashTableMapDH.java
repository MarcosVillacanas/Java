package material.map;

public class HashTableMapDH<K, V> extends AbstractHashTableMap<K, V> {

    public HashTableMapDH () { super(); }

    public HashTableMapDH (int capacity) { super(capacity); }

    public HashTableMapDH (int capacity, int prime) { super(capacity, prime); }

    @Override
    protected int offset(int retry, K key) { return retry * (7 - (key.hashCode() % 7)); }
}
