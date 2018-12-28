package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.interceptor;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedBootstrapParser;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.design.interpreter.pattern.PandaTokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPatternMapping;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;

public class TokenPatternInterceptor implements BootstrapInterceptor {

    private TokenPattern pattern;

    @Override
    public void initialize(PandaParserBootstrap bootstrap) {
        this.pattern = PandaTokenPattern.builder()
                .compile(bootstrap.getPattern().toString())
                .build();
    }

    @Override
    public InterceptorData handle(UnifiedBootstrapParser parser, ParserData data) {
        InterceptorData interceptorData = new InterceptorData();

        if (pattern != null) {
            ExtractorResult result = pattern.extract(data.getComponent(UniversalComponents.SOURCE_STREAM));

            if (!result.isMatched()) {
                data.getComponent(UniversalComponents.SOURCE_STREAM).updateCachedSource();
                throw new PandaParserFailure("Interceptor could not match token pattern, error: " + result.getErrorMessage(), data);
            }

            interceptorData.addElement(new TokenPatternMapping(result));
            interceptorData.addElement(result);
        }

        return interceptorData;
    }

}
