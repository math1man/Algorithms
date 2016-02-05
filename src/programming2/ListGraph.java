package programming2;

import java.util.*;


/**
 * A simple implementation of a graph using an adjacency list.  This is an undirected, unweighted
 * graph, with no information associated with nodes in the graph.
 */
class ListGraph implements Graph {
    // I use sets instead of lists because there should never be more than one edge between nodes (for our purposes)
    protected List<Set<Integer>> adjacencies;  // the adjacency list
    protected int nodes;    // the number of nodes in the graph

    /**
     * Constructor takes in the number of nodes, and initializes the adjacency matrix
     */
    public ListGraph(int nodes) {
        this.nodes = nodes;
        adjacencies = new ArrayList<Set<Integer>>();
        for (int i = 0; i < nodes; i++) {
            Set<Integer> newList = new TreeSet<Integer>();
            adjacencies.add(newList);
        }
    }

    @Override
    public void addEdge(int fromNode, int toNode) {
        if ((0 <= fromNode) && (fromNode < nodes) && (0 <= toNode) && (toNode < nodes)) {
            adjacencies.get(fromNode).add(toNode);
            adjacencies.get(toNode).add(fromNode);
        }
    }


    @Override
    public int getSize() {
        return nodes;
    }

    @Override
    public List<Integer> getNeighbors(int node) {
        return new ArrayList(adjacencies.get(node));
    }


    @Override
    public boolean areNeighbors(int node1, int node2) {
        if ((0 <= node1) && (node1 < nodes) && (0 <= node2) && (node2 < nodes)) {
            Set<Integer> neighbors = adjacencies.get(node1);
            return neighbors.contains(node2);
        }
        return false;
    }



    public static void main(String[] args) {
        // A test example
        ListGraph sg = new ListGraph(4);
        sg.addEdge(0, 1);
        sg.addEdge(0, 2);
        sg.addEdge(0, 3);
        sg.addEdge(1, 2);
        // sg.addEdge(1, 3);
        sg.addEdge(2, 3);

        int n = sg.getSize();
        for (int i = 0; i < n; i++) {
            System.out.println("Node " + i + ":");
            System.out.println("is a neighbor of node 3:" + sg.areNeighbors(i, 3));
            List neighbors = sg.getNeighbors(i);
            for (Object neighbor : neighbors) {
                System.out.print(neighbor + " ");
            }
            System.out.println();
        }
    }
}
