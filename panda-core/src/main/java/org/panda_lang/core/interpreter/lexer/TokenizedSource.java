package org.panda_lang.core.interpreter.lexer;

public interface TokenizedSource {

    default int size() {
        return getSource().length;
    }

    TokenRepresentation[] getSource();

}
