package ru.nsu.lebedev;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


/**
 * Tests for HeapSort.
 */
public class MainTest {
    @Test
    void callMain() {
        Main.main(new String[]{});
        assertTrue(true);
    }

    @Test
    void simpleSort() {
        assertArrayEquals(
                new int[]{-5, 1, 4, 7, 9},
                HeapSort.heapsort(new int[]{4, 9, 1, -5, 7})
        );
    }

    @Test
    void voidTest() {
        assertArrayEquals(new int[]{}, HeapSort.heapsort(new int[]{}));
    }

    @Test
    void alreadySortTest() {
        assertArrayEquals(new int[]{-6, 1, 4, 6, 8}, HeapSort.heapsort(new int[]{-6, 1, 4, 6, 8}));
    }

    @Test
    void sortWithCopies() {
        assertArrayEquals(new int[]{-19, -1, -1, 0, 0, 0, 9, 20},
                HeapSort.heapsort(new int[]{9, -1, 20, -19, 0, 0, 0, -1}));
    }

    @Test
    void oneElemTest() {
        assertArrayEquals(new int[]{1}, HeapSort.heapsort(new int[]{1}));
    }

    @Test
    void intMaxMin() {
        assertArrayEquals(new int[]{-2147483648, 2147483647},
                HeapSort.heapsort(new int[]{2147483647, -2147483648}));
    }

    @Test
    void largeTest() {
        assertArrayEquals(new int[]{-1660231475, -1589796938, -1073373868,
            -1008088036, -141299706, -1000, 0, 67869047, 116536236, 123456789,
            657504910, 1287215732},
            HeapSort.heapsort(new int[]{1287215732, -141299706, -1008088036, 116536236,
            -1589796938, -1660231475, -1073373868, -1000, 0, 67869047,
            657504910, 123456789}));
    }
}