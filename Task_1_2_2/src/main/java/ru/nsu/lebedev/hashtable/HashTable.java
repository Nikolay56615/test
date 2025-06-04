package ru.nsu.lebedev.hashtable;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Class hashtable with methods support a full set of operations.
 *
 * @param <K> type of key.
 * @param <V> type of value.
 */
public class HashTable<K, V> implements Iterable<HashTableEntry<K, V>> {
    private static final int DEFAULT_CAPACITY = 16;
    private int capacity;
    private int size;
    private int modCount;
    private ArrayList<K> keys;
    private ArrayList<V> values;

    /**
     * Initial method for hashtable.
     */
    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
        this.modCount = 0;
        this.keys = new ArrayList<>(capacity);
        this.values = new ArrayList<>(capacity);
        initializeLists(capacity);
    }

    /**
     * Initial method for lists of keys and values.
     */
    private void initializeLists(int capacity) {
        for (int i = 0; i < capacity; i++) {
            keys.add(null);
            values.add(null);
        }
    }

    /**
     * Method for creating hash key.
     *
     * @param key key for value.
     * @return hash of key or 0 if key doesn't have value.
     */
    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    /**
     * Method for clear hashtable.
     */
    void clear() {
        size = 0;
        capacity = DEFAULT_CAPACITY;
        modCount = 0;
        keys.clear();
        values.clear();
        initializeLists(capacity);
    }

    /**
     * Method for resize hashtable if size * 2 bigger than capacity.
     */
    private void resize() {
        int newCapacity = capacity * 2;
        final ArrayList<K> oldKeys = keys;
        final ArrayList<V> oldValues = values;
        keys = new ArrayList<>(newCapacity);
        values = new ArrayList<>(newCapacity);
        initializeLists(newCapacity);
        capacity = newCapacity;
        size = 0;
        for (int i = 0; i < oldKeys.size(); i++) {
            if (oldKeys.get(i) != null) {
                put(oldKeys.get(i), oldValues.get(i));
            }
        }
    }

    /**
     * Method for adding key and value to the hashtable.
     *
     * @param key new key.
     * @param value new value.
     */
    public void put(K key, V value) {
        if (size * 2 >= capacity) {
            resize();
        }
        int index = hash(key);
        while (keys.get(index) != null) {
            if (Objects.equals(keys.get(index), key)) {
                values.set(index, value);
                return;
            }
            index = (index + 1) % capacity;
        }
        keys.set(index, key);
        values.set(index, value);
        size++;
        modCount++;
    }

    /**
     * Method for getting value from hashtable[key].
     *
     * @param key key.
     * @return hashtable[key] or null if it doesn't exist.
     */
    public V get(K key) {
        int index = hash(key);
        while (keys.get(index) != null) {
            if (key.equals(keys.get(index))) {
                return values.get(index);
            }
            index = (index + 1) % capacity;
        }
        return null;
    }

    /**
     * Method for remove key and value from hashtable.
     *
     * @param key key that will be deleted.
     * @return oldValue that was removed or null if it doesn't exist.
     */
    public V remove(K key) {
        int index = hash(key);
        while (keys.get(index) != null) {
            if (Objects.equals(keys.get(index), key)) {
                final V oldValue = values.get(index);
                values.set(index, null);
                keys.set(index, null);
                size--;
                modCount++;
                index = (index + 1) % capacity;
                while (keys.get(index) != null) {
                    final K tempKey = keys.get(index);
                    final V tempValue = values.get(index);
                    keys.set(index, null);
                    values.set(index, null);
                    size--;
                    put(tempKey, tempValue);
                    index = (index + 1) % capacity;
                }
                return oldValue;
            }
            index = (index + 1) % capacity;
        }
        return null;
    }

    /**
     * Method for getting size of hashtable.
     *
     * @return size.
     */
    public int size() {
        return size;
    }

    /**
     * Method for getting capacity of hashtable.
     *
     * @return capacity.
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Method for checking key existence in hashtable.
     *
     * @param key the key.
     * @return True or False.
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Method for updating the value by key.
     *
     * @param key old key.
     * @param value new value.
     */
    public void update(K key, V value) {
        if (containsKey(key)) {
            put(key, value);
        }
    }

    /**
     * Method for checking equality of two hashtable.
     *
     * @param o other hashtable.
     * @return True or False.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HashTable<K, V> other = (HashTable<K, V>) o;
        if (this.size != other.size || this.hashCodeAll() != other.hashCodeAll()) {
            return false;
        }
        for (int i = 0; i < capacity; i++) {
            K key = keys.get(i);
            if (key != null) {
                V value = values.get(i);
                Object otherValue = other.get(key);
                if (!Objects.equals(value, otherValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method for getting hash of all values of hashtable.
     *
     * @return hash of hashtable.
     */
    private int hashCodeAll() {
        int hash = 0;
        for (int i = 0; i < capacity; i++) {
            if (keys.get(i) != null) {
                hash += Objects.hashCode(keys.get(i)) ^ Objects.hashCode(values.get(i));
            }
        }
        return hash;
    }

    /**
     * Method for getting string representation of the hash table.
     *
     * @return string representation.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (HashTableEntry<K, V> entry : this) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.key).append(" = ").append(entry.value);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Iterator of hashtable.
     *
     * @return pointer to new iterator class of hashtable.
     */
    @Override
    public Iterator<HashTableEntry<K, V>> iterator() {
        return new HashTableIterator();
    }

    /**
     * Class realization of iterator for hashtable.
     */
    private class HashTableIterator implements Iterator<HashTableEntry<K, V>> {
        private int currentIndex = 0;
        private final int expectedModCount = modCount;

        /**
         * Method for checking existence of next element of hashtable.
         *
         * @return True or False.
         */
        @Override
        public boolean hasNext() {
            while (currentIndex < capacity && keys.get(currentIndex) == null) {
                currentIndex++;
            }
            return currentIndex < capacity;
        }

        /**
         * Method for getting next element of hashtable.
         *
         * @return True or False.
         */
        @Override
        public HashTableEntry<K, V> next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            HashTableEntry<K, V> entry =
                    new HashTableEntry<>(keys.get(currentIndex), values.get(currentIndex));
            currentIndex++;
            return entry;
        }
    }
}
