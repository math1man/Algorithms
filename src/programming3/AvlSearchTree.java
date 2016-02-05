package programming3;

import java.util.*;

/**
 * My implementation of an AVL Search Tree.
 * Most of the code is copied from the original BST class (with minor
 * modifications). I also was bored and decided to implement the node
 * values as Comparables instead of just ints. Thus, it is a more
 * generic data structure that holds any type of comparable object.
 *
 * Note that the remove method has not been implemented differently
 * from the BST version.
 *
 * Supports duplicate values.
 *
 * @author Ari Weiland
 */
public class AvlSearchTree<T extends Comparable<T>> implements Iterable<T> {

    protected static enum Side {
        LEFT(1), RIGHT(-1);

        private final int factor;

        private Side(int factor) {
            this.factor = factor;
        }

        public Side other() {
            return values()[1 - ordinal()];
        }

        public boolean isLeft() {
            return this == LEFT;
        }

        public boolean isRight() {
            return !isLeft();
        }

        public int getFactor() {
            return factor;
        }
    }

    protected final boolean allowDuplicates;
    protected final Comparator<T> comparator;

    protected boolean isEmpty;
    protected T nodeValue;
    protected int balanceFactor;
    protected AvlSearchTree<T> leftChild;
    protected AvlSearchTree<T> rightChild;

    /* -------------------------------------------------------------
     * The section that follows contains constructors and core methods. It
     * contains constructors with every permutation of the set of parameters
     * of an initial node value, the allowDuplicates flag, and the comparator.
     *
     * It also contains a method for constructing an empty tree from a
     * tree, which is useful for copying non-value parameters.
     *
     * Finally, at the end are getters for the non-value parameters.
     */

    /**
     * Makes a default empty tree.
     */
    public AvlSearchTree() {
        this((T) null);
    }

    /**
     * Makes a default tree with a given node
     * @param value
     */
    public AvlSearchTree(T value) {
        this(value, null);
    }

    /**
     * Makes an empty tree that can allow duplicates
     * @param allowDuplicates
     */
    public AvlSearchTree(boolean allowDuplicates) {
        this(null, allowDuplicates);
    }

    /**
     * Makes an empty tree with a specific comparator
     * @param comparator
     */
    public AvlSearchTree(Comparator<T> comparator) {
        this(null, comparator);
    }

    /**
     * Makes a tree with a given node that can allow duplicates
     * @param value
     * @param allowDuplicates
     */
    public AvlSearchTree(T value, boolean allowDuplicates) {
        this(value, allowDuplicates, null);
    }

    /**
     * Makes a tree with a given node and specific comparator
     * @param value
     * @param comparator
     */
    public AvlSearchTree(T value, Comparator<T> comparator) {
        this(value, false, comparator);
    }

    /**
     * Makes an empty tree that can allow
     * duplicates and has a specific comparator
     * @param allowDuplicates
     * @param comparator
     */
    public AvlSearchTree(boolean allowDuplicates, Comparator<T> comparator) {
        this(null, allowDuplicates, comparator);
    }

    /**
     * Makes a tree with a given node, that can allow
     * duplicates, and has a specific comparator
     * @param value
     * @param allowDuplicates
     * @param comparator
     */
    public AvlSearchTree(T value, boolean allowDuplicates, Comparator<T> comparator) {
        this.allowDuplicates = allowDuplicates;
        this.comparator = comparator;
        setNodeValue(value);
        balanceFactor = 0;
        leftChild = null;
        rightChild = null;
    }

    /**
     * Returns an empty tree with the same parameters as the calling tree.
     * @return
     */
    public AvlSearchTree<T> emptyTree() {
        return new AvlSearchTree<T>(allowDuplicates, comparator);
    }

    public boolean allowsDuplicates() {
        return allowDuplicates;
    }

    public Comparator<T> getComparator() {
        return comparator;
    }

    /* -------------------------------------------------------------
     * The section that follows has basic tree utility functions, accessors for node values 
     * and children, functions to check the kind of tree and its properties, and to add or modify
     * elements of the tree
     */

    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Returns the root node's value, or -1 if the tree is empty.
     */
    public T getNodeValue() {
        return nodeValue;
    }

    /**
     * Returns the left child subtree
     */
    public AvlSearchTree<T> getLeftChild() {
        return leftChild;
    }

