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

package org.panda_lang.framework.language.architecture.prototype.generator;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.Reference;

public class PrototypeGeneratorManager {

    private static final PrototypeGeneratorManager INSTANCE = new PrototypeGeneratorManager();
    private static final PrototypeGenerator GENERATOR = new PrototypeGenerator();

    public Reference generate(Module module, Class<?> clazz, String name) {
        return GENERATOR.generate(module, clazz, name);
    }

    public static PrototypeGeneratorManager getInstance() {
        return INSTANCE;
    }

}
