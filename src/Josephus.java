import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author Ari Weiland
 */
public class Josephus {

    public static void main(String[] args) {
        for (int i=1; i<=200; i++) {
            System.out.println(i + ": " + josephus(i) + " survives!");
        }
    }

    public static int josephus(int n) {
        Queue<Integer> queue = new ArrayDeque<Integer>();
        for (int i=1; i<=n; i++) {
            queue.add(i);
        }
        boolean kill = false;
        int person = 0;
        while (!queue.isEmpty()) {
            person = queue.poll();
            if (!kill) {
                queue.add(person);
            }
            kill = !kill;
        }
        return person;
    }
}
