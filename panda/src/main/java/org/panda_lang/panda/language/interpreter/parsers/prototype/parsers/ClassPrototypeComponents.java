package org.panda_lang.panda.language.interpreter.parsers.prototype.parsers;

import org.panda_lang.panda.framework.design.architecture.prototype.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.language.interpreter.parsers.prototype.scope.*;

public class ClassPrototypeComponents {

    public static final Component<ClassPrototype> CLASS_PROTOTYPE = Component.of("panda-class-prototype", ClassPrototype.class);

    public static final Component<ClassScope> CLASS_SCOPE = Component.of("panda-class-scope", ClassScope.class);

}
