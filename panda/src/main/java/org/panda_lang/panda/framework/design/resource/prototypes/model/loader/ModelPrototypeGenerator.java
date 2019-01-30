package org.panda_lang.panda.framework.design.resource.prototypes.model.loader;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.framework.design.resource.prototypes.model.ClassPrototypeModel;

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

        Module module = moduleReference.orElseGet(() -> modulePath.create(moduleName));

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
