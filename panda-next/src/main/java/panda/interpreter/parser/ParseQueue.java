package panda.interpreter.parser;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Queue;
import java.util.TreeMap;

public class ParseQueue {

    private final TreeMap<Scope, Queue<Runnable>> queue = new TreeMap<>(Comparator.naturalOrder());
    
    public void addToQueue(Scope scope, Runnable task) {
        queue.computeIfAbsent(scope, key -> new ArrayDeque<>()).add(task);
    }

    /**
     * Returns false when there's no more tasks to execute
     */
    public boolean parseNext() {
        for (Queue<Runnable> scopeQueue : queue.values()) {
            if (scopeQueue.isEmpty()) {
                continue;
            }

            scopeQueue.poll().run();
            return true;
        }

        return false;
    }
    
}