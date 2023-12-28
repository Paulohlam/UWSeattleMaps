package minpq;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Unsorted array (or {@link ArrayList}) implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class UnsortedArrayMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the element-priority pairs in no specific order.
     */
    private final List<PriorityNode<E>> elements;

    /**
     * Constructs an empty instance.
     */
    public UnsortedArrayMinPQ() {
        elements = new ArrayList<>();
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }

        elements.add(new PriorityNode<>(element, priority));
    }

    @Override
    public boolean contains(E element) {
        for (PriorityNode<E> node : elements) {
            if (node.getElement() != null && node.getElement().equals(element)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }

        PriorityNode<E> smallestNode = elements.get(0);

        for (PriorityNode<E> node : elements) {
            if (node.getPriority() < smallestNode.getPriority()) {
                smallestNode = node;
            }
        }

        return smallestNode.getElement();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }

        PriorityNode<E> smallestNode = elements.get(0);

        for (PriorityNode<E> node : elements) {
            if (node.getPriority() < smallestNode.getPriority()) {
                smallestNode = node;
            }
        }

        elements.remove(smallestNode);
        return smallestNode.getElement();
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }

        for (PriorityNode<E> node : elements) {
            if (node.getElement() != null && node.getElement().equals(element)) {
                node.setPriority(priority);
                break;
            }
        }
    }

    @Override
    public int size() {
        return elements.size();
    }
}
