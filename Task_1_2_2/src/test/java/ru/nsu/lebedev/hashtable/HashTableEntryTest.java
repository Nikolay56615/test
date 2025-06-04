package ru.nsu.lebedev.hashtable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing HashTableEntry class.
 */
public class HashTableEntryTest {
    private HashTableEntry<String, Number> entry;

    /**
     * Function for creating default entry.
     */
    @BeforeEach
    public void setUp() {
        entry = new HashTableEntry<>("one", 1.0);
    }

    @Test
    public void constructorTest() {
        assertNotNull(entry);
        assertEquals("one", entry.key);
        assertEquals(1.0, entry.value);
    }

    @Test
    public void toStringTest() {
        assertEquals("one = 1.0", entry.toString());
    }

    @Test
    public void nullKeyAndValueTest() {
        HashTableEntry<String, Number> nullEntry = new HashTableEntry<>(null, null);
        assertNotNull(nullEntry);
        assertEquals("null = null", nullEntry.toString());
    }

    @Test
    public void keyAndValueTypeSafetyTest() {
        HashTableEntry<Integer, String> intStringEntry = new HashTableEntry<>(1, "value");
        assertEquals(1, intStringEntry.key);
        assertEquals("value", intStringEntry.value);
    }
}
