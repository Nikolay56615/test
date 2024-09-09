package ru.nsu.lebedev;

/**
 * The {@code heapSort} class implements a Heap Sort algorithm.
 * This sorting algorithm converts an unordered array into a maximum heap,
 * and then extracts the elements one at a time, creating a sorted array.
 */
public class HeapSort {
    private int heapSize;
    private int[] arr;

    /**
     * Swaps two elements of the array.
     */
    private void swap(int a, int b) {
        int tmp = this.arr[a];
        this.arr[a] = this.arr[b];
        this.arr[b] = tmp;
    }

    /**
     * Restores the heap structure, starting from the node with the index {@code i}.
     * If the current element is smaller than the child, an exchange and "sifting down" occurs.
     *
     * @param i index of the element to be "sifted"
     */
    private void siftDown(int i) {
        int index = i;
        int left = i * 2 + 1;
        int right = i * 2 + 2;
        int swapI;
        while (left < this.heapSize) {
            swapI = index;
            if (this.arr[left] > this.arr[index]) {
                swapI = left;
            }
            if (right < this.heapSize
                    && this.arr[right] > this.arr[swapI]) {
                swapI = left + 1;
            }
            if (swapI != index) {
                swap(swapI, index);
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
    public int[] heapsort(int[] arr) {
        this.arr = arr.clone();
        this.heapSize = this.arr.length;
        for (int i = this.arr.length / 2 - 1; i >= 0; --i) {
            this.siftDown(i);
        }
        for (int i = 0; i < this.arr.length - 1; ++i) {
            swap(0, this.heapSize - 1);
            this.heapSize--;
            siftDown(0);
        }
        return this.arr;
    }

    /**
     * @return Sorted arr.
     */
    public int[] retArr() {
        return this.arr;
    }
}
