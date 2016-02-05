package programming5;

/**
 * @author Ari Weiland
 */
public class FloydsAlgorithm {

    /**
     * Use this to numerically represent infinity.
     * The printMatrix method prints this value as "oo"
     */
    public static final int INF = Integer.MAX_VALUE;

    /**
     * Tests Floyd's Algorithm on the example matrix from Homework 10.
     * @param args
     */
    public static void main(String[] args) {
        int[][] matrix = {
                {  0, INF,   8, 20, INF},
                {  5,   0, INF,  9, INF},
                { 10, INF,   0,  6,  12},
                { 20, INF,   6,  0,   3},
                {INF,   2,  12,  3,   0}};
        printMatrix(matrix);
        int[][] output = floyd(matrix);
        printMatrix(output);

        for (int k=100; k<=2000; k+=100) {
            int[][] randomMatrix = new int[k][k];
            for (int i=0; i<k; i++) {
                for (int j=0; j<k; j++) {
                    if (j != i) {
                        int value = (int) (Math.random() * 20);
                        randomMatrix[i][j] = (value == 0 ? INF : value);
                    }
                }
            }
            long start = System.currentTimeMillis();
            floyd(randomMatrix);
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("Size " + k + ": " + elapsed + " ms");
        }
    }

    /**
     * Runs Floyd's algorithm on the input adjacency matrix.
     * It initially copies the matrix to a new matrix, leaving
     * the original matrix unchanged.
     *
     * Note that the adjacency matrix is assumed to be a square
     * matrix. If this assumption is violated, it may cause problems.
     *
     * @param adjMat
     * @return
     */
    public static int[][] floyd(int[][] adjMat) {
        int nodes = adjMat.length;
        int[][] output = new int[nodes][nodes];
        for (int i=0; i<nodes; i++) {
            System.arraycopy(adjMat[i], 0, output[i], 0, nodes);
        }
        for (int k=0; k<nodes; k++) {
            for (int i=0; i<nodes; i++) {
                if (k != i) {
                    for (int j=0; j<nodes; j++) {
                        if (k != j && i != j) {
                            int dist = output[i][k] + output[k][j];
                            if (dist > 0 && output[i][j] > dist) { // the dist > 0 check in case we overflowed an INF
                                output[i][j] = dist;
                            }
                        }
                    }
                }
            }
        }
        return output;
    }

    public static void printMatrix(int[][] adjMat) {
        int nodes = adjMat.length;
        for (int[] anAdjMat : adjMat) {
            for (int j = 0; j < nodes; j++) {
                int val = anAdjMat[j];
                if (val == INF) {
                    System.out.print("oo\t");
                } else {
                    System.out.print(val + "\t");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
