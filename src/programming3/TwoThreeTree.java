package programming3;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class TwoThreeTree<T extends Comparable<T>> {

    private static enum Side {
        LEFT, MIDDLE, RIGHT;

        public Side other() {
            return values()[2 - ordinal()];
        }

        public boolean isLeft() {
            return this == LEFT;
        }

        public boolean isMiddle() {
            return this == MIDDLE;
        }

        public boolean isRight() {
            return this == RIGHT;
        }
    }

    private T leftValue;
    private T rightValue;
    private TwoThreeTree<T> leftChild;
    private TwoThreeTree<T> middleChild;
    private TwoThreeTree<T> rightChild;

    public TwoThreeTree() {
    }

    public TwoThreeTree(T value) {
        leftValue = value;
        rightValue = null;
        leftChild = null;
        middleChild = null;
        rightChild = null;
    }

    public boolean isEmpty() {
        return leftValue == null && rightValue == null;
    }

    public boolean isTwoNode() {
        return leftValue != null && rightValue == null;
    }

    public boolean isThreeNode() {
        return leftValue != null && rightValue != null;
    }

    public boolean isLeaf() {
        return leftChild == null;
    }

    public T getValue() {
        return getLeftValue();
    }

    public T getLeftValue() {
        return leftValue;
    }

    public T getRightValue() {
        return rightValue;
    }

    public TwoThreeTree<T> getLeftChild() {
        return leftChild;
    }

    public TwoThreeTree<T> getMiddleChild() {
        return middleChild;
    }

    public TwoThreeTree<T> getRightChild() {
        return rightChild;
    }

    private T getValue(Side side) {
        if (side.isLeft()) {
            return getLeftValue();
        } else {
            return getRightValue();
        }
    }

    private TwoThreeTree<T> getChild(Side side) {
        if (side.isLeft()) {
            return getLeftChild();
        } else if (side.isMiddle()) {
            return getMiddleChild();
        } else {
            return getRightChild();
        }
    }
    
    private void setValue(T value) {
        leftValue = value;
        rightValue = null;
    }

    private void setValue(T value, Side side) {
        if (side.isLeft()) {
            if (isTwoNode()) {
                rightValue = leftValue;
            }
            leftValue = value;
        } else {
            rightValue = value;
        }
    }

    private void setChild(TwoThreeTree<T> child, Side side) {
        if (side.isLeft()) {
            leftChild = child;
        } else if (side.isMiddle()) {
            middleChild = child;
        } else {
            rightChild = child;
        }
    }

    public void insert(T value) {
        if (value != null) {
            TwoThreeTree<T> tree = insertRecur(value);
            if (tree != null) {
                leftValue = tree.leftValue;
                rightValue = tree.rightValue;
                leftChild = tree.leftChild;
                middleChild = tree.middleChild;
                rightChild = tree.rightChild;
            }
        }
    }

    public TwoThreeTree<T> insertRecur(T value) {
        if (isEmpty()) {
            leftValue = value;
            return null;
        } else if (isTwoNode()) {
            int compare = value.compareTo(leftValue);
            if (compare != 0) {
                if (isLeaf()) {
                    if (compare < 0) {
                        rightValue = leftValue;
                        leftValue = value;
                    } else {
                        rightValue = value;
                    }
                } else {
                    if (compare < 0) {
                        TwoThreeTree<T> tree = leftChild.insertRecur(value);
                        if (tree != null) {
                            rightValue = leftValue;
                            leftValue = tree.getLeftValue();
                            leftChild = tree.getLeftChild();
                            middleChild = tree.getRightChild();
                        }
                    } else {
                        TwoThreeTree<T> tree = rightChild.insertRecur(value);
                        if (tree != null) {
                            rightValue = tree.getLeftValue();
                            middleChild = tree.getLeftChild();
                            rightChild = tree.getRightChild();
                        }
                    }
                }
            }
            return null;
        } else {
            int left = value.compareTo(leftValue);
            int right = value.compareTo(rightValue);
            TwoThreeTree<T> newTree = null;
            if (left != 0 && right != 0) {
                if (isLeaf()) {
                    TwoThreeTree<T> leftTree =  new TwoThreeTree<T>(leftValue);
                    TwoThreeTree<T> valueTree = new TwoThreeTree<T>(value);
                    TwoThreeTree<T> rightTree = new TwoThreeTree<T>(rightValue);
                    if (left < 0) { // promote left
                        newTree = leftTree;
                        newTree.leftChild = valueTree;
                        newTree.rightChild = rightTree;
                    } else if (right > 0) { // promote right
                        newTree = rightTree;
                        newTree.leftChild = leftTree;
                        newTree.rightChild = valueTree;
                    } else { // promote new value
                        newTree = valueTree;
                        newTree.leftChild = leftTree;
                        newTree.rightChild = rightTree;
                    }
                } else {
                    if (left < 0) {
                        TwoThreeTree<T> tree = leftChild.insertRecur(value);
                        if (tree != null) {
                            newTree = new TwoThreeTree<T>(leftValue);
                            newTree.leftChild = tree;
                            newTree.rightChild = new TwoThreeTree<T>(rightValue);
                            newTree.rightChild.leftChild = this.middleChild;
                            newTree.rightChild.rightChild = this.rightChild;
                        }
                    } else if (right > 0) {
                        TwoThreeTree<T> tree = rightChild.insertRecur(value);
                        if (tree != null) {
                            newTree = new TwoThreeTree<T>(rightValue);
                            newTree.rightChild = tree;
                            newTree.leftChild = new TwoThreeTree<T>(leftValue);
                            newTree.leftChild.leftChild = this.leftChild;
                            newTree.leftChild.rightChild = this.middleChild;
                        }
                    } else {
                        TwoThreeTree<T> tree = middleChild.insertRecur(value);
                        if (tree != null) {
                            newTree = new TwoThreeTree<T>(tree.getLeftValue());
                            newTree.rightChild = new TwoThreeTree<T>(rightValue);
                            newTree.rightChild.leftChild = tree.getRightChild();
                            newTree.rightChild.rightChild = this.rightChild;
                            newTree.leftChild = new TwoThreeTree<T>(leftValue);
                            newTree.leftChild.leftChild = this.leftChild;
                            newTree.leftChild.rightChild = tree.getLeftChild();
                        }
                    }
                }
            }
            return newTree;
        }
    }

    /**
     * Prints the tree using indentation for levels of the tree
     */
    public void printTree() {
        if (isEmpty()) {
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
            System.out.print("Leaf: " + leftValue);
            if (isThreeNode()) {
                System.out.print(", " + rightValue);
            }
            System.out.println();
        } else {
            System.out.print("Node: " + leftValue);
            if (isThreeNode()) {
                System.out.print(", " + rightValue);
            }
            System.out.println();
            System.out.print(indent + "     LEFT:  ");
            leftChild.printRecur(depth + 4);
            if (isThreeNode()) {
                System.out.print(indent + "     MID:   ");
                middleChild.printRecur(depth + 4);
            }
            System.out.print(indent + "     RIGHT: ");
            rightChild.printRecur(depth + 4);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "";
        } else if (isTwoNode()) {
            return leftValue.toString();
        } else {
            return leftValue + ", " + rightValue;
        }
    }

    public static void main(String[] args) {
        TwoThreeTree<Integer> tree = new TwoThreeTree<Integer>();
        List<Integer> values = Arrays.asList(101, 435, 12, 982, 257, 50, 93, 176, 702, 590, 200);
        for (int i : values) {
            System.out.println("\nInserting " + i);
            tree.insert(i);
            tree.printTree();
        }
    }
}
