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

package org.panda_lang.panda.framework.design.resource.prototypes.model.loader;

import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.resource.prototypes.model.ClassPrototypeModel;

import java.util.ArrayList;
import java.util.Collection;

class ModelLoader {

    private final ModulePath modulePath;

    public ModelLoader(ModulePath modulePath) {
        this.modulePath = modulePath;
    }

    public Collection<ClassPrototype> load(Collection<Class<? extends ClassPrototypeModel>> models) {
        try {
            return loadModels(models);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<ClassPrototype> loadModels(Collection<Class<? extends ClassPrototypeModel>> models) throws Exception {
        Collection<ClassPrototype> loaded = new ArrayList<>();
        ModelPrototypeGenerator generator = new ModelPrototypeGenerator();

        for (Class<? extends ClassPrototypeModel> modelClass : models) {
            ClassPrototype prototype = generator.generate(modulePath, modelClass);

            if (prototype == null) {
                continue;
            }

            loaded.add(prototype);
        }

        return loaded;
    }

}
