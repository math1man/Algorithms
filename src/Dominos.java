import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class Dominos {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(calculateCombos(10)));
        System.out.println(Arrays.toString(calculateCombosSlow(10)));
        System.out.println(calculateCombo(5));
    }

    /**
     * Builds a List of the number of combos for the first n prisms.
     * Runs in O(n) time.
     * @param maxN
     * @return
     */
    public static long[] calculateCombos(int maxN) {
        long[] combos = new long[maxN + 1];
        combos[0] = 1L;
        if (maxN > 0) {
            // baseSum represents all possible
            long baseSum = combos[0]; // 4 * combos[0]
            // calculate combos[1] manually so we don't have to worry about combos[-1]
            combos[1] = 2; // baseSum - 2 * combos[0] + combos[-1] where combos[-1] is assumed to be 0
            for (int i=2; i<=maxN; i++) {
                // these two lines are the core of the algorithm
                baseSum += combos[i-1];
                combos[i] = 4 * baseSum - 2 * combos[i-1] + combos[i-2];
            }
        }
        return combos;
    }

    /**
     * Calculates the number of combos for the prism of size n.
     * Runs in O(n) time.
     * @param n
     * @return
     */
    public static long calculateCombo(int n) {
        long cPrev = 0; // starting value for n = -1
        long cCurr = 1; // starting value for n = 0
        long cNext;
        long baseSum = 0;
        for (int i=1; i<=n; i++) {
            baseSum += 4 * cCurr;
            cNext = baseSum + cPrev - 2 * cCurr;
            cPrev = cCurr;
            cCurr = cNext;
        }
        return cCurr;
    }

    public static long[] calculateCombosSlow(int maxN) {
        long[] combos = new long[maxN + 1];
        Arrays.fill(combos, 0L);
        combos[0] = 1L; // base case
        for (int i=1; i<=maxN; i++) {
            // this summation is the core of the algorithm
            for (int j=0; j<i; j++) {
                combos[i] += starFn(i - j) * combos[j];
            }
        }
        return combos;
    }

    public static int starFn(int n) {
        if (n > 2) {
            // all taller slices have only 4 orientations: N, E, S, and W
            return 4;
        } else if (n == 2) {
            // a slice of height 2 has 5 orientations: N, E, S, W, and Vertical
            return 5;
        } else if (n == 1) {
            // a slice of height 1 has 2 orientations: N-S and E-W
            return 2;
        } else {
            return 0;
        }
    }

    /*
    Top view of slices of N height:

    n > 2:


    N:  +--+--+
        |     |
        +--+--+
        |  |  |
        +--+--+

    E:  +--+--+
        |  |  |
        +--+  +
        |  |  |
        +--+--+

    S:  +--+--+
        |  |  |
        +--+--+
        |     |
        +--+--+

    W:  +--+--+
        |  |  |
        +  +--+
        |  |  |
        +--+--+

    n == 2 adds vertical:

    V:  +--+--+
        |  |  |
        +--+--+
        |  |  |
        +--+--+

    n == 1 has only:

    NS: +--+--+
        |     |
        +--+--+
        |     |
        +--+--+

    EW: +--+--+
        |  |  |
        +  +  +
        |  |  |
        +--+--+

     */
}
