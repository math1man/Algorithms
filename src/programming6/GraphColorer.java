package programming6;

import programming2.Graph;
import programming2.MatrixGraph;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class GraphColorer {

    /**
     * Colors every node in a graph one of n different colors.
     * If the graph cannot be colored with n colors, returns null. Colors
     * are represented by integers from 0 to n-1. The coloring is represented
     * by an array of these integers, where the index is the node.
     * @param graph
     * @param n
     * @return
     */
    public static int[] color(Graph graph, int n) {
        int size = graph.getSize();
        int[] coloring = new int[size];
        Arrays.fill(coloring, -1);
        for (int i=0; i<size; i++) {
            if (coloring[i] == -1) {
                if (!colorRecur(graph, n, coloring, i)) {
                    return null;
                }
            }
        }
        return coloring;
    }

    /**
     * Recursively colors the nodes in a graph via a DFS backtracking method.
     * If a coloring at the given node yields an uncolorable state, it will revert that
     * coloring and return false so that a higher call knows to try a different color.
     * @param graph
     * @param n
     * @param coloring
     * @param node
     * @return
     */
    public static boolean colorRecur(Graph graph, int n, int[] coloring, int node) {
        int size = graph.getSize();
        boolean[] colors = new boolean[n];
        Arrays.fill(colors, true);
        List<Integer> neighbors = graph.getNeighbors(node);
        for (int i : neighbors) {
            int color = coloring[i];
            if (color > -1) {
                colors[color] = false;
            }
        }
        int[] oldColoring = new int[size];
        System.arraycopy(coloring, 0, oldColoring, 0, size);
        boolean valid = false;
        for (int j=0; j<n && !valid; j++) {
            valid = colors[j];
            coloring[node] = j;
            for (int i=0; i<neighbors.size() && valid; i++) {
                // recurse on neighbors in a DFS manner
                int neighbor = neighbors.get(i);
                valid = (coloring[neighbor] > -1 || colorRecur(graph, n, coloring, neighbor));
            }
            if (!valid) {
                System.arraycopy(oldColoring, 0, coloring, 0, size);
            }
        }
        return valid;
    }

    public static void main(String[] args) {
        Graph graph = new MatrixGraph(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 5);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);
        graph.addEdge(4, 5);
        System.out.println(Arrays.toString(color(graph, 3)));
        Graph midwest = buildMidwestGraph();
        System.out.println(Arrays.toString(color(midwest, 3)));
        Graph macGraph = buildMacGraph();
        System.out.println(Arrays.toString(color(macGraph, 4)));
        System.out.println(Arrays.toString(color(macGraph, 3)));
        System.out.println(Arrays.toString(color(macGraph, 2)));

        Graph fourGraph = buildCompleteGraph(4);
        System.out.println(Arrays.toString(color(fourGraph, 3)));
        System.out.println(Arrays.toString(color(fourGraph, 4)));
    }

    private static Graph buildCompleteGraph(int size) {
        Graph full = new MatrixGraph(size);
        for (int i=0; i<size; i++) {
            for (int j=i+1; j<size; j++) {
                full.addEdge(i, j);
            }
        }
        return full;
    }

    private static Graph buildMidwestGraph() {
        Graph midwest = new MatrixGraph(9);
        midwest.addEdge(0, 1); // 0 = MN
        midwest.addEdge(1, 2); // 1 = IA
        midwest.addEdge(0, 3); // 2 = MO
        midwest.addEdge(1, 3); // 3 = WI
        midwest.addEdge(1, 4); // 4 = IL
        midwest.addEdge(2, 4); // 5 = MI
        midwest.addEdge(3, 4); // 6 = IN
        midwest.addEdge(3, 5); // 7 = OH
        midwest.addEdge(4, 6); // 8 = KY
        midwest.addEdge(5, 6);
        midwest.addEdge(5, 7);
        midwest.addEdge(6, 7);
        midwest.addEdge(2, 8);
        midwest.addEdge(4, 8);
        midwest.addEdge(6, 8);
        midwest.addEdge(7, 8);
        return midwest;
    }

    private static Graph buildMacGraph() {
        Graph macGraph = new MatrixGraph(105);
        // 0: 5, 13
        macGraph.addEdge(0, 5);
        macGraph.addEdge(0, 13);
        // 1: 2, 3
        macGraph.addEdge(1, 2);
        macGraph.addEdge(1, 3);
        // 2: 1, 14
        macGraph.addEdge(2, 14);
        // 3: 1, 13, 14
        macGraph.addEdge(3, 13);
        macGraph.addEdge(3, 14);
        // -----------------------
        // South side: 4-8
        // 4: 5
        macGraph.addEdge(4, 5);
        // 5: 0, 4, 7
        macGraph.addEdge(5, 7);
        // 6: 7
        macGraph.addEdge(6, 7);
        // 7: 5, 6, 8
        macGraph.addEdge(7, 8);
        // 8: 7, 9, 27
        macGraph.addEdge(8, 9);
        macGraph.addEdge(8, 27);
        // -----------------------
        // East side: 9-11
        // 9: 8, 10, 11
        macGraph.addEdge(9, 10);
        macGraph.addEdge(9, 11);
        // 10: 9
        // 11: 9, 21, 31
        macGraph.addEdge(11, 21);
        macGraph.addEdge(11, 31);
        // -----------------------
        // Fine Arts/Janet Wallace: 12-17
        // 12: 14, 18
        macGraph.addEdge(12, 14);
        macGraph.addEdge(12, 18);
        // 13: 0, 3, 15
        macGraph.addEdge(13, 15);
        // 14: 2, 3, 12
        // 15: 13, 16
        macGraph.addEdge(15, 16);
        // 16: 15, 17, 51
        macGraph.addEdge(16, 17);
        macGraph.addEdge(16, 51);
        // 17: 16
        // -----------------------
        // North side: 18-22
        // 18: 12, 19
        macGraph.addEdge(18, 19);
        // 19: 18, 20, 21
        macGraph.addEdge(19, 20);
        macGraph.addEdge(19, 21);
        // 20: 19, 21, 38
        macGraph.addEdge(20, 21);
        macGraph.addEdge(20, 38);
        // 21: 11, 20, 22, 23
        macGraph.addEdge(21, 22);
        macGraph.addEdge(21, 23);
        // 22: 19, 21, 23, 24
        macGraph.addEdge(22, 23);
        macGraph.addEdge(22, 24);
        // 23: 21, 22
        // -----------------------
        // Rock garden: 24-26
        // 24: 22, 25
        macGraph.addEdge(24, 25);
        // 25: 24, 26
        macGraph.addEdge(25, 26);
        // 26: 25
        // -----------------------
        // Gym nodes: 27-37
        // 27: 8, 28
        macGraph.addEdge(27, 28);
        // 28: 27, 29, 30
        macGraph.addEdge(28, 29);
        macGraph.addEdge(28, 30);
        // 29: 28
        // 30: 28, 36, 72
        macGraph.addEdge(30, 36);
        macGraph.addEdge(30, 72);
        // 31: 11, 32, 34
        macGraph.addEdge(31, 32);
        macGraph.addEdge(31, 34);
        // 32: 31, 33, 35
        macGraph.addEdge(32, 33);
        macGraph.addEdge(32, 35);
        // 33: 32, 38, 41
        macGraph.addEdge(33, 38);
        macGraph.addEdge(33, 41);
        // 34: 31, 35
        macGraph.addEdge(34, 35);
        // 35: 32, 34, 37, 48
        macGraph.addEdge(35, 37);
        macGraph.addEdge(35, 48);
        // 36: 30, 37, 69
        macGraph.addEdge(36, 37);
        macGraph.addEdge(36, 69);
        // 37: 36, 48
        macGraph.addEdge(37, 48);
        // -----------------------
        // Fine arts nodes: 38-48
        // 38: 20, 33, 39, 41
        macGraph.addEdge(38, 39);
        macGraph.addEdge(38, 41);
        // 39: 38, 40
        macGraph.addEdge(39, 40);
        // 40: 39
        // 41: 33, 38, 42, 48
        macGraph.addEdge(41, 42);
        macGraph.addEdge(41, 48);
        // 42: 41, 43, 44
        macGraph.addEdge(42, 43);
        macGraph.addEdge(42, 44);
        // 43: 42, 44, 50
        macGraph.addEdge(43, 50);
        // 44: 42, 45
        macGraph.addEdge(44, 45);
        // 45: 44, 46, 47
        macGraph.addEdge(45, 46);
        macGraph.addEdge(45, 47);
        // 46: 45
        // 47: 45, 51
        macGraph.addEdge(47, 51);
        // -----------------------
        // North Shaw Field: 48-57
        // 48: 35, 37, 41, 49
        macGraph.addEdge(48, 49);
        // 49: 48, 50, 54
        macGraph.addEdge(49, 50);
        macGraph.addEdge(49, 54);
        // 50: 43, 49, 54
        macGraph.addEdge(50, 54);
        // 51: 16, 47
        // 52: no longer used
        // 53: no longer used
        // 54: 49, 50, 55, 57
        macGraph.addEdge(54, 55);
        macGraph.addEdge(54, 57);
        // 55: 54, 56
        macGraph.addEdge(55, 56);
        // 56: 51, 55, 57
        macGraph.addEdge(56, 57);
        // 57: 51, 56, 58
        macGraph.addEdge(57, 58);
        // -----------------------
        // Old Main/Library: 58-65
        // 58: 53, 57, 59, 74
        macGraph.addEdge(58, 59);
        macGraph.addEdge(58, 74);
        // 59: 58, 61, 74
        macGraph.addEdge(59, 61);
        macGraph.addEdge(59, 74);
        // 60: no longer used
        // 61: 59, 62, 73, 79
        macGraph.addEdge(61, 62);
        macGraph.addEdge(61, 73);
        macGraph.addEdge(61, 79);
        // 62: 61, 64, 82
        macGraph.addEdge(62, 64);
        macGraph.addEdge(62, 82);
        // 63: no longer used
        // 64: 62, 65, 66, 99
        macGraph.addEdge(64, 65);
        macGraph.addEdge(64, 66);
        macGraph.addEdge(64, 99);
        // 65: 55, 64
        // -----------------------
        // Kirk area: 66-72
        // 66: 64, 67, 101
        macGraph.addEdge(66, 67);
        macGraph.addEdge(66, 101);
        // 67: 66, 68, 69, 71
        macGraph.addEdge(67, 68);
        macGraph.addEdge(67, 69);
        macGraph.addEdge(67, 71);
        // 68: 67, 71, 101
        macGraph.addEdge(68, 71);
        macGraph.addEdge(68, 101);
        // 69: 36, 67, 70
        macGraph.addEdge(69, 70);
        // 70: 69
        // 71: 68, 72
        macGraph.addEdge(71, 72);
        // 72: 30, 71, 102
        macGraph.addEdge(72, 102);
        // -----------------------
        // Carnegie: 73-81
        // 73: 61
        // 74: 58, 59, 76
        macGraph.addEdge(74, 76);
        // 75: no longer used
        // 76: 74, 84
        macGraph.addEdge(76, 84);
        // 77: no longer used
        // 78: no longer used
        // 79: 61, 80, 81
        macGraph.addEdge(79, 80);
        macGraph.addEdge(79, 81);
        // 80: 79, 81, 82
        macGraph.addEdge(80, 81);
        macGraph.addEdge(80, 82);
        // 81: 79, 80, 84
        macGraph.addEdge(81, 84);
        // -----------------------
        // Weyerhaeuser: 82-89
        // 82: 62, 80, 83
        macGraph.addEdge(82, 83);
        // 83: 82, 89, 90
        macGraph.addEdge(83, 89);
        macGraph.addEdge(83, 90);
        // 84: 78, 81, 85
        macGraph.addEdge(84, 85);
        // 85: 84, 87
        macGraph.addEdge(85, 87);
        // 86: no longer used
        // 87: 85, 88, 89
        macGraph.addEdge(87, 88);
        macGraph.addEdge(87, 89);
        // 88: 87, 89, 92
        macGraph.addEdge(88, 89);
        macGraph.addEdge(88, 92);
        // 89: 83, 87, 88
        // -----------------------
        // Campus Center: 90-102
        // 90: 83, 98
        macGraph.addEdge(90, 98);
        // 91: no longer used
        // 92: 88, 93, 97
        macGraph.addEdge(92, 93);
        macGraph.addEdge(92, 97);
        // 93: 92, 94, 95
        macGraph.addEdge(93, 94);
        macGraph.addEdge(93, 95);
        // 94: 93, 95, 96
        macGraph.addEdge(94, 95);
        macGraph.addEdge(94, 96);
        // 95: 93, 94, 102
        macGraph.addEdge(95, 102);
        // 96: 94, 97, 104
        macGraph.addEdge(96, 97);
        macGraph.addEdge(96, 104);
        // 97: 92, 96, 98, 104
        macGraph.addEdge(97, 98);
        macGraph.addEdge(97, 104);
        // 98: 90, 97, 99, 104
        macGraph.addEdge(98, 99);
        macGraph.addEdge(98, 104);
        // 99: 64, 98, 100, 104
        macGraph.addEdge(99, 100);
        macGraph.addEdge(99, 104);
        // 100: 99, 101, 104
        macGraph.addEdge(100, 101);
        macGraph.addEdge(100, 104);
        // 101: 66, 68, 71, 100
        // 102: 72, 95
        // 103: no longer used
        // 104: 96, 97, 98, 99, 100
        return macGraph;
    }
}
