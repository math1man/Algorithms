package preceptor.hwb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Ari Weiland
 */
public class FourLetterFrenzy {

    private final List<String> wordsList = new ArrayList<String>();
    private final Map<String, Set<String>> map = new HashMap<String, Set<String>>();

    public FourLetterFrenzy(String fileName) {
        readWordsList(fileName);
        buildMap();
    }

    public void readWordsList(String fileName) {
        wordsList.clear();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/preceptor/hwb/" + fileName));
            String line;
            while ((line = br.readLine()) != null) {
                String word = line.trim().toUpperCase();
                wordsList.add(word);
            }
            br.close();
        } catch(IOException ignored) {}
    }

    /**
     * Builds a map from the wordsList where each string maps to a set of strings 1 letter different from it.
     * Runs in O(n^2) time because it must compare each word to every other word.
     */
    public void buildMap() {
        map.clear();
        for (int i=0; i<wordsList.size(); i++) {
            for (int j=i+1; j<wordsList.size(); j++) {
                String w1 = wordsList.get(i);
                String w2 = wordsList.get(j);
                if (compareWords(w1, w2)) {
                    appendToMap(w1, w2);
                    appendToMap(w2, w1);
                }
            }
        }
    }

    /**
     * Appends the secondary string to the set of strings associated with the primary in the map.
     * Runs in O(1) constant time.
     * @param primary
     * @param secondary
     */
    private void appendToMap(String primary, String secondary) {
        if (!map.containsKey(primary)) {
            map.put(primary, new HashSet<String>());
        }
        map.get(primary).add(secondary);
    }

    /**
     * Checks whether the two words differ by exactly 1 character.
     * Returns false if they are different length, or if they are the same string.
     * Runs in O(l) = O(4) = O(1) constant time.
     * @param w1
     * @param w2
     * @return
     */
    public boolean compareWords(String w1, String w2) {
        if (w1.length() != w2.length()) {
            return false;
        }
        boolean foundDifference = false;
        for (int i=0; i<w1.length(); i++) {
            if (w1.charAt(i) != w2.charAt(i)) {
                if (!foundDifference) {
                    foundDifference = true;
                } else {
                    return false;
                }
            }
        }
        return foundDifference;
    }

    /**
     * Finds a sequence from the start string to the end string using BFS on the string map generated above.
     * Runs in O(m*n^2) time where m is the length of the sequence, and n is the number of words
     * @param start
     * @param end
     * @return
     */
    public List<String> sequenceSearch(String start, String end) {
        start = start.toUpperCase();
        end = end.toUpperCase();
        List<String> sequence = new LinkedList<String>();
        sequence.add(start);
        Queue<QueueNode> bfsQueue = new ArrayDeque<QueueNode>();                // BFS Queue for gathering nodes
        Map<String, String> visited = new HashMap<String, String>();                 // Map of visited nodes to their predecessors
        if (start.equals(end)) {                                        // Handle trivial cases
            return sequence;
        } else if (compareWords(start, end)) {
            sequence.add(end);
            return sequence;
        } else {
            visited.put(start, null);                                   // make sure not to revisit the start node
            if (map.containsKey(start)) {                               // initialize the queue: O(n) time for n the number of words
                for (String next : map.get(start)) {
                    visited.put(next, start);                           // O(1)
                    bfsQueue.add(new QueueNode(next, start));           // O(1)
                }
            }
            while (!bfsQueue.isEmpty()) {                               // run BFS: max n iterations (each node can be visited at most once)
                QueueNode node = bfsQueue.poll();                       // O(1)
                String string = node.getString();
                if (string.equals(end)) {                               // check to see if we are done
                    while (!string.equals(start)) {                     // build the sequence: O(m) time for m the length of the sequence
                        sequence.add(1, string);                        // O(1) time for a LinkedList
                        string = visited.get(string);                   // O(1)
                    }
                    System.out.println(visited.size());
                    return sequence;
                } else {                                                // add more states to the queue: O(n) time
                    for (String next : map.get(string)) {
                        if (!visited.containsKey(next)) {
                            visited.put(next, string);                  // O(1)
                            bfsQueue.add(new QueueNode(next, string));  // O(1)
                        }
                    }
                }
            }
            System.out.println(visited.size());
            return null;
        }
    }

    /**
     * This helper method cleans out a sequence by removing redundant entries.
     * For example, the sequence (BOAT, GOAT, MOAT, COAT, CHAT) would get
     * simplified to (BOAT, COAT, CHAT)
     * Runs in O(m) time for m the length of sequence
     * @param sequence
     * @return
     */
    private List<String> cleanSequence(List<String> sequence) {
        if (sequence.size() < 2) {                          // handle awkward edge cases
            return sequence;
        }
        List<String> clean = new ArrayList<String>();
        String last = null;                                         // last element added to clean list
        String test = null;                                         // test element
        for (String next : sequence) {
            if (last == null) {                                     // only occurs for the first element in sequence
                clean.add(next);                                    // add the first element
                last = next;                                        // set the last element added to the list
            } else {
                if (test != null && !compareWords(last, next)) {    // if test is set and should not be skipped
                    clean.add(test);                                // add it to the clean list
                    last = test;                                    // shift last because we added a new element
                }
                test = next;                                        // set next test element
            }
        }
        clean.add(test);                                            // always add the final element
        return clean;
    }

    /**
     * Of the 5526 words in the given list, 5450 of them are in one orbit. Of the remaining 76 words, there
     * are two orbits of size 3, five orbits of size 2, and sixty orbits of size 1.
     * 
     * Orbit Size 3:
     * AMBO, AMMO, UMBO
     * ETUI, PFUI, PTUI
     * 
     * Orbit Size 2:
     * AWOL, AWDL
     * ESPY, ESKY
     * ISBA, ISNA
     * ORYX, ONYX
     * UPSY, UPBY
     * 
     * Orbit Size 1:
     * ABRI, ACAI, ADAW, ADZE, AESC, AHOY, ANKH, ASCI, AZYM, DJIN, DZHO, ECRU, EEVN, EKKA, ELHI,
     * ENUF, ENVY, EPEE, EPHA, ERHU, EUOI, EVIL, EXAM, EXPO, EXUL, GYNY, HUHU, HWYL, IMAM, ISIT,
     * JEHU, JEUX, KHOR, KIWI, LWEI, MWAH, MYXO, MZEE, NGAI, OCCY, ODOR, ODSO, OGAM, OMBU, OMOV,
     * OPPO, OSSA, OVUM, PFFT, PRUH, UGHS, UPTA, VULN, WAAC, YEBO, YGOE, YUNX, ZOIC, ZYGA, ZZZS
     *
     * @return
     */
    public Map<String, Set<String>> analyzeOrbits() {
        Set<String> visited = new HashSet<String>();
        Map<String, Set<String>> orbits = new HashMap<String, Set<String>>();
        Queue<String> queue = new ArrayDeque<String>();
        for (String s : wordsList) {
            if (!visited.contains(s) && queue.isEmpty()) {
                queue.add(s);
                Set<String> set = new HashSet<String>();
                while (!queue.isEmpty()) {
                    String string = queue.poll();
                    visited.add(string);
                    set.add(string);
                    if (map.containsKey(string)) {
                        for (String next : map.get(string)) {
                            if (!visited.contains(next)) {
                                queue.add(next);
                            }
                        }
                    }
                }
                orbits.put(s, set);
                if (set.size() > wordsList.size() / 2) {
                    System.out.println("Big Orbit:");
                    System.out.println("==> " + set.size() + "\n");
                } else {
                    printCollection(set);
                }
            }
        }
        return orbits;
    }

    public static void printCollection(Collection collection) {
        if (collection != null) {
            for (Object s : collection) {
                System.out.println(s);
            }
            System.out.println("==> " + collection.size() + "\n");
        } else {
            System.out.println("==> Failure!");
        }
    }

    public static void main(String[] args) {
        FourLetterFrenzy flf = new FourLetterFrenzy("fourletterwords.txt");
        printCollection(flf.sequenceSearch("jynx", "zurf"));
//        flf.analyzeOrbits();
    }
}
