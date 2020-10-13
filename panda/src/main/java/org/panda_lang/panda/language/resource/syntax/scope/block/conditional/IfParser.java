package org.panda_lang.panda.language.resource.syntax.scope.block.conditional;

import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.resource.syntax.scope.block.BlockParser;
import org.panda_lang.utilities.commons.function.Option;

import java.util.concurrent.CompletableFuture;

public final class IfParser extends BlockParser<ConditionalBlock> {

    private static final ConditionalParser CONDITIONAL_PARSER = new ConditionalParser();

    @Override
    public String name() {
        return "if";
    }

    @Override
    public Option<CompletableFuture<ConditionalBlock>> parse(Context<Object> context) {
        return CONDITIONAL_PARSER
                .parse(SCOPE_PARSER, context, Keywords.IF, true)
                .map(CompletableFuture::completedFuture);
    }

}
