/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.architecture.type;

import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.architecture.type.generator.TypeGeneratorManager;
import org.panda_lang.language.interpreter.source.PandaClassSource;

public final class PandaTypeUtils {

    private PandaTypeUtils() { }

    public static Type of(Module module, Class<?> type) {
        return of(module, type.getSimpleName(), type);
    }

    public static Type of(Module module, String name, Class<?> javaType) {
        return module.add(PandaType.builder()
                    .name(name)
                    .module(module)
                    .javaType(javaType)
                    .visibility(Visibility.OPEN)
                    .state(State.DEFAULT)
                    .model(javaType.isInterface() ? TypeModels.INTERFACE : TypeModels.CLASS)
                    .location(new PandaClassSource(javaType).toLocation())
                    .build());
    }

    public static Type generateOf(Module module, Class<?> type) {
        return generateOf(module, type.getSimpleName(), type);
    }

    public static Type generateOf(Module module, String name, Class<?> type) {
        return TypeGeneratorManager.getInstance().generate(module, name, type);
    }

}
