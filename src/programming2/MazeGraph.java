package programming2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * File: MazeGraph.java
 * Author: Ari Weiland, Devin Bjelland; based on MazeGraph.py by Susan Fox
 * Date: September 2014
 *
 * Contains a program to read in mazes from a file. It assumes that the file has
 * no blank lines. Each line in the file represents a row of the maze. Each line
 * must be the same length: the number of columns in the maze. Each character
 * represents a grid square. Possible characters are either space for an open
 * square, X for a wall square, S for the starting point, or G for the goal.
 * This program then constructs an undirected graph to represent the maze
 *
 * @author Ari Weiland
 */
public class MazeGraph {

    public static class Position {
        public int x;
        public int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Position)) return false;

            Position position = (Position) o;

            return x == position.x && y == position.y;

        }
    }

    /**
     * This class holds the information that is passed to the DFS and BFS algorithms.
     * It just contains the startNode, goalNode and the graph, represented with adjacency lists.
     */
    public static class ProcessedGraph {
        public int startNode;
        public int goalNode;
        public Graph graph;

        public ProcessedGraph(int startNode, int goalNode, Graph graph) {
            this.startNode = startNode;
            this.goalNode = goalNode;
            this.graph = graph;
        }
    }

    public static class ProcessedMaze {
        public int startNode;
        public int goalNode;
        public List<Position> openSquares;

        public ProcessedMaze(int startNode, int goalNode, List<Position> openSquares) {
            this.startNode = startNode;
            this.goalNode = goalNode;
            this.openSquares = openSquares;
        }
    }

    /**
     * Takes in a filename and reads the maze found there. Mazes are a series
     * of lines. It is assumed that the outline of the maze will all be filled in;
     * any characters before the first X or after the last X are discarded.  It builds
     * a list whose contents are each row of the maze. Each row is represented as a string.
     */
    public static List<String> readMaze(String filename) {
        List<String> result = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/programming2/mazes/" + filename));
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line.trim());
            }
            br.close();
        } catch(IOException ignored) {}
        return result;
    }

    /**
     * Takes in the maze represented as a list of strings (one string per row), and
     * it prints the maze as is.
     */
    public static void printMaze(List<String> mazelist) {
        for(String line : mazelist) {
            System.out.println(line);
        }
        System.out.println();
    }

    /**
     * Every open square in the maze becomes a node in the graph. It will have
     * an edge to every open square that is immediately to the left, right,
     * above, or below it. First the program must determine all the open squares,
     * giving each a (row, col) coordinate. The node number for each square
     * corresponds to the position of its coordinates in the open square list.
     */
    public static ProcessedGraph mazeToGraph(List<String> mazelist) {
        ProcessedMaze maze = collectOpenSquares(mazelist);
        if (maze != null) {
            int numOpen = maze.openSquares.size();
            Graph mazegraph = new ListGraph(numOpen);

            for (int i = 0; i < numOpen; i++) {
                int x = maze.openSquares.get(i).x;
                int y = maze.openSquares.get(i).y;
                for (int j = -1; j <= 1; j += 2) {
                    int k = 0;
                    Position neigh = new Position(x+j, y+k);
                    if (maze.openSquares.contains(neigh)) {
                        int neighIdx = maze.openSquares.lastIndexOf(neigh);
                        mazegraph.addEdge(i, neighIdx);
                    }
                }
                for (int k = -1; k <= 1; k += 2) {
                    int j = 0;
                    Position neigh = new Position(x+j, y+k);
                    if (maze.openSquares.contains(neigh)) {
                        int neighIdx = maze.openSquares.lastIndexOf(neigh);
                        mazegraph.addEdge(i, neighIdx);
                    }
                }

            }
            return new ProcessedGraph(maze.startNode, maze.goalNode, mazegraph);
        } else {
            return null;
        }
    }

    public static ProcessedMaze collectOpenSquares(List<String> mazelist) {
        List<Position> openList = new ArrayList<Position>();
        int startPos = -1;
        int goalPos = -1;
        for (int row = 0; row < mazelist.size(); row++) {
            String rowString = mazelist.get(row).toUpperCase();
            for (int col = 0; col < rowString.length(); col++) {
                char val = rowString.charAt(col);
                if (val != 'X') {
                    if (val == 'S') {
                        startPos = openList.size();
                    } else if (val == 'G') {
                        goalPos = openList.size();
                    }
                    openList.add(new Position(row, col));
                }
            }
        }
        if (startPos != -1 && goalPos != -1) {
            return (new ProcessedMaze(startPos, goalPos, openList));
        } else {
            return null;
        }
    }

    /**
     * This is a utility function. It counts the open spaces in the same way as collectOpenSquares,
     * but it creates a copy of the maze list of strings, with every open space replaced by a number
     * that represents its node number. To keep it compact, only the ones-place digit is stored;
     * you can deduce the rest of the number from context. At the end of each line, it prints the
     * last node number occurring on that line, and the node numbers for the start and goal if found.
     */
    public static List<String> nodeMarkedMaze(List<String> unmarkedMaze) {
        int count = 0;
        List<String> newMazeList = new ArrayList<String>();
        for (String row : unmarkedMaze) {
            int startFound = -1;
            int goalFound = -1;
            String newrow = "";
            for (int i = 0; i < row.length(); i++) {
                char sqVal = Character.toUpperCase(row.charAt(i));
                String val = "" + sqVal;
                if (sqVal != 'X') {
                    if (sqVal == 'S') {
                        startFound = count;
                    } else if (sqVal == 'G') {
                        goalFound = count;
                    } else {
                        val = String.valueOf(count % 10);
                    }
                    count++;
                }
                newrow = newrow + val;
            }
            newrow = newrow + "     " + (count-1);
            if (startFound != -1) {
                newrow = newrow + "    Start at " + startFound;
            }
            if (goalFound != -1) {
                newrow = newrow + "    Goal at " + goalFound;
            }
            newMazeList.add(newrow);
        }
        return newMazeList;
    }

    /**
     * Takes in a ProcessedGraph object which contains a graph represented as an adjacency list,
     * the node number for the starting point, and for the goal point. It computes and returns a
     * path from start to goal (if one exists), using the Depth-First Seach algorithm. The search
     * starts from the startNode, and continues only until the goalNode is reached. If there is no
     * path from start to goal then an empty list is returned.
     *
     * The DFS goes towards nodes with smaller indices over nodes with larger ones first. This
     * means its direction preference, in decreasing order, is up, left, right, down. It has a
     * habit of snaking through large open spaces, leading to unnecessarily long paths.  It does
     * however often have to visit fewer nodes than BFS, especially if goal node index is smaller
     * than the start node index.
     */
    public static List<Integer> dfs(ProcessedGraph processedGraph) {
        Graph graph = processedGraph.graph;
        boolean[] marked = new boolean[graph.getSize()];
        Arrays.fill(marked, false);
        List<Integer> path = dfsHelper(processedGraph.startNode, processedGraph.goalNode, graph, marked);
        System.out.println("Number visited: " + countMarked(marked));
        return path;
    }

    /**
     * This helper method actually runs the recursive DFS.
     * The graph is passed through to define structure, and is unchanged.
     * At each iteration, the current node is marked (if not already marked)
     * @param currentNode
     * @param goalNode
     * @param graph
     * @param marked
     * @return
     */
    private static List<Integer> dfsHelper(int currentNode, int goalNode, Graph graph, boolean[] marked) {
        if (marked[currentNode]) {
            // We found a node that already was visited, so stop
            return new ArrayList<Integer>();
        } else {
            marked[currentNode] = true;
            List<Integer> path = new ArrayList<Integer>();
            if (currentNode == goalNode) {
                // If we find the node, start building the path
                path.add(currentNode);
            } else {
                for (int i : graph.getNeighbors(currentNode)) {
                    // Recurse on the next node
                    path = dfsHelper(i, goalNode, graph, marked);
                    // If path is not empty, we must have found the goal
                    if (!path.isEmpty()) {
                        // Therefore, keep building the path
                        path.add(0, currentNode);
                        // and stop iterating
                        return path;
                    }
                }
            }
            return path;
        }
    }

    /**
     * This DFS algorithm is a mirror of the BFS algorithm, except using a stack
     * instead of a queue. It is non-recursive, unlike the other DFS algorithm.
     * Note that I also reversed the order in which nodes are added to the stack.
     * This is so that they come off in the same order they might have come out
     * of a queue. This maintains the direction preference of the algorithm so it
     * is more easily compared to the other algorithms.
     * @param processedGraph
     * @return
     */
    public static List<Integer> dfs2(ProcessedGraph processedGraph) {
        Graph graph = processedGraph.graph;
        boolean[] marked = new boolean[graph.getSize()];
        Arrays.fill(marked, false);
        // Queue for the BFS algorithm
        Stack<Integer> stack = new Stack<Integer>();
        // Array to keep track of the parents of each node in the BFS.
        // It allows for the ability to backtrack from the goal to the
        // start if and when the goal node is found.
        int[] parents = new int[graph.getSize()];
        Arrays.fill(parents, -1);
        int startNode = processedGraph.startNode;
        int goalNode = processedGraph.goalNode;
        stack.push(startNode);
        marked[startNode] = true;
        while (!stack.isEmpty()) {
//            System.out.println(stack);
            int currentNode = stack.pop();
            if (currentNode == goalNode) {
                List<Integer> path = new ArrayList<Integer>();
                while (currentNode != startNode) {
                    path.add(0, currentNode);
                    currentNode = parents[currentNode];
                }
                path.add(0, startNode);
                System.out.println("Number visited: " + countMarked(marked));
                return path;
            } else {
                List<Integer> neighbors = graph.getNeighbors(currentNode);
                Collections.reverse(neighbors);
                for (int i : neighbors) {
                    if (!marked[i]) {
                        marked[i] = true;
                        stack.push(i);
                        parents[i] = currentNode;
                    }
                }
            }
        }
        System.out.println("Number visited: " + countMarked(marked));
        return new ArrayList<Integer>();    }

    /**
     * Takes in a ProcessedGraph object which contains a graph represented as an adjacency list,
     * the node number for the starting point, and for the goal point. It computes and returns a
     * path from start to goal (if one exists), using the Breadth-First Seach algorithm. The search
     * starts from the startNode, and continues only until the goalNode is reached. If there is no
     * path from start to goal then an empty list is returned.
     *
     * The BFS also goes towards nodes with smaller indices over nodes with larger ones, but because
     * it finds the shortest possible path, this only matters in cases where there are multiple
     * paths of the same smallest length. Thus, it finds the shortest path whose sum of its node
     * indices is also smallest. It will always find the shortest path, so it will always be the
     * same length or shorter than DFS. It does usually visit more nodes than DFS, however.
     */
    public static List<Integer> bfs(ProcessedGraph processedGraph) {
        Graph graph = processedGraph.graph;
        boolean[] marked = new boolean[graph.getSize()];
        Arrays.fill(marked, false);
        // Queue for the BFS algorithm
        Queue<Integer> queue = new LinkedBlockingQueue<Integer>();
        // Array to keep track of the parents of each node in the BFS.
        // It allows for the ability to backtrack from the goal to the
        // start if and when the goal node is found.
        int[] parents = new int[graph.getSize()];
        Arrays.fill(parents, -1);
        int startNode = processedGraph.startNode;
        int goalNode = processedGraph.goalNode;
        queue.add(startNode);
        marked[startNode] = true;
        while (!queue.isEmpty()) {
//            System.out.println(queue);
            int currentNode = queue.remove();
            if (currentNode == goalNode) {
                List<Integer> path = new ArrayList<Integer>();
                while (currentNode != startNode) {
                    path.add(0, currentNode);
                    currentNode = parents[currentNode];
                }
                path.add(0, startNode);
                System.out.println("Number visited: " + countMarked(marked));
                return path;
            } else {
                for (int i : graph.getNeighbors(currentNode)) {
                    if (!marked[i]) {
                        marked[i] = true;
                        queue.add(i);
                        parents[i] = currentNode;
                    }
                }
            }
        }
        System.out.println("Number visited: " + countMarked(marked));
        return new ArrayList<Integer>();
    }

    private static int countMarked(boolean[] marked) {
        int count = 0;
        for (boolean b : marked) {
            count += b ? 1 : 0;
        }
        return count;
    }

    /**
     * Takes a filename as input. It reads the maze from that file, and
     * prints it. You can print the node-marked version instead by uncommenting
     * the next lines. Next it converts the maze to be a graph, returning the
     * graph object and the node numbers of the start and goal nodes. Once you
     * have define BFS and DFS, uncomment these lines to test and print the
     * result.
     */
    public static void testMaze(String mazeFile) {
        List<String> unprocessedMaze = readMaze(mazeFile);
        printMaze(unprocessedMaze);
        ProcessedGraph processedGraph = mazeToGraph(unprocessedMaze);
        List<String> mazeCopy = nodeMarkedMaze(unprocessedMaze);
        printMaze(mazeCopy);
        System.out.println("StartNode=" + processedGraph.startNode + " GoalNode=" + processedGraph.goalNode);
        double repetitions = 1;

        System.out.println("DFS:");
        long start = System.currentTimeMillis();
        List<Integer> path = new ArrayList<Integer>();
        for (int i=0; i<repetitions; i++) {
            path = dfs(processedGraph);
        }
        double elapsed = (System.currentTimeMillis() - start) / repetitions;
        System.out.println(path);
        System.out.println("Path length: " + path.size());
        System.out.println("Elapsed Time: " + elapsed);
        System.out.println();

//        System.out.println("DFS2:");
//        start = System.currentTimeMillis();
//        for (int i=0; i<repetitions; i++) {
//            path = dfs2(processedGraph);
//        }
//        elapsed = (System.currentTimeMillis() - start) / repetitions;
//        System.out.println(path);
//        System.out.println("Path length: " + path.size());
//        System.out.println("Elapsed Time: " + elapsed);
//        System.out.println();
//
        System.out.println("BFS:");
        start = System.currentTimeMillis();
        for (int i=0; i<repetitions; i++) {
            path = bfs(processedGraph);
        }
        elapsed = (System.currentTimeMillis() - start) / repetitions;
        System.out.println(path);
        System.out.println("Path length: " + path.size());
        System.out.println("Elapsed Time: " + elapsed);
        System.out.println();
    }

    /**
     * To test a maze, simply include the file name as an argument.
     * The maze must be in the "mazes" folder in this package.     M
     *
     * Mazes available:
     *  - maze1.txt
     *  - maze2.txt
     *  - maze3.txt
     *  - maze4.txt
     *  - maze5.txt (same as 3 except S and G reversed)
     *  - maze6.txt
     *  - maze7.txt
     *  - maze7r.txt (same as 7 except S and G reversed)
     *  - maze7d.txt (same as 7 except across the other diagonal)
     *  - maze7dr.txt (reverse of 7d)
     *  - maze8.txt
     * @param args
     */
    public static void main(String args[]) {
        for (String s : args) {
            testMaze(s);
        }

//        testMaze("maze1.txt");
//        testMaze("maze2.txt");
//        testMaze("maze3.txt");
//        testMaze("maze4.txt");
//        testMaze("maze5.txt");
//        testMaze("maze6.txt");
//        testMaze("maze7.txt");
//        testMaze("maze7.txt");
//        testMaze("maze7r.txt");
//        testMaze("maze7d.txt");
//        testMaze("maze7dr.txt");
//        testMaze("maze8.txt");
    }
}
