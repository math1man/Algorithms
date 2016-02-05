import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class Homework2 {

    public static void main(String args[]) {
        int[] array;
        if (args.length > 0) {
            array = new int[args.length];
            for (int i=0; i<args.length; i++) {
                array[i] = Integer.parseInt(args[i]);
            }
            System.out.println(Arrays.toString(array) + ": " + recursiveCount(array));
        }
        array = new int[]{};
        System.out.println(Arrays.toString(array) + ": " + recursiveCount(array));
        array = new int[]{0};
        System.out.println(Arrays.toString(array) + ": " + recursiveCount(array));
        array = new int[]{0, 0};
        System.out.println(Arrays.toString(array) + ": " + recursiveCount(array));
        array = new int[]{0, 1, 2, 3};
        System.out.println(Arrays.toString(array) + ": " + recursiveCount(array));
        array = new int[]{0, 1, 0, 1, 2, 0, 1, 2, 3};
        System.out.println(Arrays.toString(array) + ": " + recursiveCount(array));
        array = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        System.out.println(Arrays.toString(array) + ": " + recursiveCount(array));
    }

    public static int recursiveCount(int[] array) {
        if (array.length == 0) {
            return 0; // base case
        } else {
            int[] next = new int[array.length-1];
            System.arraycopy(array, 1, next, 0, next.length); // copy the subarray from index 1 to the end
            if (array[0] == 0) {
                return 1 + recursiveCount(next); // add one for this iteration and recurse on the subarray
            } else {
                return recursiveCount(next); // recurse on the subarray
            }
        }
    }
}
