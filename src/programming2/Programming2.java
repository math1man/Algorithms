package programming2;

/**
 * To test the algorithm, simply pass in a text body followed by a search string
 * as arguments to the main method. Note that if the body contains whitespace,
 * it must be enclosed in quotes.
 *
 * @author Ari Weiland
 */
public class Programming2 {

    public static void main(String[] args) {
        bruteForceStringMatch(args[0], args[1]);

        int j = -1;
        int k = 0;
        while (j == -1) {
            String random = "";
            for (int i = 0; i < 200; i++) {
                random += (char) (Math.random() * 26 + 97);
            }
            String search = "";
            for (int i = 0; i < 3; i++) {
                search += (char) (Math.random() * 26 + 97);
            }
            k++;
            j = bruteForceStringMatch(random, search);
        }
        System.out.println("Random test strings: " + k);
    }

    public static int bruteForceStringMatch(String text, String search) {
        int comparisons = 0;
        for (int i=0; i < text.length() - search.length(); i++) {
            System.out.println("Position " + i + ":");
            System.out.println(text);
            System.out.println(gap(i) + search);
            boolean match = true;
            for (int j=0; j < search.length() && match; j++) {
                match = search.charAt(j) == text.charAt(i + j);
                comparisons++;
                System.out.println(gap(i + j) + "^   " + (match ? "Match!" : "No match"));
            }
            if (match) {
                System.out.println("Pattern found at position " + i);
                System.out.println("Total comparisons: " + comparisons);
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a string of n spaces
     * @param n
     * @return
     */
    private static String gap(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<n; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
