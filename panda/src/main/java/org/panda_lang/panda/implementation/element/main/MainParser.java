package org.panda_lang.panda.implementation.element.main;

import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.linker.WrapperLinker;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.framework.structure.Statement;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenExtractor;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.parser.OverallParser;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.implementation.interpreter.parser.ParserRegistration;

import java.util.List;

@ParserRegistration(parserClass = MainParser.class, handlerClass = MainParserHandler.class)
public class MainParser implements UnifiedParser {

    protected static final TokenPattern pattern = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "main")
            .unit(TokenType.SEPARATOR, "{")
            .gap()
            .unit(TokenType.SEPARATOR, "}")
            .build();

    @Override
    public Statement parse(ParserInfo parserInfo) {
        Main main = new Main();

        TokenReader tokenReader = parserInfo.getComponent(Components.READER);
        TokenExtractor extractor = pattern.extractor();
        boolean matched = extractor.extract(tokenReader);
        List<TokenizedSource> hollows = extractor.getGaps();

        if (!matched) {
            throw new PandaParserException("Mismatched parser to the specified source");
        }

        TokenizedSource body = hollows.get(0);
        TokenReader bodyReader = new PandaTokenReader(body);

        OverallParser overallParser = new OverallParser(parserInfo, bodyReader);
        WrapperLinker wrapperLinker = parserInfo.getComponent(Components.LINKER);
        wrapperLinker.pushWrapper(main);

        for (Statement statement : overallParser) {
            main.addStatement(statement);
        }

        return main;
    }

}
