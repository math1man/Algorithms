package programming2;

import java.util.List;

/**
 * A simple implementation of a graph using an adjacency matrix.  This is an undirected, unweighted
 * graph, with no information associated with nodes in the graph.
 */
public class MatrixGraph extends DirectedMatrixGraph {

    /**
     * Constructor takes in the number of nodes, and initializes the adjacency matrix
     */
    public MatrixGraph(int nodes) {
        super(nodes);
    }

    /**
     * Takes in two nodes, and adds an undirected edge between them.  It sets the corresponding positions
     * to 1.  It ignores bad input, doesn't raise an exception or tell anyone that the input was bad.
     */
    public void addEdge(int fromNode, int toNode) {
        super.addEdge(fromNode, toNode);
        super.addEdge(toNode, fromNode);
    }

    public static void main(String[] args) {
        // A test example
        MatrixGraph sg = new MatrixGraph(4);
        sg.addEdge(0, 1);
        sg.addEdge(0, 2);
        sg.addEdge(0, 3);
        sg.addEdge(1, 2);
        // sg.addEdge(1, 3);
        sg.addEdge(2, 3);

        int n = sg.getSize();
        for (int i = 0; i < n; i++) {
            System.out.println(sg.areNeighbors(i, 3));
            List<Integer> neighbors = sg.getNeighbors(i);
            for (int j = 0; j < n; j++) {
                System.out.print(neighbors.get(j) + " ");
            }
            System.out.println();
        }
    }
}
