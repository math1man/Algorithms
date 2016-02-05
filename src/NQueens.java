import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class NQueens {

    public static void main(String[] args) {
//        double repetitions = 10.0;
//        long prevElapsed = 0;
//        for (int i=1; i<17; i++) {
//            int count = 0;
//            long start = System.currentTimeMillis();
//            for (int j=0; j<repetitions; j++) {
//                count = nQueens(i);
//            }
//            long elapsed = System.currentTimeMillis() - start;
//            System.out.print(i + " queens: " + count + " positions (" + elapsed / repetitions + " ms");
//            if (prevElapsed != 0) {
//                System.out.print(", increased by a factor of " + (elapsed * 10 / prevElapsed / 10.0));
//            }
//            System.out.println(")");
//            prevElapsed = elapsed;
//        }
        System.out.println(nQueens(8));
    }

    /**
     * This generates all valid solutions, ignoring symmetrical solutions about
     * the horizontal and vertical axes.  Does not ignore symmetric solutions
     * about either diagonal axis or rotations by 180 degrees.
     * @param n
     * @return
     */
    public static int nQueens(int n) {
        // State is represented by an n-array where the array
        // element refers to the column that queen is in
        // A 0 indicates there is no queen in that row
        int[] state = new int[n];
        int rowIndex = 1;
        int count = 0;
//        List<int[]> states = new ArrayList<int[]>();
        while (rowIndex > 0) {
            // Increment the position of the queen in the currently specified row
            state[rowIndex - 1]++;
            // If we go over, reset that row and decrement the rowIndex
            // so that it will increment the previous row position
            // If we are at the first row, placing the queen past
            // half way is symmetric, so we can ignore them
            if (state[rowIndex - 1] > (rowIndex == 1 ? (n + 1) / 2 : n)) {
                state[rowIndex - 1] = 0;
                rowIndex--;
            } else {
                boolean isValid = true;
                // Check that the generated state is valid
                for (int i = 0; i < rowIndex - 1 && isValid; i++) {
                    isValid = (state[i] != state[rowIndex - 1])                             // check column
                            && ((state[i] - i) != (state[rowIndex - 1] - rowIndex + 1))     // check top-left to bot-right diagonal
                            && ((state[i] + i) != (state[rowIndex - 1] + rowIndex - 1));    // check bot-left to top-right diagonal

                }
                if (isValid) {
                    if (rowIndex < n) {
                        // If valid and there are still more rows to position, go to the next row
                        rowIndex++;
                    } else {
                        // Otherwise, increment the counter, reset the current row, and go back
                        count++;
//                        states.add(state);
                        System.out.println(Arrays.toString(state));
                        state[rowIndex - 1] = 0;
                        rowIndex--;
                    }
                }
            }
        }
        return count;
    }
}
