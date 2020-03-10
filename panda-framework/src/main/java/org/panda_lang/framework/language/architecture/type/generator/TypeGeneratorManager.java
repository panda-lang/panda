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

package org.panda_lang.framework.language.architecture.type.generator;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.type.Type;

public final class TypeGeneratorManager {

    private static final TypeGeneratorManager INSTANCE = new TypeGeneratorManager();

    private final TypeGenerator generator = new TypeGenerator();

    public Type generate(Module module, String name, Class<?> clazz) {
        boolean exists = module.forClass(clazz).isDefined();
        Type reference = generator.generate(module, name, clazz);

        if (!exists) {
            module.add(reference);
        }

        return reference;
    }

    public void disposeCache() {
        generator.disposeCache();
    }

    protected TypeGenerator getGenerator() {
        return generator;
    }

    public int getCacheSize() {
        return generator.initializedTypes.size();
    }

    public static TypeGeneratorManager getInstance() {
        return INSTANCE;
    }

}
