package preceptor.hwa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Ari Weiland
 */
public class Scrabbilicious {

    public static final char[] LETTERS = "etaoinshrdlucmfwypvbgkjqxz".toCharArray();

    private WordList wordsList;

    public WordList getWordsList() {
        return wordsList;
    }

    private List<List<String>> wordsListByLength;

    public Scrabbilicious() {
    }

    public Scrabbilicious(String fileName) {
        readWordsList(fileName);
    }

    public void readWordsList(String fileName) {
        wordsList = new WordList();
        wordsListByLength = new ArrayList<List<String>>();
        for (int i=0; i<33; i++) {
            wordsListByLength.add(new ArrayList<String>());
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/preceptor/hwa/" + fileName));
            String line;
            while ((line = br.readLine()) != null) {
                String word = line.trim().toLowerCase();
                wordsList.add(word);
                wordsListByLength.get(word.length()).add(word);
            }
            br.close();
        } catch(IOException ignored) {}
    }

    public Set<StringWrapper> getExtensionWords(String word) {
        Set<StringWrapper> set = new HashSet<StringWrapper>();
        for (String s : wordsListByLength.get(word.length() + 1)) {
            if (shareLetters(word, s)) {
                set.add(new StringWrapper(s));
            }
        }
        return set;
    }

    public List<String> getExtensionSequence(String word) {
        return getExtensionSequence(word, new ArrayList<String>());
    }

    private List<String> getExtensionSequence(String initial, List<String> sequence) {
        sequence.add(initial);
        List<String> longest = sequence;
        Set<StringWrapper> extensions = getExtensionWords(initial); // k time
        for (StringWrapper sw : extensions) {
            List<String> sub = getExtensionSequence(sw.getString(), new ArrayList<String>(sequence));
            if (!sub.isEmpty() && sub.size() > longest.size()) {
                longest = sub;
            }
        }
//        if (longest.size() == sequence.size()) {
//            printCollection(sequence);
//        }
        return longest;
    }

    public Set<StringWrapper> getReductionWords(String word) {
        Set<StringWrapper> set = new HashSet<StringWrapper>();
        for (String s : wordsListByLength.get(word.length() - 1)) {
            if (shareLetters(word, s)) {
                set.add(new StringWrapper(s));
            }
        }
        return set;
    }

    public List<String> getReductionSequence(String word) {
        List<String> sequence = getReductionSequence(word, new ArrayList<String>());
        Collections.reverse(sequence);
        return sequence;
    }

    private List<String> getReductionSequence(String initial, List<String> sequence) {
        sequence.add(initial);
        List<String> longest = sequence;
        if (initial.length() > 3) {
            Set<StringWrapper> reductions = getReductionWords(initial); // k time
            for (StringWrapper sw : reductions) {
                List<String> sub = getReductionSequence(sw.getString(), new ArrayList<String>(sequence));
                if (!sub.isEmpty() && sub.size() > longest.size()) {
                    longest = sub;
                }
            }
        }
//        if (longest.size() == sequence.size()) {
//            printCollection(sequence);
//        }
        return longest;
    }

    public List<String> getLongestSequence() {
        for (int i = wordsListByLength.size(); i > 0; i--) {
            for (String word : wordsListByLength.get(i-1)) {
                List<String> sequence = getReductionSequence(word);
                if (sequence.get(0).length() == 3) {
                    return sequence;
//                } else if (sequence.size() > 1) {
//                    printCollection(sequence);
                }
            }
        }
        return new ArrayList<String>();
    }

    private char[] getLetterSet(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return chars;
    }

    public List<String> generateExtensionSequence(String word) {
        Map<StringWrapper, String> traces = new HashMap<StringWrapper, String>();
        List<String> words = Collections.singletonList(word);
        while (!words.isEmpty()) {                                      // lmax times
            List<String> newWords = new ArrayList<String>();
            for (String w : words) {                                    // n times
                for (char c : LETTERS) {                                // 26 times
                    if (wordsList.contains(w + c)) {                    // l*log(l) time
                        String newWord = wordsList.getAnagram(w + c);   // l*log(l) time
                        newWords.add(newWord);                          // constant time
                        traces.put(new StringWrapper(newWord), w);      // l*log(l) time
                    }
                }
            }
            words = newWords;
        }                                                               // O(n*l^2*log(l)) for n the size of dictionary and
                                                                        // l the length of the longest word (< 30)
        StringWrapper longest = new StringWrapper("");
        for (StringWrapper sw : traces.keySet()) {                      // n times
            if (sw.size() > longest.size()) {
                longest = sw;
            }
        }                                                               // O(n)
        List<String> sequence = new ArrayList<String>();                // O(1)
        while (traces.containsKey(longest)) {                           // l times
            sequence.add(longest.getString());                          // contsant time
            longest = new StringWrapper(traces.get(longest));           // l*log(l) time
        }                                                               // O(l^2*log(l))
        sequence.add(longest.getString());                              // O(1)
        Collections.reverse(sequence);                                  // O(l)
        return sequence;                                                // Net: O(n*l^2*log(l))
    }

