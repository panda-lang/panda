package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

public class BootstrapUtils {

    public static ParserData updated(ParserData data) {
        return data.setComponent(UniversalComponents.SOURCE_STREAM, new PandaSourceStream(data.getComponent(BootstrapComponents.CURRENT_SOURCE)));
    }

}
