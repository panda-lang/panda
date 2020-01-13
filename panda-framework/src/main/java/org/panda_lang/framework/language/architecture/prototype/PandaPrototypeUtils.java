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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.PrototypeModels;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.architecture.prototype.State;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.language.architecture.prototype.dynamic.PandaDynamicClass;
import org.panda_lang.framework.language.architecture.prototype.generator.PrototypeGeneratorManager;
import org.panda_lang.framework.language.interpreter.source.PandaClassSource;

public final class PandaPrototypeUtils {

    private PandaPrototypeUtils() { }

    public static Reference of(Module module, Class<?> type) {
        return of(module, type.getSimpleName(), type);
    }

    public static Reference of(Module module, String name, Class<?> type) {
        return module.add(new PandaReference(new PandaDynamicClass(type, name, module.getName()), module, reference -> {
            PandaPrototype prototype = PandaPrototype.builder()
                    .name(name)
                    .reference(reference)
                    .module(module)
                    .associated(reference.getAssociatedClass())
                    .visibility(Visibility.PUBLIC)
                    .state(State.DEFAULT)
                    .model(type.isInterface() ? PrototypeModels.INTERFACE : PrototypeModels.CLASS)
                    .location(new PandaClassSource(type).toLocation())
                    .build();

            prototype.getAssociatedClass().append(type);
            return prototype;
        }
        ));
    }

    public static Reference generateOf(Module module, Class<?> type) {
        return generateOf(module, type.getSimpleName(), type);
    }

    public static Reference generateOf(Module module, String name, Class<?> type) {
        return PrototypeGeneratorManager.getInstance().generate(module, name, type);
    }

}
