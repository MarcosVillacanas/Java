package material.map;

public class HashTableMapQP<K, V> extends AbstractHashTableMap<K, V> {

    final int c1 = 3, c2 = 5;

    public HashTableMapQP() { super(); }

    public HashTableMapQP(int capacity) { super(capacity); }

    public HashTableMapQP(int capacity, int prime) { super(prime); }

    @Override
    protected int offset(int retry, K key) { return (this.c1 * retry) + (this.c2 * retry * retry); }

}
