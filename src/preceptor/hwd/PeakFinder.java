package preceptor.hwd;

/**
 * @author Ari Weiland
 */
public class PeakFinder {

    public static int findPeakIndex(int[] array) {
        return findPeakIndex(array, 0, array.length - 1);
    }

    private static int findPeakIndex(int[] array, int min, int max) {
        if (min == max) {
            return min;
        }
        int mid = (max + min) / 2;
        if (mid < max && array[mid] < array[mid + 1]) {
            return findPeakIndex(array, mid + 1, max);
        } else if (min < mid && array[mid] < array[mid - 1]) {
            return findPeakIndex(array, min, mid - 1);
        } else {
            return mid;
        }
    }

    public static void main(String[] args) {
        System.out.println(findPeakIndex(new int[]{3, 5, 8, 10, 12, 5, 1}));
        System.out.println(findPeakIndex(new int[]{1, 2, 5,  4,  3, 2, 1}));
    }
}
