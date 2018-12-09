package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;

public class PandaExpressionFailure extends PandaParserFailure {

    public PandaExpressionFailure(String message, ParserData data) {
        super(message, data);
    }

}
