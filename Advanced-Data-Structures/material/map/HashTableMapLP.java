package material.map;

public class HashTableMapLP<K, V> extends AbstractHashTableMap<K, V> {

    public HashTableMapLP() { super(); }

    public HashTableMapLP(int capacity) { super(capacity); }

    public HashTableMapLP(int capacity, int prime) { super(prime); }

    @Override
    protected int offset(int retry, K key) { return retry; }

}
