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

package org.panda_lang.panda.language.interpreter.parsers.prototype.mapper.generator;

import org.panda_lang.panda.design.architecture.module.*;
import org.panda_lang.panda.framework.design.architecture.module.*;
import org.panda_lang.panda.framework.design.architecture.prototype.*;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.*;
import org.panda_lang.panda.framework.design.architecture.value.*;
import org.panda_lang.panda.framework.design.runtime.*;

import java.lang.reflect.*;

public class ClassPrototypeConstructorGenerator {

    private final Class<?> type;
    private final ClassPrototype prototype;
    private final Constructor<?> constructor;

    public ClassPrototypeConstructorGenerator(Class<?> type, ClassPrototype prototype, Constructor<?> constructor) {
        this.type = type;
        this.prototype = prototype;
        this.constructor = constructor;
    }

    public PrototypeConstructor generate(ModuleRegistry registry) {
        ClassPrototype[] parameters = new ClassPrototype[constructor.getParameterCount()];

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = PandaModuleRegistryAssistant.forClass(registry, constructor.getParameterTypes()[i]);
        }

        // TODO: Generate bytecode
        PrototypeConstructor generatedConstructor = new PrototypeConstructor() {
            @Override
            public Object createInstance(ExecutableBranch bridge, Value... values) {
                try {
                    Object[] args = new Object[values.length];

                    for (int i = 0; i < values.length; i++) {
                        args[i] = values[i].getValue();
                    }

                    return constructor.newInstance(args);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public ClassPrototype[] getParameterTypes() {
                return parameters;
            }
        };

        return generatedConstructor;
    }

}
