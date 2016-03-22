package preceptor.hwd;

/**
 * @author Ari Weiland
 */
public class PeakFinder {

    public static int findPeakIndex(int[] array) {
        return findPeakIndex(array, 0, array.length - 1);
    }

    private static int findPeakIndex(int[] array, int min, int max) {
        // general base case
        if (min == max) {
            return min;
        }
        // split the problem in half
        int mid = (max + min) / 2;
        if (mid < max && array[mid] < array[mid + 1]) {
            // peak is in right half
            return findPeakIndex(array, mid + 1, max);
        } else if (min < mid && array[mid] < array[mid - 1]) {
            // peak is in left half
            return findPeakIndex(array, min, mid - 1);
        } else {
            // we hit the peak
            return mid;
        }
    }

    public static void main(String[] args) {
        System.out.println(findPeakIndex(new int[]{3, 5, 8, 10, 12, 5, 1}));
        System.out.println(findPeakIndex(new int[]{1, 2, 5,  4,  3, 2, 1}));
    }
}