    /**
     * Returns the right child subtree
     */
    public AvlSearchTree<T> getRightChild() {
        return rightChild;
    }

    /**
     * Returns true if the node has no children, false otherwise.
     */
    public boolean isLeaf() {
        return !hasLeftChild() && !hasRightChild();
    }

    /**
     * Returns true if the node has a left child, false otherwise.
     */
    public boolean hasLeftChild() {
        return leftChild != null && !leftChild.isEmpty();
    }

    /**
     * Returns true if the node has a right child, false otherwise.
     */
    public boolean hasRightChild() {
        return rightChild != null && !rightChild.isEmpty();
    }

    /*-------------------------------------------------------------
     * The following methods can disturb the tree's fragile balance.
     * Thus, they are protected, and not available to the caller.
     */

    /**
     * Clears this node, converting it to an empty node.
     */
    protected void clear() {
        setNodeValue(null);
    }

    /**
     * Takes in a new node value, and changes this node's value to the new value.
     * @param newValue
     */
    protected void setNodeValue(T newValue) {
        isEmpty = (newValue == null);
        nodeValue = newValue;
    }

    /**
     * Takes a subtree as input. It adds the input tree as the left child of this
     * node. Passing an empty or null tree will cause the left child to be removed.
     * @param childTree
     */
    protected void setLeftChild(AvlSearchTree<T> childTree) {
        if (isEmpty) {
            throw new IllegalStateException("Cannot add child to empty tree.");
        } else {
            leftChild = childTree;
        }
    }

    /**
     * Takes a subtree as input. It adds the input tree as the right child of this
     * node. Passing an empty or null tree will cause the left child to be removed.
     * @param childTree
     */
    protected void setRightChild(AvlSearchTree<T> childTree) {
        if (isEmpty) {
            throw new IllegalStateException("Cannot add child to empty tree.");
        } else {
            rightChild = childTree;
        }
    }

    /**
     * Takes a subtree as input. It adds the input tree as either the right or left child
     * of this node. Passing an empty or null tree will cause that side's child to be removed.
     * @param childTree
     * @param side
     */
    protected void setChild(AvlSearchTree<T> childTree, Side side) {
        if (side.isLeft()) {
            setLeftChild(childTree);
        } else {
            setRightChild(childTree);
        }
    }

    /**
     * Converts this node to the input node. This sets the value,
     * the children, and the isEmpty flag appropriately. Note that
     * it does not change the allowDuplicate flag or the comparator.
     * @param node
     */
    protected void setNode(AvlSearchTree<T> node) {
        setNodeValue(node.getNodeValue());
        setLeftChild(node.getLeftChild());
        setRightChild(node.getRightChild());
        isEmpty = node.isEmpty;
    }

    /**
     * Convenience method used to get the left
     * or right child, specified by the side
     * @param side
     * @return
     */
    protected AvlSearchTree<T> getChild(Side side) {
        if (side.isLeft()) {
            return getLeftChild();
        } else {
            return getRightChild();
        }
    }

    /*-------------------------------------------------------------
     * The section that follows has more complex operations.
     * getHeight, insert, contains, and remove all take O(log n)
     * time, though removing is not properly implemented.
     * size, and asList both take O(n) time.
     * The call to iterator() takes O(log n) time, and each call
     * to next() takes worst case O(log n) time, but the average
     * case will be in O(n) time.
     */

    /**
     * Computes and returns the height of the tree. Note that an empty tree
     * is defined to have a height of -1, a leaf has a height 0, and larger
     * trees' heights are defined by the amount of child layers they have.
     * @return
     */
    public int getHeight() {
        if (isEmpty()) {
            return -1;
        } else if (isLeaf()) {
            return 0;
        } else {
            int leftHeight = 0;
            int rightHeight = 0;
            if (hasLeftChild()) {
                leftHeight = leftChild.getHeight();
            }
            if (hasRightChild()) {
                rightHeight = rightChild.getHeight();
            }
            return Math.max(leftHeight + 1, rightHeight + 1);
        }
    }

    /**
     * Takes in a value and handles the empty tree case, passing everything else off to the Node
     * class's contains method.  Return true if it finds the value and false otherwise.
     */
    public boolean contains(T value) {
        if (isEmpty()) {
            return false;
        } else if (compare(nodeValue, value) == 0) {
            return true;
        } else if (compare(nodeValue, value) > 0 && hasLeftChild()) {
            return leftChild.contains(value);
        } else {
            return compare(nodeValue, value) < 0 && hasRightChild() && rightChild.contains(value);
        }
    }

