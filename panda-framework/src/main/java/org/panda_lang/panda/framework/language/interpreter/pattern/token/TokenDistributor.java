package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class TokenDistributor implements Iterable<TokenRepresentation> {

    private final TokenizedSource source;
    private int index;

    public TokenDistributor(TokenizedSource source) {
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

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean hasNext() {
        return index < length();
    }

    public int getIndex() {
        return index;
    }

    public int length() {
        return source.size();
    }

}
