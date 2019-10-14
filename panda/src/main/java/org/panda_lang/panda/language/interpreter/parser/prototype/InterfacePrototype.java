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

package org.panda_lang.panda.language.interpreter.parser.prototype;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.State;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.source.Source;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototype;

public class InterfacePrototype extends PandaPrototype {

    public InterfacePrototype(Module module, String className, Source source, Class<?> associated, Visibility visibility) {
        super(module, className, source, associated, State.ABSTRACT, visibility);
    }

    public InterfacePrototype(PandaPrototypeBuilder<?, ?> builder) {
        super(builder);
    }

}