    /**
     * Takes in a new value and handles the empty tree case.  Performs classic programming3.BST insertion.
     */
    public void insert(T newValue) {
        if (newValue == null) {
            throw new NullPointerException();
        } else {
            insertRecur(newValue);
        }
    }

    /**
     * Recursive method for insertion. Returns true if 
     * the child node experienced a height increase.
     * @param newValue
     * @return
     */
    protected boolean insertRecur(T newValue) {
        if (isEmpty()) {
            setNodeValue(newValue);
            return true;
        } else if (!allowDuplicates && nodeValue.compareTo(newValue) == 0) {
            return false; // if duplicates not allowed and is a duplicate, stop
        } else {
            // duplicate values are placed on the right (if allowed)
            Side placeOn = nodeValue.compareTo(newValue) > 0 ? Side.LEFT : Side.RIGHT;
            if (getChild(placeOn) == null) {
                setChild(emptyTree(), placeOn);
            }
            if (getChild(placeOn).insertRecur(newValue)) {
                balanceFactor += placeOn.getFactor();
                if (balanceFactor == 2 * placeOn.getFactor()) {
                    if (getChild(placeOn).balanceFactor == placeOn.getFactor()) {
                        rotate(placeOn.other());
                    } else {
                        doubleRotate(placeOn.other());
                    }
                }
                return balanceFactor != 0;
            } else {
                return false;
            }
        }
    }

    /**
     * Rotates the calling node in the specified direction. Note 
     * that it automatically sets the balance factor of the calling 
     * node to 0, which assumes the rotation occurred rightfully.
     * @param side
     */
    protected void rotate(Side side) {
        AvlSearchTree<T> newChild = new AvlSearchTree(nodeValue, allowDuplicates, comparator);
        newChild.setChild(getChild(side), side);
        newChild.setChild(getChild(side.other()).getChild(side), side.other());
        nodeValue = getChild(side.other()).getNodeValue();
        setChild(getChild(side.other()).getChild(side.other()), side.other());
        setChild(newChild, side);
        balanceFactor = 0;
    }

    /**
     * Performs a double rotate on the calling node in the specified direction. 
     * Note that it automatically sets the balance of the calling node and its 
     * children appropriately, which assumes the rotation occurred rightfully.
     * @param side
     */
    protected void doubleRotate(Side side) {
        int balance = getChild(side.other()).getChild(side).balanceFactor;
        getChild(side.other()).rotate(side.other());
        rotate(side);
        if (balance == 1) {                     // The new node was added on the left side.
            getRightChild().balanceFactor = -1; // This results in the right child's left child
            getLeftChild().balanceFactor = 0;   // being shorter than all other grandchildren.
        } else if (balance == 0) {              // getChild(side.other()).getChild(side) is the new node.
            getRightChild().balanceFactor = 0;  // This results in balanced trees on either
            getLeftChild().balanceFactor = 0;   // side because both nodes have no children.
        } else {                                // The new node was added on the right side.
            getRightChild().balanceFactor = 0;  // This results in the left child's right child
            getLeftChild().balanceFactor = 1;   // being shorter than all other grandchildren.
        }
    }

    /**
     * Recursively removes the value from the tree. Returns true if 
     * the value was found and removed. For trees that allow duplicates, 
     * it will only remove the first instance of the value it finds
     * 
     * WARNING: This method is not properly implemented for 
     *          AVL Search Trees. Use at your own risk!
     *          
     * @param value
     * @return
     */
    public boolean remove(T value) {
        if (isEmpty()) {
            return false;
        } else {
            if (compare(nodeValue, value) == 0) {   // this is the node to delete
                if (isLeaf()) {
                    clear();
                } else if (!hasLeftChild()) {
                    setNode(rightChild);            // right child is taking this node's place
                } else if (!hasRightChild()) {
                    setNode(leftChild);             // left child is taking this node's place
                } else {                            // Node has two children, most complicated case
                    T replaceValue = leftChild.findMaxValue();
                    setNodeValue(replaceValue);     // delete from left child
                    leftChild.remove(replaceValue);
                }
                return true;
            } else if (compare(nodeValue, value) > 0) {
                return hasLeftChild() && leftChild.remove(value);
            } else { // value > nodeValue
                return hasRightChild() && rightChild.remove(value);
            }
        }
    }

