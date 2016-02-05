package preceptor.hwb;

/**
 * @author Ari Weiland
 */
public class QueueNode {

    private final String string;
    private final String previous;

    public QueueNode(String string, String previous) {
        this.string = string;
        this.previous = previous;
    }

    public String getString() {
        return string;
    }

    public String getPrevious() {
        return previous;
    }
}
