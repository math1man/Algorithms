package preceptor.hwd;

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
                int leftSum = left[0];
                // try crossing over from the left
                for (int i=left[2]; i < right[1]; i++) {
                    leftSum += array[i];
                    if (leftSum >= left[0]) {
                        left[0] = leftSum;
                        left[2] = i + 1;
                    }
                }
                // try the combination of left and right
                leftSum += right[0];
                if (leftSum >= left[0]) {
                    left[0] = leftSum;
                    left[2] = right[2];
                    // if the combination works, we are done
                    return left;
                }
                // try crossing over from the right
                int rightSum = right[0];
                for (int i=right[1] - 1; i >= left[2]; i--) {
                    rightSum += array[i];
                    if (rightSum >= right[0]) {
                        right[0] = rightSum;
                        right[1] = i;
                    }
                }
                // return whichever is greater
                if (left[0] > right[0]) {
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
        System.out.println(maxSum(new int[]{1,2,-1,0,5,-7,4,2}));
//        for (int j=0; j<10; j++) {
//            int[] random = new int[13];
//            for (int i=0; i<random.length; i++) {
//                random[i] = (int) (Math.random() * 21) - 8;
//            }
//            System.out.println(Arrays.toString(random));
//            System.out.println(maxSum(random));
//        }
    }
}


