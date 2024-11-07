package p12.exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q> {

    private final Map<Q, Queue<T>> queues = new HashMap<>();

    @Override
    public Set<Q> availableQueues() {
        return queues.keySet();
    }

    @Override
    public void openNewQueue(Q queue) {
        if (queues.containsKey(queue)) {
            throw new IllegalArgumentException("Queue already available");
        }
        queues.put(queue, new LinkedList<>());
    }

    @Override
    public boolean isQueueEmpty(Q queue) {
        Queue<T> targetQueue = queues.get(queue);
        if (targetQueue == null) {
            throw new IllegalArgumentException("Queue not available");
        }
        return targetQueue.isEmpty();
    }

    @Override
    public void enqueue(T elem, Q queue) {
        Queue<T> targetQueue = queues.get(queue);
        if (targetQueue == null) {
            throw new IllegalArgumentException("Queue not available");
        }
        targetQueue.add(elem);
    }

    @Override
    public T dequeue(Q queue) {
        Queue<T> targetQueue = queues.get(queue);
        if (targetQueue == null) {
            throw new IllegalArgumentException("Queue not available");
        }
        return targetQueue.isEmpty() ? null : targetQueue.poll();
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        Map<Q, T> result = new HashMap<>();
        for(Q queue : queues.keySet()) {
            result.put(queue, queues.get(queue).poll());
        }
        return result;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        Set<T> allElements = new HashSet<>();
        for (Queue<T> queueList : queues.values()) {
            allElements.addAll(queueList);
        }
        return allElements;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        Queue<T> targetQueue = queues.get(queue);
        if (targetQueue == null) {
            throw new IllegalArgumentException("Queue not available");
        }
        List<T> allElements = new ArrayList<>(targetQueue);
        targetQueue.clear();
        return allElements;
    }

    @Override
    public void closeQueueAndReallocate(Q queue) { 
        Queue<T> targetQueue = queues.get(queue);
        if (targetQueue == null) {
            throw new IllegalArgumentException("Queue not available");
        }
        queues.remove(queue);
    
        Queue<T> alternativeQueue = null;
        for (Queue<T> q : queues.values()) {
            if (q != targetQueue) {
                alternativeQueue = q;
                break;
            }
        }
    
        if (alternativeQueue == null) {
            throw new IllegalStateException("No alternative queue available");
        }
    
        alternativeQueue.addAll(targetQueue);
    }    
}
