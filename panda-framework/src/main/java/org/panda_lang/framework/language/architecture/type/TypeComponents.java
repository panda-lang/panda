/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.architecture.type;

import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.interpreter.parser.ContextComponent;

public final class TypeComponents {

    public static final ContextComponent<Type> PROTOTYPE = ContextComponent.of("type", Type.class);

    public static final ContextComponent<TypeScope> PROTOTYPE_SCOPE = ContextComponent.of("type-scope", TypeScope.class);

    private TypeComponents() { }

}
