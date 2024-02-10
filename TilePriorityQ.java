package finalproject;

import java.util.ArrayList;
import java.util.Arrays;


import finalproject.system.Tile;

public class TilePriorityQ {
    public ArrayList<Tile> heap;

    public TilePriorityQ(ArrayList<Tile> vertices) {
        this.heap = new ArrayList<>(vertices);
        for (int i = 0; i < vertices.size(); i++) {
            heapifyUp(heap.size() - i - 1);
        }
    }
    
    public Tile removeMin() {
        if (heap.isEmpty()) {
            return null;
        }
        Tile minTile = heap.get(0);
        heap.set(0, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        heapifyDown(0);
        if (!heap.isEmpty()) {
            Tile min = findMinimum();
            moveElementToStart(heap, heap.indexOf(min));
        }
        return minTile;
}


    public void updateKeys(Tile t, Tile newPred, double newEstimate) {
        int index = heap.indexOf(t);
        if (index != -1) {
            Tile currentTile = heap.get(index);
            currentTile.predecessor = newPred;
            currentTile.costEstimate = newEstimate;
            heapifyUp(index);
        }
    }

    private void heapifyUp(int index) {
        int parentIndex = (index - 1) / 2;
        while (index > 0 && heap.get(index).costEstimate < (heap.get(parentIndex)).costEstimate) {
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
    }

    private void heapifyDown(int index) {
        int leftChildIndex = 2 * index + 1;
        int rightChildIndex = 2 * index + 2;
        int smallestIndex = index;
        if (leftChildIndex < heap.size() && heap.get(leftChildIndex).costEstimate < (heap.get(smallestIndex)).costEstimate) {
            smallestIndex = leftChildIndex;
        }
        if (rightChildIndex < heap.size() && heap.get(rightChildIndex).costEstimate < (heap.get(smallestIndex)).costEstimate) {
            smallestIndex = rightChildIndex;
        }
        if (smallestIndex != index) {
            swap(index, smallestIndex);
            heapifyDown(smallestIndex);
        }
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    private void swap(int i, int j) {
        Tile temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }


    private static void moveElementToStart(ArrayList<Tile> list, int index) {
        if (index >= 0 && index < list.size()) {
            Tile element = list.remove(index);
            list.add(0, element);
        }
    }

    private Tile findMinimum() {
        Tile min = heap.get(0);
        for (int i = 1; i < heap.size(); i++) {
            if (heap.get(i).costEstimate < min.costEstimate){
                min = heap.get(i);
            }
        }
        return min;
    }
}

