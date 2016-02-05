package programming6;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * @author Ari Weiland
 */
public class HuffmanTree {

    protected char nodeValue;
    protected HuffmanTree leftChild;
    protected HuffmanTree rightChild;
    protected Map<Character, String> encodingMap = null;

    /**
     * Makes an tree with a given node
     */
    public HuffmanTree(char initialNode) {
        nodeValue = initialNode;
        leftChild = null;
        rightChild = null;
    }

    public HuffmanTree(HuffmanTree leftChild, HuffmanTree rightChild) {
        nodeValue = 0;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    /* -------------------------------------------------------------
     * The section that follows has basic tree utility functions, accessors for node values
     * and children, functions to check the kind of tree and its properties, and to add or modify
     * elements of the tree
     */

    /**
     * Returns the root node's value, or -1 if the tree is empty.
     */
    public char getNodeValue() {
        return nodeValue;
    }

    /**
     * Returns the left child subtree
     */
    public HuffmanTree getLeftChild() {
        return leftChild;
    }

    /**
     * Returns the right child subtree
     */
    public HuffmanTree getRightChild() {
        return rightChild;
    }

    /**
     * Returns true if the node has no children, false otherwise.
     */
    public boolean isLeaf() {
        return !hasLeftChild() && !hasRightChild();
    }

    /**
     * Takes in a new node value, and changes this node's value to the new value.
     */
    public void setNodeValue(char newValue) {
        nodeValue = newValue;
    }

    /**
     * Returns true if the node has a left child, false otherwise.
     */
    public boolean hasLeftChild() {
        return leftChild != null;
    }

    /**
     * Returns true if the node has a right child, false otherwise.
     */
    public boolean hasRightChild() {
        return rightChild != null;
    }

    /**
     * Takes a subtree as input.  It adds the input tree as the left child of this node.
     * Passing an empty tree here causes the left child to be removed.
     */
    public void setLeftChild(HuffmanTree childTree) {
        leftChild = childTree;
    }

    /**
     * Takes a subtree as input.  It adds the input tree as the right child of this node.
     * Passing an empty tree here causes the left child to be removed.
     */
    public void setRightChild(HuffmanTree childTree) {
        rightChild = childTree;
    }

    public void setNode(HuffmanTree node) {
        setNodeValue(node.getNodeValue());
        setLeftChild(node.getLeftChild());
        setRightChild(node.getRightChild());
    }

    /*-------------------------------------------------------------
     * The section that follows has more complex operations.  Computing the height,
     * traversing the tree in various orders, inserting, searching, and deleting.  The
     * last three operations are not fully defined here; should be added to in a subclass.
     */

    /**
     * Prints the tree using indentation for levels of the tree
     */
    public void printTree() {
        printRecur(0);
    }

    /**
     * Takes in an indentation depth, and prints out the tree, first the root
     * then the left subtree, and then the right subtree
     */
    protected void printRecur(int depth) {
        String indent = "";
        for ( int i = 0; i < depth; i++ ) {
            indent += " ";
        }

        if (isLeaf()) {
            System.out.println(indent + "Leaf: " + nodeValue);
        } else {
            if (hasLeftChild()) {
                leftChild.printRecur(depth + 4);
            }
            System.out.println(indent + "Node: " + nodeValue);
            if ( hasRightChild() ) {
                rightChild.printRecur(depth + 4);
            }
        }
    }

    public Map<Character, String> getEncodingMap() {
        if (encodingMap == null) {
            encodingMap = new HashMap<Character, String>();
            if (isLeaf()) {
                encodingMap.put(nodeValue, "");
            } else {
                for (Map.Entry<Character, String> e : leftChild.getEncodingMap().entrySet()) {
                    encodingMap.put(e.getKey(), "0" + e.getValue());
                }
                for (Map.Entry<Character, String> e : rightChild.getEncodingMap().entrySet()) {
                    encodingMap.put(e.getKey(), "1" + e.getValue());
                }
            }
        }
        return encodingMap;
    }

    public String encode(String s) {
        Map<Character, String> encodingMap = getEncodingMap();
        String output = "";
        for (int i=0; i<s.length(); i++) {
            output += encodingMap.get(s.charAt(i));
        }
        return output;
    }

    public String decode(String s) {
        HuffmanTree tree = this;
        String output = "";
        for (int i=0; i<s.length(); i++) {
            if (s.charAt(i) == '0') {
                tree = tree.getLeftChild();
            } else {
                tree = tree.getRightChild();
            }
            if (tree.isLeaf()) {
                output += tree.getNodeValue();
                tree = this;
            }
        }
        return output;
    }

    public static HuffmanTree buildHuffmanTree(String s) {
        Map<Character, Integer> counter = new HashMap<Character, Integer>();
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (!counter.containsKey(c)) {
                counter.put(c, 1);
            } else {
                counter.put(c, 1 + counter.get(c));
            }
        }
        return buildHuffmanTree(counter);
    }

