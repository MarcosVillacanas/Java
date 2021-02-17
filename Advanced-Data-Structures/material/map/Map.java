package material.map;

public interface Map<K, V> extends Iterable<Entry<K, V>> {

    /* Implements means you are using the elements of a Java Interface in your class.
     Extends means that you are creating a subclass of the base class you are extending.
     You can only extend one class in your child class, but you can implement as many interfaces as you would like. */

    public int size();

    public boolean isEmpty();

    public V put(K key, V value) throws RuntimeException;

    public V get (K key) throws RuntimeException;

    public V remove (K key) throws RuntimeException;

    public Iterable<K> keys();

    public Iterable<V> values();

    public Iterable<Entry<K, V>> entries();
}
