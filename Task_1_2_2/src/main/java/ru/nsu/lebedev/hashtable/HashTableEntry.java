package ru.nsu.lebedev.hashtable;

/**
 * Class for uses in HashTableIterator for one element (key = value).
 *
 * @param <K> type of key.
 * @param <V> type of value.
 */
public class HashTableEntry<K, V> {
    final K key;
    final V value;

    /**
     * Initial method for HashTableEntry.
     *
     * @param key key of hashtable.
     * @param value value of hashtable.
     */
    public HashTableEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Method for getting string representation of the hash table.
     *
     * @return string representation.
     */
    @Override
    public String toString() {
        return key + " = " + value;
    }
}
