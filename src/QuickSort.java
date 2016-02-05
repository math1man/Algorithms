import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class QuickSort {

    public static int swapCount = 0;
    public static int hoareSwapCount = 0;
    public static int lomutoSwapCount = 0;

    public static void main(String[] args) {
        System.out.println(Arrays.toString(quickSort(new int[]{4, 2, 3, 9, 7, 1, 5, 8, 2, 6})));
        System.out.println();
        System.out.println(Arrays.toString(quickSort(new int[]{1, 2, 2, 3, 4, 5, 6, 7, 8, 9})));
        System.out.println();
        System.out.println(Arrays.toString(quickSort(new int[]{5, 2, 3, 4, 1, 6, 7, 8, 9})));
    }

    public static int[] quickSort(int[] input) {
        swapCount = 0;
        int[] sorted = quickSortHelper(input, 0, input.length - 1);
        System.out.println("Swap count: " + swapCount);
        return sorted;
    }

    public static int[] quickSortHelper(int[] input, int start, int end) {
        if (!isOrdered(input, start, end)) {
            int s = lomutoPartition(input, start, end);
            if (s != -1) {
                if (start < s - 1) {
                    quickSortHelper(input, start, s - 1);
                }
                if (s + 1 < end) {
                    quickSortHelper(input, s + 1, end);
                }
            }
        }
        return input;
    }

    public static int lomutoPartition(int[] input, int start, int end) {
        int p = input[start];
        int s = start;
        for (int i=s; i<=end; i++) {
            int t = input[i];
            if (t < p) {
                s++;
                input[i] = input[s];
                input[s] = t;
                swapCount++;
                lomutoSwapCount++;
            }
        }
        input[start] = input[s];
        input[s] = p;
        swapCount++;
        lomutoSwapCount++;
        return s;
    }

    public static int hoarePartition(int[] input, int start, int end) {
        int p = input[start];
        int i = start + 1;
        int j = end;
        while (i <= j) {                    // if i > j, size 1 partition, nothing happens
            while (input[i] < p && i < j) { // if i >= j, it is now in the greater-than part, so it
                i++;                        // is done and we just need to find where to put the pivot
            }
            while (input[j] > p) {          // for the case where i >= j, it must now find the place
                j--;                        // to put the pivot, which is the first point <= p
            }
            if (i < j) {                    // if i < j, we are still partitioning, and need to swap
                int t = input[i];           // the two points because they are on the wrong sides
                input[i] = input[j];
                input[j] = t;
                swapCount++;
                hoareSwapCount++;
            } else if (j != start) {        // otherwise, we are just moving the pivot into its place
                input[start] = input[j];
                input[j] = p;
                swapCount++;
                hoareSwapCount++;
            }
        }
        return j;
    }

    public static boolean isOrdered(int[] input, int start, int end) {
        int a = input[start];
        boolean ordered = true;
        for (int i=start+1; i<=end && ordered; i++) {
            ordered = a <= input[i];
            a = input[i];
        }
        return ordered;
    }
}
