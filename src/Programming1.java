import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ari Weiland
 */
public class Programming1 {

    /**
     * The first arg specifies which problem to run.
     * 1 is the locker problem, 2 is the anagram problem.
     * Depending on which problem is selected, the args
     * vary. Problem 1 takes a single integer arg: the
     * amount of lockers to test. Problem 2 takes two
     * String args to test if they are anagrams or not.
     *
     * @param args
     */
    public static void main(String[] args) {
        int problemNumber = Integer.parseInt(args[0]);
        switch (problemNumber) {
            case 1:
                int lockerCount = Integer.parseInt(args[1]);
                lockers(lockerCount);
                break;
            case 2:
                String s1 = args[1];
                String s2 = args[2];
//                System.out.println(", : " + anagrams("", ""));
//                System.out.println(s1 + ", : " + anagrams(s1, ""));
                System.out.println(s1 + ", " + s2 + ": " + anagrams(s1, s2));
                break;
        }
    }

    /**
     * This method executes the locker problem (1.1.12) for a
     * given number of lockers. It prints the arrangement of
     * the lockers at the start, and after each iteration.
     *
     * The lockers left open are the prime lockers, because the
     * algorithm only leave open lockers with an odd number of
     * factors, and only primes have an odd number of factors.
     *
     * @param count the number of lockers and iterations
     */
    public static void lockers(int count) {
        boolean[] lockers = new boolean[count];
        Arrays.fill(lockers, false);                            // false = closed, true = open
        System.out.println("0:\t" + lockersToString(lockers));  // print the initial state
        for (int i=1; i<=count; i++) {                          // iteration loop
            for (int j=1; j<=count; j++) {                      // locker flipping loop
                if (j % i == 0) {                               // check whether j is a multiple of i
                    lockers[j-1] = !lockers[j-1];               // j-1 because lockers are indexed from 0
                }
            }
            System.out.println(i + ":\t" + lockersToString(lockers)); // print the iteration number
        }                                                             // followed by the locker string
    }

    public static String lockersToString(boolean[] lockers) {
        StringBuilder sb = new StringBuilder();
        for (boolean b : lockers) {
            sb.append(b ? "O" : "X").append("\t");
        }
        return sb.toString();
    }

    /**
     * This method returns true if the two input strings are
     * anagrams. It operates with O(n) time complexity.
     *
     * The algorithm first confirms that the two strings are
     * the same length. Then it goes through s1 and s2 and
     * counts the amount of each character with two Maps of
     * chars to ints. Finally, it returns true if the two
     * maps are equal.
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean anagrams(String s1, String s2) {
        if (s1.length() != s2.length()) { // lengths must be equal
            return false;
        }
        s1 = s1.toLowerCase(); // case should not affect anything
        s2 = s2.toLowerCase();
        Map<Character, Integer> s1chars = new HashMap<Character, Integer>(); // count the characters in each string
        Map<Character, Integer> s2chars = new HashMap<Character, Integer>();
        for (int i=0; i<s1.length(); i++) {
            char s1char = s1.charAt(i);         // get the ith character
            int s1count = 1;                    // count the character
            if (s1chars.containsKey(s1char)) {
                s1count += s1chars.get(s1char); // sum up the count
            }
            s1chars.put(s1char, s1count);       // store the character count

            char s2char = s2.charAt(i);         // repeat for the second string
            int s2count = 1;
            if (s2chars.containsKey(s2char)) {
                s2count += s2chars.get(s2char);
            }
            s2chars.put(s2char, s2count);
        }
        // if the maps are equal, each string contains the same
        // amount of each character, so they must be anagrams
        return s1chars.equals(s2chars);
    }
}
