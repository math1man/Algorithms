package programming3;

import java.util.*;

/**
 * Implements a normal, typical Binary Search Tree.  Really just the insert, delete, and
 * search are different and implemented here.
 */
public class BinarySearchTree {

    protected boolean emptyTree;
    protected int nodeValue;
    protected BinarySearchTree leftChild;
    protected BinarySearchTree rightChild;

    /**
     * Makes an empty tree.
     */
    public BinarySearchTree() {
        emptyTree = true;
        nodeValue = 0;
        leftChild = null;
        rightChild = null;
    }

    /**
     * Makes an tree with a given node
     */
    public BinarySearchTree(int initialNode) {
        emptyTree = false;
        nodeValue = initialNode;
        leftChild = null;
        rightChild = null;
    }

    /* -------------------------------------------------------------
     * The section that follows has basic tree utility functions, accessors for node values 
     * and children, functions to check the kind of tree and its properties, and to add or modify
     * elements of the tree
     */

    /**
     * Returns True if the tree is empty, and False otherwise.
     */
    public boolean isEmpty() {
        return emptyTree;
    }

    /**
     * Returns the root node's value, or -1 if the tree is empty.
     */
    public int getNodeValue() {
        if (isEmpty()) {
            return -1;
        }
        else {
            return nodeValue;
        }
    }

    /**
     * Returns the left child subtree
     */
    public BinarySearchTree getLeftChild() {
        return leftChild;
    }

