package ru.nsu.lebedev;

import java.util.Random;


public class Main {
    public static void main(String[] args) {
        HeapSort hs = new HeapSort();
        Random random = new Random();
        int[] sizes = {1000, 5000, 10000, 50000, 100000, 500000, 1000000};
        System.out.println("Array Size | Time (ns) | n log(n)");
        for (int size : sizes) {
            int[] arr = random.ints(size, -10000, 10000).toArray();
            long startTime = System.nanoTime();
            hs.heapsort(arr);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            double nLogN = size * (Math.log(size) / Math.log(2));
            System.out.printf("%10d | %12d | %.2f\n", size, duration, nLogN);
        }
    }
}