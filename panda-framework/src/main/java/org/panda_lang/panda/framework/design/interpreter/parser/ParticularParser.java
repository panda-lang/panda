package org.panda_lang.panda.framework.design.interpreter.parser;

import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;

public interface ParticularParser<T> extends Parser {

    /**
     * @param source matched and checked before tokenized source for parser
     * @param info set of information about source and interpretation process
     */
    T parse(ParserInfo info, TokenizedSource source);

}
