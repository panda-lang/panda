package org.panda_lang.panda.language.structure.general.expression.callbacks.math;

import org.panda_lang.panda.core.interpreter.lexer.extractor.vague.VagueExtractor;
import org.panda_lang.panda.core.interpreter.lexer.extractor.vague.VagueResult;
import org.panda_lang.panda.framework.implementation.interpreter.token.reader.PandaTokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.Parser;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.reader.TokenReader;
import org.panda_lang.panda.language.syntax.tokens.Operators;

public class MathParser implements Parser {

    private static final VagueExtractor EXTRACTOR = new VagueExtractor(Operators.ADDITION, Operators.SUBTRACTION, Operators.DIVISION, Operators.MULTIPLICATION);

    public MathExpression parse(TokenizedSource source, ParserInfo info) {
        TokenReader reader = new PandaTokenReader(source);
        VagueResult result = EXTRACTOR.extract(reader);

        return new MathExpression();
    }

}