    private static final HashMap<Character, Integer> CHAR_VALS = new HashMap<Character, Integer>();

    static {
        int[] primes = new int[26];
        int index = 0;
        int p = 2;
        while (index < 26) {
            boolean isPrime = true;
            for (int i=0; i<index; i++) {
                if (p % primes[i] == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                primes[index] = p;
                CHAR_VALS.put(LETTERS[index], p);
                index++;
            }
            p++;
        }
    }

    /**
     * There are 1379 words in WORDS.txt that suffer integer overflow with this method.
     * @param word
     * @return
     */
    public Long getValue(String word) {
        long v = 1;
        for (char c : word.toCharArray()) {
            v *= CHAR_VALS.get(c);
            if (v < 1) {
                System.out.println("--> Overflow on \"" + word + "\" at letter \'" + c + "\'. Word length: " + word.length());
                break;
            }
        }
        return v;
    }

    /**
     * Returns true if the longer of the two strings contains
     * all the letters of the shorter string.  If the strings
     * are the same length, returns true if they are anagrams
     *
     * @param s1
     * @param s2
     * @return
     */
    public boolean shareLetters(String s1, String s2) {
        char[] c1, c2;
        if (s1.length() < s2.length()) {
            c1 = s1.toCharArray();
            c2 = s2.toCharArray();
        } else {
            c1 = s2.toCharArray();
            c2 = s1.toCharArray();
        }
        Arrays.sort(c1);
        Arrays.sort(c2);
        int i = 0;
        for (int j=0; i < c1.length && j < c2.length; j++) {
            if (c1[i] == c2[j]) {
                i++;
            }
        }
        return i == c1.length;
    }

    public static void printCollection(Collection collection) {
        for (Object s : collection) {
            System.out.println(s);
        }
        System.out.println("==> " + collection.size() + "\n");
    }

    public static void main(String[] args) {
        Scrabbilicious scrabbilicious = new Scrabbilicious("WORDS.txt");

//        Collection sequence = scrabbilicious.getExtensionSequence("tab");
//        Collection sequence = scrabbilicious.getReductionSequence("indeterminations");
//        Collection sequence = scrabbilicious.getReductionWords("rejackets");
        Collection sequence = scrabbilicious.getLongestSequence();

//        printCollection(sequence);

        /*
        hat - 13 (15 letters)
        eath
        earth
        earths
        anthers
        hairnets
        tarnishes
        trashiness
        swarthiness
        waterishness
        swarthinesses
        waterishnesses
        seaworthinesses

        eon - 14 (16 letters)
        note
        monte
        omenta
        montane
        montanes
        nominates
        antimonies
        inseminator
        terminations
        antimodernist
        determinations
        intermediations
        indeterminations

        HUGE_WORDS list:
        nnn - 15 (17 letters)
        innn
        iinnn
        innitn
        ntitinn
        tinnient
        tintinner
        intinerant
        internation
        internationa
        interantional
        internationali
        internalization
        internationalize
        internationalized
         */

        /* Useful words:
         1) vox, wud, zax
         2) jaw, jay, jew, jig, job, jog, vex, wax, yay, zap
         3) jow, jun, vav, waw, wiz, zek
         4) jab, jam, vug
         5) jar, jib, jin, joy
         6) jag, jug, jot, zag
         7) jee, suq, xis, yuk, zig
         8) axe, jet, jeu, jus, jut
          */

        String test = "par";
        double timeFactor = 1000;

        long start = System.currentTimeMillis();
        sequence = scrabbilicious.generateExtensionSequence(test);
        long end = System.currentTimeMillis();
        printCollection(sequence);
//        System.out.println(sequence.size());
        System.out.println("-----> Elapsed time: " + ((end - start) / timeFactor) + "\n");

//        start = System.currentTimeMillis();
//        sequence = scrabbilicious.generateExtensionSequence(tes);
//        end = System.currentTimeMillis();
//        printCollection(sequence);
////        System.out.println(sequence.size());
//        System.out.println("-----> Elapsed time: " + ((end - start) / timeFactor) + "\n");
    }
}
