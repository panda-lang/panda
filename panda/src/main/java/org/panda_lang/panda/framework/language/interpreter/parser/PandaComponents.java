/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.component.Component;

/**
 * Default list of names used by {@link org.panda_lang.panda.framework.design.interpreter.parser.Context} for components
 */
public class PandaComponents {

    public static final Component<Panda> PANDA = Component.of("panda", Panda.class);

    public static final Component<PandaScript> PANDA_SCRIPT = Component.of("panda-script", PandaScript.class);

    public static final Component<Container> CONTAINER = Component.of("panda-container", Container.class);

    public static final Component<Scope> SCOPE = Component.of("panda-scope", Scope.class);

}
