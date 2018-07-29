/*
 * Copyright (c) 2015-2018 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.framework.design.interpreter.parser;

import org.panda_lang.panda.*;
import org.panda_lang.panda.framework.design.architecture.*;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.*;
import org.panda_lang.panda.framework.design.architecture.module.*;
import org.panda_lang.panda.framework.design.architecture.statement.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.*;

/**
 * Default list of names used by {@link org.panda_lang.panda.framework.design.interpreter.parser.ParserData} for components
 */
public class PandaComponents {

    public static final Component<Panda> PANDA = Component.of("panda", Panda.class);

    public static final Component<ScopeLinker> SCOPE_LINKER = Component.of("panda-scope-linker", ScopeLinker.class);

    public static final Component<ModuleRegistry> MODULE_REGISTRY = Component.of("panda-module-registry", ModuleRegistry.class);

    public static final Component<PandaScript> PANDA_SCRIPT = Component.of("panda-script", PandaScript.class);

    public static final Component<AbyssRedactor> REDACTOR = Component.of("panda-redactor", AbyssRedactor.class);

    public static final Component<Container> CONTAINER = Component.of("panda-container", Container.class);

    public static final Component<Scope> SCOPE = Component.of("panda-scope", Scope.class);

}
