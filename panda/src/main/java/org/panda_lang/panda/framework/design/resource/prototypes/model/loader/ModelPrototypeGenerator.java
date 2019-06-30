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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.resource.prototypes.model.ClassPrototypeModel;
import org.panda_lang.panda.framework.language.architecture.module.PandaModule;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.PandaClassPrototype;

import java.lang.reflect.Method;
import java.util.Optional;

class ModelPrototypeGenerator {

    private static final ModelMethodGenerator GENERATOR = new ModelMethodGenerator();

    public @Nullable ClassPrototype generate(ModulePath modulePath, Class<? extends ClassPrototypeModel> modelClass) throws Exception {
        ClassPrototypeModel.ModuleDeclaration moduleDeclaration = modelClass.getAnnotation(ClassPrototypeModel.ModuleDeclaration.class);
        ClassPrototypeModel.ClassDeclaration classDeclaration = modelClass.getAnnotation(ClassPrototypeModel.ClassDeclaration.class);

        String moduleName = moduleDeclaration.value();
        Optional<Module> moduleReference = modulePath.get(moduleName);

        if (moduleReference.isPresent() && moduleReference.get().get(classDeclaration.value()).isPresent()) {
            return null;
        }

        Module module = moduleReference.orElseGet(() -> modulePath.include(new PandaModule(moduleName)));

        ClassPrototype prototype = PandaClassPrototype.builder()
                .module(module)
                .name(classDeclaration.value())
                .associated(modelClass)
                .build();

        module.add(prototype.getReference());
        Class.forName(modelClass.getName());

        for (Method method : modelClass.getMethods()) {
            GENERATOR.generate(modulePath, modelClass, prototype, method);
        }

        return prototype;
    }

}
