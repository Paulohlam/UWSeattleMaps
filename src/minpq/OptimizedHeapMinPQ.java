package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class OptimizedHeapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of element-priority pairs.
     */
    private final List<PriorityNode<E>> elements;
    /**
     * {@link Map} of each element to its associated index in the {@code elements} heap.
     */
    private final Map<E, Integer> elementsToIndex;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        elements = new ArrayList<>();
        elementsToIndex = new HashMap<>();
    }

    // Beginning of helper methods
    private int getParent(int index) {
        return (index - 1) / 2;
    }

    private int leftChild(int index) {
        return 2 * index + 1;
    }

    private int rightChild(int index) {
        return 2 * index + 2;
    }

    private boolean isRoot(int index) {
        return index == 0;
    }

    private boolean accessible(int index) {
        return index >= 0 && index < size();
    }

    private void swimsUp(int elementIndex) {
        int parentIndex;
        boolean valid = true;

        while (valid) {
            parentIndex = getParent(elementIndex);

            if (isRoot(elementIndex) || elements.get(parentIndex).getPriority() <= elements.get(elementIndex).getPriority()) {
                valid = false;
            } else {
                swap(parentIndex, elementIndex);
                elementIndex = parentIndex;
            }
        }
    }

    private void sinksDown(int elementIndex) {
        int leftIndex, rightIndex, chosenIndex;
        boolean valid = true;

        while (valid) {
            leftIndex = leftChild(elementIndex);
            rightIndex = rightChild(elementIndex);

            if (!accessible(leftIndex) && !accessible(rightIndex) ||
                    accessible(leftIndex) && accessible(rightIndex) &&
                            elements.get(elementIndex).getPriority() <= elements.get(leftIndex).getPriority() &&
                            elements.get(elementIndex).getPriority() <= elements.get(rightIndex).getPriority() ||
                    !accessible(leftIndex) && accessible(rightIndex) &&
                            elements.get(elementIndex).getPriority() <= elements.get(rightIndex).getPriority() ||
                    accessible(leftIndex) && !accessible(rightIndex) &&
                            elements.get(elementIndex).getPriority() <= elements.get(leftIndex).getPriority()) {
                valid = false;
            } else {
                if (!accessible(leftIndex) || !accessible(rightIndex)) {
                    chosenIndex = !accessible(leftIndex) ? rightIndex : leftIndex;
                } else {
                    chosenIndex = elements.get(leftIndex).getPriority() < elements.get(rightIndex).getPriority() ? leftIndex : rightIndex;
                }

                swap(elementIndex, chosenIndex);
                elementIndex = chosenIndex;
            }
        }
    }

    private void swap(int index1, int index2) {
        elementsToIndex.put(elements.get(index1).getElement(), index2);
        elementsToIndex.put(elements.get(index2).getElement(), index1);

        PriorityNode<E> temp = elements.get(index1);
        elements.set(index1, elements.get(index2));
        elements.set(index2, temp);
    }
    // End of Helper Methods
    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }

        elementsToIndex.put(element, size());
        elements.add(new PriorityNode<>(element, priority));
        swimsUp(size() - 1);
    }

    @Override
    public boolean contains(E element) {
        return elementsToIndex.containsKey(element);
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return elements.get(0).getElement();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }

        E smallestElement = peekMin();
        swap(0, size() - 1);
        elements.remove(size() - 1);
        elementsToIndex.remove(smallestElement);
        sinksDown(0);

        return smallestElement;
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }

        int elementIndex = elementsToIndex.get(element);
        swap(elementIndex, size() - 1);
        elements.remove(size() - 1);
        sinksDown(elementIndex);
        elementsToIndex.remove(element);
        add(element, priority);
    }

    @Override
    public int size() {
        return elements.size();
    }
}
