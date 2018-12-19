package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.component.Component;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;

public class BootstrapComponents {

    public static Component<Tokens> CURRENT_SOURCE = Component.of("current-source", Tokens.class);

}