    public static HuffmanTree buildHuffmanTree(Map<Character, Integer> counter) {
        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        for (Map.Entry<Character, Integer> e : counter.entrySet()) {
            queue.add(new Node(e.getValue(), new HuffmanTree(e.getKey())));
        }
        while (queue.size() > 1) {
            Node n1 = queue.poll();
            Node n2 = queue.poll();
            queue.add(new Node(n1.count + n2.count, new HuffmanTree(n1.tree, n2.tree)));
        }
        return queue.poll().tree;
    }

    private static class Node implements Comparable<Node> {
        public final int count;
        public final HuffmanTree tree;

        private Node(int count, HuffmanTree tree) {
            this.count = count;
            this.tree = tree;
        }

        @Override
        public int compareTo(Node o) {
            // note that this returns the lower node
            return count - o.count;
        }
    }

    public static void main(String[] args) {
        String s = "macalester college";
        HuffmanTree ht = buildHuffmanTree(s);
        String encoding = ht.encode(s);
        System.out.println(s);
        System.out.println(encoding);
        System.out.println(ht.decode(encoding));
        System.out.println("Encoding length: " + encoding.length());
        System.out.println("String bit length: " + (s.length() * 8));
        System.out.println();
        String text = buildEndersGameText();
        String normalized = normalize(text);
        HuffmanTree standard = buildHuffmanTree(buildEnglishCharacterDistributionMap());
        HuffmanTree normalizedHT = buildHuffmanTree(normalized);
        String standardEncoding = standard.encode(normalized);
        String idealEncoding = normalizedHT.encode(normalized);
        System.out.println(normalized);
        System.out.println(normalizedHT.decode(idealEncoding));
        System.out.println(standard.decode(standardEncoding));
        System.out.println("Ideal Encoding length: " + standardEncoding.length());
        System.out.println("Standard Encoding length: " + idealEncoding.length());
        System.out.println("String bit length: " + (normalized.length() * 8));
        System.out.println();
        HuffmanTree textHT = buildHuffmanTree(text);
        String textEncoding = textHT.encode(text);
        System.out.println(textHT.decode(textEncoding));
        System.out.println("Raw Text Encoding length: " + textEncoding.length());
        System.out.println("String bit length: " + (text.length() * 8));
    }

    /**
     * Returns a map with a standard english character distribution.
     * It contains only lower case characters, with the space character
     * added in given that 1 out of every 6.1 characters is a space.
     * @return
     */
    public static Map<Character, Integer> buildEnglishCharacterDistributionMap() {
        Map<Character, Integer> counter = new HashMap<Character, Integer>();
        counter.put(' ', 35746);
        counter.put('e', 21912);
        counter.put('t', 16587);
        counter.put('a', 14810);
        counter.put('o', 14003);
        counter.put('i', 13318);
        counter.put('n', 12666);
        counter.put('s', 11450);
        counter.put('r', 10977);
        counter.put('h', 10795);
        counter.put('d', 7874);
        counter.put('l', 7253);
        counter.put('u', 5246);
        counter.put('c', 4943);
        counter.put('m', 4761);
        counter.put('f', 4200);
        counter.put('y', 3853);
        counter.put('w', 3819);
        counter.put('g', 3693);
        counter.put('p', 3316);
        counter.put('b', 2715);
        counter.put('v', 2019);
        counter.put('k', 1257);
        counter.put('x', 315);
        counter.put('q', 205);
        counter.put('j', 188);
        counter.put('z', 128);
        return counter;
    }

