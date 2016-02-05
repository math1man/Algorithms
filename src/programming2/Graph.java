package programming2;

import java.util.List;

/**
 * @author Ari Weiland
 */
public interface Graph {

    /**
     * Takes in two nodes, and adds an undirected edge between them.  It sets the corresponding positions
     * to 1.  It ignores bad input, doesn't raise an exception or tell anyone that the input was bad.
     */
    void addEdge(int fromNode, int toNode);

    /**
     * Returns the graph's size
     */
    int getSize();

    /**
     * Takes in a node number and returns the list of its neighbors
     */
    List<Integer> getNeighbors(int node);

    /**
     * Takes in two nodes, and checks to see if an edge exists between them
     */
    boolean areNeighbors(int node1, int node2);
}
