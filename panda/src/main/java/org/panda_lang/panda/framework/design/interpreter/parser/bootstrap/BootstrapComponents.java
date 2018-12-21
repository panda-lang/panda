package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.component.Component;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;

public class BootstrapComponents {

    public static final String CURRENT_SOURCE_LABEL = "bootstrap-current-source";

    public static final Component<Tokens> CURRENT_SOURCE = Component.of(CURRENT_SOURCE_LABEL, Tokens.class);

}
