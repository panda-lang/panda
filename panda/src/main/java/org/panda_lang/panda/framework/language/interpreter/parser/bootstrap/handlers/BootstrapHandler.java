package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.handlers;

import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.PandaParserBootstrap;

public interface BootstrapHandler extends ParserHandler {

    void initialize(PandaParserBootstrap bootstrap);

}
