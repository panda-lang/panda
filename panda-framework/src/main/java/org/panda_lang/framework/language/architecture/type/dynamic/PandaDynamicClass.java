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

package org.panda_lang.framework.language.architecture.type.dynamic;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import org.panda_lang.framework.design.architecture.type.DynamicClass;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeModels;
import org.panda_lang.framework.language.architecture.type.TypeInstance;
import org.panda_lang.utilities.commons.ClassPoolUtils;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.PackageUtils;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.ValidationUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class PandaDynamicClass implements DynamicClass {

    private static final CtClass PROTOTYPE_CLASS = ClassPoolUtils.require(TypeInstance.class);

    protected final Type type;
    protected final String name;
    protected final String module;
    protected final String model;

    protected final AtomicInteger id = new AtomicInteger();
    protected final Collection<DynamicClass> interfaces = new ArrayList<>();
    protected DynamicClass superclass = null;
    protected boolean frozen;

    protected Class<?> structure;
    protected Class<?> implementation;
    protected boolean changedStructure = true;
    protected boolean changedImplementation = true;

    public PandaDynamicClass(Type type, String name, String module, String model) {
        this.type = ValidationUtils.notNull(type, "Type cannot be null");
        this.name = ValidationUtils.notNull(name, "Name cannot be null");
        this.model = ValidationUtils.notNull(model, "Model cannot be null");
        this.module = StringUtils.replace(ValidationUtils.notNull(module, "Module cannot be null"), ":", ".");
    }

    public PandaDynamicClass(Type type, Class<?> clazz, String customName, String module) {
        this(type, customName, module, TypeModels.of(clazz));
        this.structure = clazz;
        this.implementation = clazz;
        this.frozen = true;
    }

    public PandaDynamicClass(Type type, Class<?> clazz) {
        this(type, clazz, clazz.getSimpleName(), PackageUtils.toString(clazz.getPackage(), StringUtils.EMPTY));
    }

    private synchronized void recreateStructure() throws DynamicClassException {
        if (structure != null && (frozen || !changedStructure)) {
            return;
        }

        this.changedStructure = false;
        String className = "IPanda_" + name.replace("::", "_");

        if (structure == null) {
            CtClass generatedStructure = ClassPool.getDefault().makeInterface(className, PROTOTYPE_CLASS);

            try {
                generatedStructure.writeFile(".dynamic_classes");
                this.structure =  ClassPoolUtils.toClass(generatedStructure);
            } catch (CannotCompileException e) {
                e.printStackTrace();
                throw new DynamicClassException(e.getCause());
            } catch (Exception e) {
                e.printStackTrace();
                throw new DynamicClassException(e.getMessage());
            }
        }
    }

    private synchronized void recreateImplementation() throws DynamicClassException {
        if (implementation != null && (frozen || !changedImplementation)) {
            return;
        }

        recreateStructure();
        CtClass superclassCt = null;

        if (superclass != null) {
            superclassCt = ClassPoolUtils.require(superclass.fetchImplementation());
        }

        String generatedClassName = "Panda_" + name.replace("::", "_") + "$" + id.getAndIncrement();
        CtClass generatedImplementation;

        if (TypeModels.isInterface(type)) {
            generatedImplementation = ClassPool.getDefault().makeInterface("I" + generatedClassName, superclassCt);
        }
        else if (superclassCt != null && superclassCt.isInterface()) {
            generatedImplementation = ClassPool.getDefault().makeClass(generatedClassName);
            generatedImplementation.addInterface(superclassCt);
        }
        else {
            generatedImplementation = ClassPool.getDefault().makeClass(generatedClassName, superclassCt);
        }

        DynamicClassGenerator generator = new DynamicClassGenerator(type, generatedImplementation);

        try {
            generator.generateDeclaration();
            generator.generateFields();
            generator.generateConstructor();
            generator.generateInstanceMethods();
            this.implementation = generator.generate();
            generatedImplementation.writeFile(".dynamic_classes");
        } catch (CannotCompileException e) {
            e.printStackTrace();
            throw new DynamicClassException(e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DynamicClassException(e.getMessage());
        }
    }

    @Override
    public DynamicClass append(Type toAppend) {
        if (TypeModels.isInterface(toAppend)) {
            return implementInterface(toAppend.getAssociatedClass());
        }
        else {
            extendClass(toAppend.getAssociatedClass());

            if (toAppend.getName().equals("java::Object")) {
                this.changedStructure = false;
                this.changedImplementation = false;
            }
        }

        return this;
    }

    @Override
    public DynamicClass extendClass(DynamicClass superclass) {
        if (this.superclass != null) {
            throw new IllegalStateException("Class cannot extend more than one class");
        }

        return update(() -> this.superclass = superclass);
    }

    @Override
    public DynamicClass implementInterface(DynamicClass interfaceClass) {
        return update(() -> interfaces.add(interfaceClass));
    }

    private DynamicClass update(Runnable action) {
        action.run();
        this.changedStructure = true;
        this.changedImplementation = true;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, module, model);
    }

    @Override
    public boolean equals(Object o) { // lgtm [java/unchecked-cast-in-equals]
        DynamicClass that = ObjectUtils.cast(o);
        return that != null && model.equals(that.getModule()) && name.equals(that.getSimpleName()) && model.equals(that.getModel());
    }

    @Override
    public boolean isAssignableFrom(Class<?> cls) {
        return ClassUtils.isAssignableFrom(implementation, cls);
    }

    @Override
    public boolean isAssignableTo(Class<?> cls) {
        return ClassUtils.isAssignableFrom(cls, implementation);
    }

    @Override
    public boolean isClass() {
        return TypeModels.CLASS.equals(model);
    }

    @Override
    public boolean isInterface() {
        return TypeModels.INTERFACE.equals(model);
    }

    @Override
    public Class<?> fetchImplementation() {
        recreateImplementation();
        return implementation;
    }

    @Override
    public Class<?> fetchStructure() {
        recreateStructure();
        return structure;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getModule() {
        return module;
    }

    @Override
    public String getSimpleName() {
        return name;
    }

    @Override
    public String getName() {
        return getModule() + "." + getSimpleName();
    }

}
