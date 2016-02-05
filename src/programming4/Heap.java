package programming4;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class Heap<T extends Comparable<T>> implements Iterable<T> {

    private Comparable<T>[] array;
    private int size;

    public Heap() {
        this(10);
    }

    public Heap(int initialCap) {
        array = new Comparable[initialCap];
        size = 0;
    }

    public Heap(T... array) {
        this.array = array;
        size = array.length;
        buildHeap();
    }

    public void add(T t) {
        if (array.length == size) {
            Comparable<T>[] newArray = new Comparable[size * 2];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
        int currNode = size;
        size++;
        array[currNode] = t;
        int parentNode = findParentNode(currNode);
        while (currNode > 0 && array[parentNode].compareTo(t) < 0) {
            array[currNode] = array[parentNode];
            array[parentNode] = t;
            currNode = parentNode;
            parentNode = findParentNode(currNode);
        }
    }

    public T checkLargest() {
        if (size > 0) {
            return (T) array[0];
        } else {
            return null;
        }
    }

    public T removeLargest() {
        T largest = checkLargest();
        size--;
        if (largest != null && size > 0) {
            T value = (T) array[size];
            array[0] = value;
            int currNode = 0;
            int bigChildNode = findBigChild(currNode);
            while (currNode < ((size) / 2) && array[bigChildNode].compareTo(value) > 0) {
                array[currNode] = array[bigChildNode];
                array[bigChildNode] = value;
                currNode = bigChildNode;
                bigChildNode = findBigChild(currNode);
            }

        }
        return largest;
    }

    private int findParentNode(int index) {
        return (index + 1) / 2 - 1;
    }

    private int findLeftChildNode(int index) {
        return (index + 1) * 2 - 1;
    }

    private int findRightChildNode(int index) {
        return (index + 1) * 2;
    }

    private int findBigChild(int index) {
        int leftChildNode = findLeftChildNode(index);
        int rightChildNode = findRightChildNode(index);
        if (rightChildNode < size && array[leftChildNode].compareTo((T) array[rightChildNode]) < 0) {
            return rightChildNode;
        } else {
            return leftChildNode;
        }
    }

    private void buildHeap() {
        int half = size / 2 - 1;
        for (int r = half; r >= 0; r--) {
            int currNode = r;
            T value = (T) array[r];
            int bigChildNode = findBigChild(currNode);
            while (currNode <= half && array[bigChildNode].compareTo(value) > 0) {
                array[currNode] = array[bigChildNode];
                array[bigChildNode] = value;
                currNode = bigChildNode;
                bigChildNode = findBigChild(currNode);
            }
        }
    }

    public List<T> sort() {
        int oldSize = size;
        for (int i=size-1; i > 0; i--) {
            T next = removeLargest();
            array[i] = next;
        }
        size = oldSize;
        List<T> list = new ArrayList<T>(size);
        for (int i=0; i<size; i++) {
            list.add((T) array[i]);
        }
        buildHeap();
        return list;
    }

    @Override
    public Iterator<T> iterator() {
        return sort().iterator();
    }

    @Override
    public String toString() {
        Comparable<T>[] output = new Comparable[size];
        System.arraycopy(array, 0, output, 0, size);
        return Arrays.toString(output);
    }

    public static void main(String[] args) {
        Heap<Integer> heap = new Heap<Integer>(0, 1, 2, 3, 4, 5);
        System.out.println(heap);
        heap.add(6);
        System.out.println(heap);
        System.out.println(heap.sort());
        System.out.println(heap);

        Heap<String> stringHeap = new Heap<String>("a", "b", "c", "d");
        System.out.println(stringHeap);
        stringHeap.add("x");
        System.out.println(stringHeap);
        System.out.println(stringHeap.sort());
        System.out.println(stringHeap);
    }
}
