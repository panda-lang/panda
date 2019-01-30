package org.panda_lang.panda.utilities.commons.iterable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.IntFunction;

public class ResourcesIterable<T> implements Iterable<T> {

    private final Iterable<T>[] iterables;

    @SafeVarargs
    public ResourcesIterable(Iterable<T>... iterables) {
        if (iterables.length == 0) {
            throw new IllegalArgumentException("ResourcesIterable requires at least one resource");
        }

        this.iterables = iterables;
    }

    @Override
    public Iterator<T> iterator() {
        return new ResourceIterator();
    }

    class ResourceIterator implements Iterator<T> {

        private final Iterator<T>[] iterators = Arrays.stream(iterables)
                .map(Iterable::iterator)
                .toArray((IntFunction<Iterator<T>[]>) Iterator[]::new);

        private int selected;
        private int index;

        public ResourceIterator() {
            this.selectNext();
        }

        @Override
        public boolean hasNext() {
            return iterators[selected].hasNext();
        }

        @Override
        public T next() {
            T value = iterators[selected].next();

            if (!iterators[selected].hasNext()) {
                selectNext();
            }

            return value;
        }

        private void selectNext() {
            for (int i = selected; i < iterators.length; i++) {
                Iterator<T> iterator = iterators[i];

                if (iterator.hasNext()) {
                    selected = i;
                    index = 0;
                    break;
                }
            }
        }

    }

}
