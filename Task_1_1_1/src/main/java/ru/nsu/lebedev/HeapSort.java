package ru.nsu.lebedev;

/**
 * The {@code heapSort} class implements a Heap Sort algorithm.
 * This sorting algorithm converts an unordered array into a maximum heap,
 * and then extracts the elements one at a time, creating a sorted array.
 */
public class HeapSort {
    /**
     * Swaps two elements of the array.
     */
    private static void swap(int[] arr, int a, int b) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    /**
     * Restores the heap structure, starting from the node with the index {@code i}.
     * If the current element is smaller than the child, an exchange and "sifting down" occurs.
     *
     * @param i index of the element to be "sifted"
     */
    private static void siftDown(int[] arr, int heapSize, int i) {
        int index = i;
        int left = i * 2 + 1;
        int right = i * 2 + 2;
        int swapI;
        while (left < heapSize) {
            swapI = index;
            if (arr[left] > arr[index]) {
                swapI = left;
            }
            if (right < heapSize
                    && arr[right] > arr[swapI]) {
                swapI = left + 1;
            }
            if (swapI != index) {
                swap(arr, swapI, index);
                index = swapI;
                left = index * 2 + 1;
                right = index * 2 + 2;
            } else {
                break;
            }
        }
    }

    /**
     * Main sorter's method.
     * Converts an array to a maximum heap.
     * Builds a sorted array by extracting items from the heap one at a time.
     * Restores the properties of the heap after each extraction.
     *
     * @param arr Unsorted ints' array.
     * @return Sorted arr.
     */
    public static int[] heapsort(int[] arr) {
        arr = arr.clone();
        int heapSize = arr.length;
        for (int i = arr.length / 2 - 1; i >= 0; --i) {
            siftDown(arr, heapSize, i);
        }
        for (int i = 0; i < arr.length - 1; ++i) {
            swap(arr, 0, heapSize - 1);
            heapSize--;
            siftDown(arr, heapSize, 0);
        }
        return arr;
    }
}