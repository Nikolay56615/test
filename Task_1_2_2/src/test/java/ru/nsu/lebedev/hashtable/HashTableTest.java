package ru.nsu.lebedev.hashtable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing HashTable class.
 */
public class HashTableTest {
    private HashTable<String, Number> hashTable;
    private Iterator<HashTableEntry<String, Number>> iterator;

    /**
     * Function for creating default hashtable and iterator.
     */
    @BeforeEach
    public void setUp() {
        hashTable = new HashTable<>();
        hashTable.put("one", 1);
        iterator = hashTable.iterator();
    }

    @Test
    void clearTest() {
        hashTable.clear();
        assertEquals(new HashTable<String, Number>(), hashTable);
    }

    @Test
    void putTest() {
        assertFalse(hashTable.containsKey("two"));
        hashTable.put("two", 2);
        assertTrue(hashTable.containsKey("two"));
    }

    @Test
    void resizeTest() {
        HashTable<Number, Number> otherHashTable = new HashTable<>();
        for (int i = 0; i < 18; i++) {
            otherHashTable.put(i, i);
        }
        assertEquals(64, otherHashTable.capacity());
    }

    @Test
    void getTest() {
        assertEquals(1, hashTable.get("one"));
    }

    @Test
    void removeTest() {
        hashTable.remove("one");
        assertFalse(hashTable.containsKey("one"));
    }

    @Test
    void sizeTest() {
        assertEquals(1, hashTable.size());
    }

    @Test
    void capacityTest() {
        assertEquals(16, hashTable.capacity());
    }

    @Test
    void containsTest() {
        assertTrue(hashTable.containsKey("one"));
    }

    @Test
    void updateTest() {
        hashTable.update("one", 1.0);
        assertEquals(1.0, hashTable.get("one"));
    }

    @Test
    void equalsTest() {
        HashTable<String, Number> otherHashTable = new HashTable<>();
        assertNotEquals(hashTable, otherHashTable);
        otherHashTable.put("one", 1.0);
        assertNotEquals(hashTable, otherHashTable);
        otherHashTable.update("one", 1);
        assertEquals(hashTable, otherHashTable);
        hashTable.put("one", 1.0);
        hashTable.put("two", 2.0);
        otherHashTable.put("two", 2.0);
        otherHashTable.put("one", 1.0);
        assertEquals(hashTable, otherHashTable);
    }

    @Test
    void toStringTest() {
        hashTable.put("two", 2);
        assertEquals("{one = 1, two = 2}", hashTable.toString());
    }

    @Test
    void iteratorTest() {
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    void next() {
        assertEquals(1, iterator.next().value);
        hashTable.put("two", 2);
        iterator = hashTable.iterator();
        assertEquals(1, iterator.next().value);
        assertEquals(2, iterator.next().value);
        assertFalse(iterator.hasNext());
    }

    @Test
    void iteratorNoSuchElementExceptionTest() {
        iterator.next();
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void iteratorConcurrentModificationExceptionTest() {
        hashTable.put("two", 2);
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }
}