    protected T findMaxValue() {
        if (!hasRightChild()) {
            return nodeValue;
        } else {
            return rightChild.findMaxValue();
        }
    }

    protected int compare(T t1, T t2) {
        if (comparator == null) {
            return t1.compareTo(t2);
        } else {
            return comparator.compare(t1, t2);
        }
    }

    /**
     * Returns the amount of nodes in this tree.
     * @return
     */
    public int size() {
        if (isEmpty()) {
            return 0;
        } else {
            int size = 1;
            if (hasLeftChild()) {
                size += leftChild.size();
            }
            if (hasRightChild()) {
                size += rightChild.size();
            }
            return size;
        }
    }

    /**
     * Returns the tree as a list, sorted in increasing order
     * @return
     */
    public List<T> asList() {
        List<T> list = new ArrayList<T>();
        if (!isEmpty()) {
            if (hasLeftChild()) {
                list.addAll(leftChild.asList());
            }
            list.add(nodeValue);
            if (hasRightChild()) {
                list.addAll(rightChild.asList());
            }
        }
        return list;
    }

    /**
     * Returns an iterator that uses a form of DFS to iterate
     * through the tree, seeking out the lowest value and then
     * returning the next lowest values in order. The total time
     * to construct and iterate through the entire tree is O(n)
     * time, though construction is always O(log n) and individual
     * calls to next() have worst case O(log n) time.
     * @return
     */
    public Iterator<T> iterator() {
        return new TreeIterator<T>(this);
    }

    private static class TreeIterator<T extends Comparable<T>> implements Iterator<T> {

        private final Stack<AvlSearchTree<T>> stack = new Stack<AvlSearchTree<T>>();

        public TreeIterator(AvlSearchTree<T> tree) {
            fillStack(tree);
        }

        @Override
        public boolean hasNext() {
            // runs in O(1) time
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            // runs in amortized O(1) time
            AvlSearchTree<T> next = stack.pop();
            fillStack(next.getRightChild());
            return next.getNodeValue();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void fillStack(AvlSearchTree<T> tree) {
            while (tree != null && !tree.isEmpty()) {
                stack.push(tree);
                tree = tree.getLeftChild();
            }
        }
    }

    /*-------------------------------------------------------------
     * The section that follows contains various print methods.
     */

    @Override
    public String toString() {
        return "" + nodeValue;
    }

    /**
     * Traverses the tree, printing the node values.  This function performs preorder
     * traversal, meaning the root is touched, then the left child
     * is traversed, and finally the right child is traversed.
     */
    public void preorderTraverse() {
        if (!isEmpty()) {
            // handle the root node's value
            System.out.println(nodeValue);
            // recur on left and right children, if they exist
            if (hasLeftChild()) {
                leftChild.preorderTraverse();
            }
            if (hasRightChild()) {
                rightChild.preorderTraverse();
            }
        }
    }

    /**
     * Traverses the tree, printing the node values. This function performs postorder
     * traversal, meaning first the left child is traversed, then the right child is
     * traversed, and finally the root is touched.
     */
    public void postorderTraverse() {
        if (!isEmpty()) {
            // recur on left and right children, if they exist
            if (hasLeftChild()) {
                leftChild.preorderTraverse();
            }
            if (hasRightChild()) {
                rightChild.preorderTraverse();
            }
            // handle the root node's value
            System.out.println(nodeValue);
        }
    }

    /**
     * Traverses the tree, printing the node values.  This function performs inorder traversal,
     * meaning the left child is traversed, then the root is touched, and finally the right
     * child is traversed.
     */
    public void inorderTraverse() {
        if (!isEmpty()) {
            // recur on left and right children, if they exist
            if ( hasLeftChild() ) {
                leftChild.preorderTraverse();
            }

            // handle the root node's value
            System.out.println(nodeValue);

            if ( hasRightChild() ) {
                rightChild.preorderTraverse();
            }
        }
    }

    /**
     * Prints the tree using indentation for levels of the tree.
     * Setting inOrder as false prints the original method.
     * Setting inOrder to true prints the tree in order, with the
     * smallest value first and all values in order. Indentation
     * is still used to implement layers. "None" is not displayed
     */
    public void printTree(boolean inOrder) {
        if (isEmpty()) {
            System.out.println("Tree is empty");
        } else if (inOrder) {
            printRecurInOrder(0);
        } else {
            printRecurPreOrder(0);
        }
    }

