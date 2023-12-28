package minpq;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * {@link PriorityQueue} implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class HeapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link PriorityQueue} storing {@link PriorityNode} objects representing each element-priority pair.
     */
    private final PriorityQueue<PriorityNode<E>> pq;

    /**
     * Constructs an empty instance.
     */
    public HeapMinPQ() {
        pq = new PriorityQueue<>(Comparator.comparingDouble(PriorityNode::getPriority));
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }

        pq.add(new PriorityNode<>(element, priority));
    }

    @Override
    public boolean contains(E element) {
        for (PriorityNode<E> currentNode : pq) {
            if (currentNode.getElement().equals(element)) {
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

        PriorityNode<E> minNode = pq.peek();
        if (minNode != null) {
            return minNode.getElement();
        } else {
            throw new NoSuchElementException("PQ is empty");
        }
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }

        PriorityNode<E> minNode = pq.poll();
        if (minNode != null) {
            return minNode.getElement();
        } else {
            throw new NoSuchElementException("PQ is empty");
        }
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }
        for (PriorityNode<E> currentNode : pq) {
            if (currentNode.getElement().equals(element)) {
                pq.remove(currentNode);
                currentNode.setPriority(priority);
                pq.add(currentNode);

                break;
            }
        }
    }

    @Override
    public int size() {
        return pq.size();
    }
}
