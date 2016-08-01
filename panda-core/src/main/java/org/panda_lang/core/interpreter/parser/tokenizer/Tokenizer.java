package org.panda_lang.core.interpreter.parser.tokenizer;

import java.util.Iterator;

public interface Tokenizer<T extends Token> extends Iterable<T>, Iterator<T> {

    int getCaretPosition();

    int getLine();

    char[] getSource();

}
