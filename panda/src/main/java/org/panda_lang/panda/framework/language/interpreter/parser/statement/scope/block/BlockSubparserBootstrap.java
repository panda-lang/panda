package org.panda_lang.panda.framework.language.interpreter.parser.statement.scope.block;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

public abstract class BlockSubparserBootstrap extends UnifiedParserBootstrap<BlockData> implements BlockSubparser {

    @Override
    public final BlockData parse(ParserData data, Tokens declaration) throws Throwable {
        return super.parse(data.setComponent(UniversalComponents.SOURCE_STREAM, new PandaSourceStream(declaration)));
    }

}
