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

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.type.TypeModels;
import org.panda_lang.framework.design.architecture.type.Reference;
import org.panda_lang.framework.design.architecture.type.State;
import org.panda_lang.framework.design.architecture.type.Visibility;
import org.panda_lang.framework.language.architecture.type.dynamic.PandaDynamicClass;
import org.panda_lang.framework.language.architecture.type.generator.TypeGeneratorManager;
import org.panda_lang.framework.language.interpreter.source.PandaClassSource;

public final class PandaTypeUtils {

    private PandaTypeUtils() { }

    public static Reference of(Module module, Class<?> type) {
        return of(module, type.getSimpleName(), type);
    }

    public static Reference of(Module module, String name, Class<?> javaType) {
        return module.add(new PandaReference(new PandaDynamicClass(javaType, name, module.getName()), module, reference -> {
            PandaType type = PandaType.builder()
                    .name(name)
                    .reference(reference)
                    .module(module)
                    .associated(reference.getAssociatedClass())
                    .visibility(Visibility.PUBLIC)
                    .state(State.DEFAULT)
                    .model(javaType.isInterface() ? TypeModels.INTERFACE : TypeModels.CLASS)
                    .location(new PandaClassSource(javaType).toLocation())
                    .build();

            type.getAssociatedClass().append(javaType);
            return type;
        }
        ));
    }

    public static Reference generateOf(Module module, Class<?> type) {
        return generateOf(module, type.getSimpleName(), type);
    }

    public static Reference generateOf(Module module, String name, Class<?> type) {
        return TypeGeneratorManager.getInstance().generate(module, name, type);
    }

}
