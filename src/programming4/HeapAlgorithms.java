package programming4;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class HeapAlgorithms {

    public static void buildHeap(int[] array) {
        int half = array.length / 2 - 1;
        for (int r = half; r >= 0; r--) {
            int currNode = r;
            int value = array[r];
            int bigChildNode = findBigChild(array, currNode, array.length);
            while (currNode <= half && value < array[bigChildNode]) {
                array[currNode] = array[bigChildNode];
                array[bigChildNode] = value;
                currNode = bigChildNode;
                bigChildNode = findBigChild(array, currNode, array.length);
            }
        }
    }

    private static int findBigChild(int[] array, int index, int length) {
        int childNode1 = (index + 1) * 2 - 1;
        int childNode2 = (index + 1) * 2;
        if (childNode2 < length && array[childNode1] < array[childNode2]) {
            return childNode2;
        } else {
            return childNode1;
        }
    }

    public static void heapSort(int[] array) {
        buildHeap(array);
        for (int r = array.length - 1; r>0; r--) {
            int value = array[r];
            array[r] = array[0];
            array[0] = value;
            int currNode = 0;
            int bigChildNode = findBigChild(array, currNode, r);
            while (currNode < (r / 2) && value < array[bigChildNode]) {
                array[currNode] = array[bigChildNode];
                array[bigChildNode] = value;
                currNode = bigChildNode;
                bigChildNode = findBigChild(array, currNode, r);
            }
        }
    }

    public static void main(String[] args) {
        int[] test = new int[]{1, 2, 3, 4, 5, 6, 2, 8, 9, 10};
        System.out.println("Test array: " + Arrays.toString(test));
        buildHeap(test);
        System.out.println("Heap of test array: " + Arrays.toString(test));
        heapSort(test);
        System.out.println("Sorted test array: " + Arrays.toString(test));

        int[] random = generateRandomArray(40, 20);
        System.out.println("Random array: " + Arrays.toString(random));
        buildHeap(random);
        System.out.println("Heap of random array: " + Arrays.toString(random));
        heapSort(random);
        System.out.println("Sorted random array: " + Arrays.toString(random));

        Map<Integer, List<int[]>> timingMap = new TreeMap<Integer, List<int[]>>();
        for (int i=100; i < 100000; i *= 2) {
            List<int[]> randoms = new ArrayList<int[]>();
            for (int j=0; j<1000; j ++) {
                randoms.add(generateRandomArray(i * i, i));
            }
            timingMap.put(i, randoms);
        }

        for (int size : timingMap.keySet()) {
            List<int[]> randoms = timingMap.get(size);
            long start = System.nanoTime();
            for (int[] array : randoms) {
                buildHeap(array);
            }
            long elapsed = System.nanoTime() - start;
            System.out.println(size + ": " + (elapsed / size) + " ns/size for 1000 heaps");
        }

        for (int size : timingMap.keySet()) {
            List<int[]> randoms = timingMap.get(size);
            long start = System.nanoTime();
            for (int[] array : randoms) {
                heapSort(array);
            }
            long elapsed = System.nanoTime() - start;
            System.out.println(size + ": " + (elapsed / size) + " ns/size for 1000 heaps");
        }
    }

    public static int[] generateRandomArray(int range, int length) {
        int[] random = new int[length];
        for (int i=0; i<length; i++) {
            random[i] = (int) (Math.random() * range);
        }
        return random;
    }
}
