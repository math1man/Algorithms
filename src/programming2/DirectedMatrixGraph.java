package programming2;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple implementation of a graph using an adjacency matrix.  This is an undirected, unweighted
 * graph, with no information associated with nodes in the graph.
 */
class DirectedMatrixGraph implements Graph {
    protected int[][] matrix;  // the adjacency matrix
    protected int nodes;    // the number of nodes in the graph

    /**
     * Constructor takes in the number of nodes, and initializes the adjacency matrix
     */
    public DirectedMatrixGraph(int nodes) {
        this.nodes = nodes;
        matrix = new int[nodes][nodes];
    }

    /**
     * Takes in two nodes, and adds an undirected edge between them.  It sets the corresponding positions
     * to 1.  It ignores bad input, doesn't raise an exception or tell anyone that the input was bad.
     */
    public void addEdge(int fromNode, int toNode) {
        if ((0 <= fromNode) && (fromNode < nodes) && (0 <= toNode) && (toNode < nodes)) {
            matrix[fromNode][toNode] = 1;
        }
    }


    /**
     * Returns the graph's size
     */
    public int getSize() {
        return nodes;
    }

    /**
     *
     */
    public List<Integer> getNeighbors(int node) {
        List<Integer> neighbors = new ArrayList<Integer>();
        if ((0 <= node) && (node < nodes)) {
            for (int i = 0; i < nodes; i++) {
                if (matrix[node][i] == 1) {
                    neighbors.add(i);
                }
            }
        }
        return neighbors;
    }

    /**
     * Takes in two nodes, and checks to see if an edge exists between them
     */
    public boolean areNeighbors(int node1, int node2) {
        return (0 <= node1) && (node1 < nodes) && (0 <= node2) && (node2 < nodes) && (matrix[node1][node2] == 1);
    }

    public static void main(String[] args) {
        // A test example
        // For an unweighted graph, just enter 0 for every weight value
        DirectedMatrixGraph sg = new DirectedMatrixGraph(4);
        sg.addEdge(0, 1);
        sg.addEdge(0, 2);
        sg.addEdge(0, 3);
        sg.addEdge(1, 2);
        sg.addEdge(1, 3);
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


    