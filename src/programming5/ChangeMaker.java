package programming5;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class ChangeMaker {

    /**
     * Makes change given an input amount and coin set.
     *
     * args[0]:   the amount to make change for
     * args[i>0]: the coins to make change from
     *            the first coin must be 1 and
     *            the coins must be in order
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Must specify an amount and at least 1 coin.");
        }
        int amount = Integer.parseInt(args[0]);
        int[] coins = new int[args.length - 1];
        for (int i=0; i<coins.length; i++) {
            coins[i] = Integer.parseInt(args[i + 1]);
            if (i == 0) {
                if (coins[i] != 1) {
                    throw new IllegalArgumentException("The first coin must be a 1 coin.");
                }
            } else {
                if (coins[i] <= coins[i-1]) {
                    throw new IllegalArgumentException("The coins must be in order.");
                }
            }
        }
        System.out.println(Arrays.toString(coins));
        int[] solutions = changeMaker(coins, amount);
        System.out.println(Arrays.toString(solutions));
    }

    /**
     * Makes change for the specified amount and a set of coins.
     * The coins must be specified in order, with the first being 1,
     * or the algorithm will not behave properly.
     * @param coins
     * @param amount
     * @return
     */
    public static int[] changeMaker(int[] coins, int amount) {
        // An array of ideal solutions
        // Each element in a solution array corresponds to the amount
        // of the corresponding coin that is in the actual solution
        int[][] solutions = new int[amount+1][coins.length];
        // An array of the ideal solution sizes
        int[] fewestCoins = new int[amount+1];
        for (int i=1; i<=amount; i++) {
            fewestCoins[i] = amount; // the most coins would be all 1's, so this is a safe upper bound
                                         // This second check stops the loop if the coin value exceeds the change amount
            for (int j=0; j<coins.length && coins[j]<=i; j++) {
                int val = coins[j];
                int coinCount = 1 + fewestCoins[i - val];
                if (coinCount < fewestCoins[i]) {
                    // update the ideal solution coin count
                    fewestCoins[i] = coinCount;
                    // copy the solution to the sub-problem
                    System.arraycopy(solutions[i - val], 0, solutions[i], 0, coins.length);
                    // increment the appropriate coin counter in the solution
                    solutions[i][j]++;
                }
            }
        }
        // print out the array of the solution sizes
        System.out.println(Arrays.toString(fewestCoins));
        return solutions[amount];
    }
}