    /**
     * Returns a long string of text from Ender's Game.
     * @return
     */
    public static String buildEndersGameText() {
        return "The monitor lady smiled very nicely and tousled his hair and said, \"Andrew, I suppose\n" +
                "by now you're just absolutely sick of having that horrid monitor. Well, I have good news\n" +
                "for you. That monitor is going to come out today. We're going to just take it right out, and\n" +
                "it won't hurt a bit.\"\n" +
                "\n" +
                " Ender nodded. It was a lie, of course, that it wouldn't hurt a bit. But since adults always\n" +
                "said it when it was going to hurt, he could count on that statement as an accurate\n" +
                "prediction of the future. Sometimes lies were more dependable than the truth.\n" +
                "\n" +
                " \"So if you'll just come over here, Andrew, just sit right up here on the examining table.\n" +
                "The doctor will be in to see you in a moment.\"\n" +
                "  The monitor gone. Ender tried to imagine the little device missing from the back of his\n" +
                "neck. I'll roll over on my back in bed and it won't be pressing there. I won't feel it tingling\n" +
                "and taking up the heat when I shower.\n" +
                "\n" +
                " And Peter won't hate me anymore. I'll come home and show him that the monitor's\n" +
                "gone, and he'll see that I didn't make it, either. That I'll just be a normal kid now, like\n" +
                "him. That won't be so bad then. He'll forgive me that I had my monitor a whole year\n" +
                "longer than he had his. We'll be-- not friends, probably. No, Peter was too dangerous.\n" +
                "Peter got so angry. Brothers, though. Not enemies, not friends, but brothers-- able to live\n" +
                "in the same house. He won't hate me, he'll just leave me alone. And when he wants to\n" +
                "play buggers and astronauts, maybe I won't have to play, maybe I can just go read a book.\n" +
                "\n" +
                " But Ender knew, even as he thought it, that Peter wouldn't leave him alone. There was\n" +
                "something in Peter's eyes, when he was in his mad mood, and whenever Ender saw that\n" +
                "look, that glint, he knew that the one thing Peter would not do was leave him alone. I'm\n" +
                "practicing piano, Ender. Come turn the pages for me. Oh, is the monitor boy too busy to\n" +
                "help his brother? Is he too smart? Got to go kill some buggers, astronaut? No, no, I don't\n" +
                "want your help. I can do it on my own, you little bastard, you little Third.\n" +
                "\n" +
                " \"This won't take long, Andrew,\" said the doctor.\n" +
                "\n" +
                " Ender nodded.\n" +
                "\n" +
                " \"It's designed to be removed. Without infection, without damage. But there'll be some\n" +
                "tickling, and some people say they have a feeling of something missing. You'll keep\n" +
                "looking around for something. Something you were looking for, but you can't find it, and\n" +
                "you can't remember what it was. So I'll tell you. It's the monitor you're looking for, and it\n" +
                "isn't there. In a few days that feeling will pass.\"\n" +
                "\n" +
                " The doctor was twisting something at the back of Ender's head. Suddenly a pain stabbed\n" +
                "through him like a needle from his neck to his groin. Ender felt his back spasm, and his\n" +
                "body arched violently backward; hi head struck the bed. He could feel his legs thrashing,\n" +
                "and his hands were clenching each other, wringing each other so tightly that they ached.\n" +
                "\n" +
                " \"Deedee!\" shouted the doctor. \"I need you!\" The nurse ran in, gasped. \"Got to relax\n" +
                "these muscles. Get it to me, now! What are you waiting for!\"\n" +
                "\n" +
                " Something changed hands; Ender could not see. He lurched to one side and fell off the\n" +
                "examining table. \"Catch him!\" cried the nurse.\n" +
                "\n" +
                " \"Just hold him steady.\"\n" +
                "\n" +
                " \"You hold him, doctor, he's too strong for me.\"\n" +
                "\n" +
                " \"Not the whole thing! You'll stop his heart.\"\n" +
                "  Ender felt a needle enter his back just above the neck of his shirt. It burned, but\n" +
                "wherever in him the fire spread, his muscles gradually unclenched. Now he could cry for\n" +
                "the fear and pain of it.\n" +
                "\n" +
                " \"Are you all right, Andrew?\" the nurse asked.\n" +
                "\n" +
                " Andrew could not remember how to speak. They lifted him onto the table. They\n" +
                "checked his pulse, did other things; he did not understand it all.\n" +
                "\n" +
                " The doctor was trembling; his voice shook as he spoke. \"They leave these things in the\n" +
                "kids for three years, what do they expect? We could have switched him off, do you\n" +
                "realize that? We could have unplugged his brain for all time.\"\n" +
                "\n" +
                " \"When does the drug wear off'?\" asked the nurse.\n" +
                "\n" +
                " \"Keep him here for at least an hour. Watch him. If he doesn't start talking in fifteen\n" +
                "minutes, call me. Could have unplugged him forever. I don't have the brains of a bugger.\" ";
    }

    /**
     * Removes all non-letter characters except spaces, and
     * all upper case characters are converted to lower case.
     * @param s
     * @return
     */
    public static String normalize(String s) {
        return s.replaceAll("\'", "").replaceAll("[\\W]", " ").replaceAll(" {2,}", " ").toLowerCase();
    }






















}
