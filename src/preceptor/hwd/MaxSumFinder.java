package preceptor.hwd;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class MaxSumFinder {

    public static int maxSum(int[] array) {
        if (array.length == 0) {
            return 0;
        } else {
            return maxSum(array, 0, array.length)[0];
        }
    }

    private static int[] maxSum(int[] array, int min, int max) {
        if (max - min == 1) {
            return new int[]{Math.max(array[min], 0), min, max};
        } else {
            int mid = (min + max) / 2;
            int[] left = maxSum(array, min, mid);
            int[] right = maxSum(array, mid, max);
            if (left[0] == 0) {
                return right;
            } else if (right[0] == 0) {
                return left;
            } else {
                int spanSum = left[0] + right[0];
                for (int i=left[2]; i < right[1]; i++) {
                    spanSum += array[i];
                }
                if (spanSum > left[0] && spanSum > right[0]) {
                    return new int[]{spanSum, left[1], right[2]};
                } else if (left[0] > right[0]) {
                    return left;
                } else {
                    return right;
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(maxSum(new int[]{3, -6, 1, 0, 9, -4, 2, 1, -2, 6, -4}));
        System.out.println(maxSum(new int[0]));
        System.out.println(maxSum(new int[]{-4}));
        System.out.println(maxSum(new int[]{3}));
        for (int j=0; j<10; j++) {
            int[] random = new int[13];
            for (int i=0; i<random.length; i++) {
                random[i] = (int) (Math.random() * 21) - 8;
            }
            System.out.println(Arrays.toString(random));
            System.out.println(maxSum(random));
        }
    }
}