    /**
     * Returns the right child subtree
     */
    public BinarySearchTree getRightChild() {
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
    public void setNodeValue(int newValue) {
        nodeValue = newValue;
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

    /**
     * Takes a subtree as input.  It adds the input tree as the left child of this node.
     * Passing an empty tree here causes the left child to be removed.
     */
    public void setLeftChild(BinarySearchTree childTree) {
        if (emptyTree) {
            System.out.println("Cannot add child to empty tree.");
        } else {
            leftChild = childTree;
        }
    }

    /**
     * Takes a subtree as input.  It adds the input tree as the right child of this node.
     * Passing an empty tree here causes the left child to be removed.
     */
    public void setRightChild(BinarySearchTree childTree) {
        if (emptyTree) {
            System.out.println("Cannot add child to empty tree.");
        } else {
            rightChild = childTree;
        }
    }

    public void setNode(BinarySearchTree node) {
        setNodeValue(node.getNodeValue());
        setLeftChild(node.getLeftChild());
        setRightChild(node.getRightChild());
        emptyTree = node.emptyTree;
    }

    /*-------------------------------------------------------------
     * The section that follows has more complex operations.  Computing the height,
     * traversing the tree in various orders, inserting, searching, and deleting.  The 
     * last three operations are not fully defined here; should be added to in a subclass.
     */

    /**
     * Computes and returns the height of the tree.  Note that an empty tree
     * is defined to have a height of -1, a nonempty tree relies on the Node's
     * getHeight method.
     */
    public int getHeight() {
        if ( isEmpty() ) {
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
     * Prints the tree using indentation for levels of the tree
     */
    public void printTree() {
        if ( isEmpty() ) {
            System.out.println("Tree is empty");
        } else {
            printRecur(0);
        }
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
            System.out.println("Leaf: " + nodeValue);
        } else {
            System.out.println("Node: " + nodeValue);
            System.out.print(indent + "     LEFT:  ");
            if (hasLeftChild()) {
                leftChild.printRecur(depth + 4);
            } else {
                System.out.println("None");
            }
            System.out.print(indent + "     RIGHT: ");
            if ( hasRightChild() ) {
                rightChild.printRecur(depth + 4);
            } else {
                System.out.println("None");
            }
        }
    }

    /**
     * Takes in a value and handles the empty tree case, passing everything else off to the Node
     * class's search method.  Return true if it finds the value and false otherwise.
     */
    public boolean search(int value) {
        if (isEmpty()) {
            return false;
        } else if (value == nodeValue) {
            return true;
        } else if (value < nodeValue && hasLeftChild()) {
            return leftChild.search(value);
        } else {
            return value >= nodeValue && hasRightChild() && rightChild.search(value);
        }
    }

    /**
     * Takes in a new value and handles the empty tree case.  Performs classic programming3.BST insertion.
     */
    public void insert(int newValue) {
        if (isEmpty()) {
            nodeValue = newValue;
            emptyTree = false;
        } else if (newValue < nodeValue) {
            if (hasLeftChild()) {
                leftChild.insert(newValue);
            } else {
                setLeftChild(new BinarySearchTree(newValue));
            }
        } else if (newValue > nodeValue) {
            if (hasRightChild()) {
                rightChild.insert(newValue);
            } else {
                setRightChild(new BinarySearchTree(newValue));
            }
        }
    }

    /**
     * Takes in a value and checks for the empty tree case. Passes everything else off to the Node
     * class's delete method.
     */
    public void delete(int value) {
        if (isEmpty()) {
            System.out.println("Cannot remove value from an empty tree");
        } else {
            if (value == nodeValue) {           // this is the node to delete
                if (isLeaf()) {
                    setNode(new BinarySearchTree());         // this node is going away, return empty tree to trigger removal
                } else if (!hasLeftChild()) {
                    setNode(rightChild);        // right child is taking this node's place
                } else if (!hasRightChild()) {
                    setNode(leftChild);         // left child is taking this node's place
                } else {                        // Node has two children, most complicated case
                    int replaceValue = leftChild.findMaxValue();
                    setNodeValue(replaceValue); // delete from left child
                    leftChild.delete(replaceValue);
                }
            } else if (value < nodeValue) {
                if (hasLeftChild()) {
                    leftChild.delete(value);
                }
            } else if (hasRightChild()) {       // value > nodeValue
                rightChild.delete(value);       // update this node's right child
            }
        }
    }

    /**
     * Finds the maximum value in this subtree.  If there is a right child, then it 
     * follows the path down to the right child.  If no right child, then this node is the
     * maximum.
     */
    protected int findMaxValue() {
        if (!hasRightChild()) {
            return nodeValue;
        } else {
            return rightChild.findMaxValue();
        }
    }

    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();
        System.out.println(tree.isEmpty());
        System.out.println(tree.getHeight());
        tree.printTree();
        tree.insert(3);
        tree.printTree();
        System.out.println(tree.getHeight());
        tree.setLeftChild(new BinarySearchTree(1234));
        tree.setRightChild(new BinarySearchTree(101));
        System.out.println(tree.getHeight());
        tree.preorderTraverse();
        tree.inorderTraverse();
        tree.postorderTraverse();

        BinarySearchTree newT = new BinarySearchTree();
        List<Integer> valList = new ArrayList<Integer>();
        for ( int i = 0; i < 20; i++ ) {
            int val = (int)(Math.random() * 100);
            System.out.println("Inserting value " + val);
            valList.add(val);
            newT.insert(val);
        }
        newT.printTree();

        List<Integer> extraList = new ArrayList<Integer>();
        for ( int j = 0; j < 5; j++ ) {
            int val = (int)(Math.random() * 100);
            extraList.add(val);
        }
        Collections.shuffle(valList);
        System.out.println("------------------Searching for values that are in tree:");
        for ( int i = 0; i < 20; i++ ) {
            int v = valList.get(i);
            System.out.println("Searching for value " + v);
            System.out.println(newT.search(v));
        }
        System.out.println("------------------Searching for values that may not be there:");
        for ( int i = 0; i < 5; i++ ) {
            int v = extraList.get(i);
            System.out.println("Searching for value " + v);
            System.out.println(newT.search(v));
        }

        Collections.shuffle(valList);
        for ( int i = 0; i < 20; i++ ) {
            int v = valList.get(i);
            System.out.println("Deleting value " + v);
            newT.delete(v);
            newT.printTree();
        }
    }
}
