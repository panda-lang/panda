package org.panda_lang.panda.framework.language.interpreter.token.distributors;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class TokenDistributor implements Iterable<TokenRepresentation> {

    private final Tokens source;
    private int index;

    public TokenDistributor(Tokens source) {
        this.source = source;
    }

    @Override
    public Iterator<TokenRepresentation> iterator() {
        return new Iterator<TokenRepresentation>() {
            private int i = index;

            @Override
            public boolean hasNext() {
                return i < source.size();
            }

            @Override
            public TokenRepresentation next() {
                return source.get(i++);
            }
        };
    }

    public TokenRepresentation next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return source.get(index++);
    }

    public List<TokenRepresentation> next(int size) {
        List<TokenRepresentation> representations = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            representations.add(next());
        }

        return representations;
    }

    public @Nullable TokenRepresentation current() {
        return source.get(index);
    }

    public Tokens currentSubSource() {
        return subSource(getIndex(), size());
    }

    public Tokens subSource(int startIndex, int endIndex) {
        return source.subSource(startIndex, endIndex);
    }

    public int size() {
        return source.size();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean hasNext() {
        return index < size();
    }

    public @Nullable TokenRepresentation get(int index) {
        return source.get(index);
    }

    public @Nullable TokenRepresentation getNext() {
        return source.hasElement(index) ? source.get(index) : null;
    }

    public int getIndex() {
        return index;
    }

    public Tokens getSource() {
        return source;
    }

}