    /**
     * Takes in an indentation depth, and prints out the tree, first
     * the root, then the left subtree, and then the right subtree
     */
    protected void printRecurPreOrder(int depth) {
        String indent = "";
        for ( int i = 0; i < depth; i++ ) {
            indent += " ";
        }

        if (isLeaf()) {
            System.out.println("Leaf: " + nodeValue);
        } else {
            System.out.println("Node: " + nodeValue);
            System.out.print(indent + "    LEFT:  ");
            if (hasLeftChild()) {
                leftChild.printRecurPreOrder(depth + 4);
            } else {
                System.out.println("None");
            }
            System.out.print(indent + "    RIGHT: ");
            if ( hasRightChild() ) {
                rightChild.printRecurPreOrder(depth + 4);
            } else {
                System.out.println("None");
            }
        }
    }

    /**
     * Takes in an indentation depth, and prints out the tree, first
     * the left subtree, then the root, and then the right subtree
     */
    protected void printRecurInOrder(int depth) {
        String indent = "";
        for ( int i = 0; i < depth; i++ ) {
            indent += " ";
        }

        if (isLeaf()) {
            System.out.println(indent + "Leaf: " + nodeValue);
        } else {
            if (hasLeftChild()) {
                leftChild.printRecurInOrder(depth + 4);
            }
            System.out.println(indent + "Node: " + nodeValue);
            if ( hasRightChild() ) {
                rightChild.printRecurInOrder(depth + 4);
            }
        }
    }

    /*-------------------------------------------------------------
     * main method
     */

    public static void main(String[] args) {
        System.out.println("------------------Specific test:");
        // This first tree is built in a very specific
        // order so that all four rotations are tested
        // adding the 7 triggers an L-rotation
        // adding the 8 triggers an RL-rotation
        // adding the 3 triggers an R-rotation
        // adding the 2 triggers an LR-rotation
        // Note that behavior on the double rotations is slightly different
        // when they occur higher in the tree, and while those cases are not
        // explicitly tested here, I have tested them thoroughly
        AvlSearchTree<Integer> tree = new AvlSearchTree<Integer>();
        List<Integer> valList = Arrays.asList(5, 6, 7, 9, 8, 4, 3, 1, 2);
        for (int val : valList) {
            System.out.println("Inserting value " + val);
            tree.insert(val);
            tree.printTree(true);
            System.out.println();
        }
        System.out.println(valList);        // the original list of values
        System.out.println(tree.asList());  // the sorted list of values via the tree

        System.out.println("------------------Random test:");
        // This second tree is built using a random set
        // of 20 values between 0 and 99. Note that the
        // tree is not set to accept duplicates, so it
        // is possible the final tree may not have 20
        // values if it generated the same value twice.
        AvlSearchTree<Integer> newT = new AvlSearchTree<Integer>();
        List<Integer> newValList = new ArrayList<Integer>();
        for (int i = 0; i < 20; i++) {
            int val = (int) (Math.random() * 100);
            System.out.println("Inserting value " + val);
            newValList.add(val);
            newT.insert(val);
            newT.printTree(true);
            System.out.println();
        }
        System.out.println(newValList);     // the random list of values
        System.out.println(newT.asList());  // the same list of values sorted via the tree

        List<Integer> extraList = new ArrayList<Integer>();
        for ( int j = 0; j < 5; j++ ) {
            int val = (int)(Math.random() * 100);
            extraList.add(val);
        }
        Collections.shuffle(newValList);
        System.out.println("------------------Searching for values that are in the random tree:");
        // check to make sure the random tree has all the values in it it is supposed to
        for ( int i = 0; i < 20; i++ ) {
            int v = newValList.get(i);
            System.out.println("Searching for value " + v);
            System.out.println(newT.contains(v));
        }
        System.out.println("------------------Searching for values that may not be in the random tree:");
        // check to make sure random other values are NOT in the tree
        // it is possible for these to be true if it randomly generates numbers that
        // were added initially, but statistically most of these should return false
        for ( int i = 0; i < 5; i++ ) {
            int v = extraList.get(i);
            System.out.println("Searching for value " + v);
            System.out.println(newT.contains(v));
        }
    }
}
