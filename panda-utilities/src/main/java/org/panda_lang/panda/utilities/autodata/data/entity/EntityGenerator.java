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

package org.panda_lang.panda.utilities.autodata.data.entity;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.panda_lang.panda.utilities.autodata.AutomatedDataException;
import org.panda_lang.panda.utilities.autodata.data.repository.DataHandler;
import org.panda_lang.panda.utilities.autodata.data.repository.RepositoryOperationType;
import org.panda_lang.panda.utilities.autodata.data.repository.RepositoryScheme;
import org.panda_lang.panda.utilities.autodata.orm.As;
import org.panda_lang.panda.utilities.autodata.orm.Generated;
import org.panda_lang.panda.utilities.autodata.orm.GenerationStrategy;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.ClassUtils;
import org.panda_lang.panda.utilities.commons.collection.Maps;

import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Optional;

final class EntityGenerator {

    private static final ClassPool CLASS_POOL = ClassPool.getDefault();

    private final CtClass ctDataHandler;

    {
        try {
            ctDataHandler = CLASS_POOL.get(DataHandler.class.getName());
        } catch (NotFoundException e) {
            throw new AutomatedDataException("Class not found: " + e.getMessage());
        }

    }

    @SuppressWarnings("unchecked")
    protected Class<? extends DataEntity> generate(RepositoryScheme repositoryScheme, DataHandler<?> dataHandler) throws NotFoundException, CannotCompileException {
        EntityScheme entityScheme = repositoryScheme.getCollectionScheme().getEntityScheme();
        Class<?> clazz = entityScheme.getRootClass();

        if (!clazz.isInterface()) {
            throw new AutomatedDataException("Entity class is not an interface (source: " + clazz.toGenericString() + ")");
        }

        String name = clazz.getPackage().getName() + ".Controlled" + clazz.getSimpleName();
        Optional<Class<? extends DataEntity>> loadedEntityClass = ClassUtils.forName(name);

        if (loadedEntityClass.isPresent()) {
            return loadedEntityClass.get();
        }

        CtClass entityClass = CLASS_POOL.makeClass(name);
        entityClass.addInterface(CLASS_POOL.get(clazz.getName()));

        generateFields(entityScheme, entityClass);
        generateDefaultConstructor(entityScheme, entityClass);
        generateConstructors(repositoryScheme, entityClass);
        generateMethods(entityScheme, entityClass);

        return (Class<? extends DataEntity>) entityClass.toClass();
    }

    private void generateDefaultConstructor(EntityScheme scheme, CtClass entityClass) throws CannotCompileException {
        CtField field = new CtField(ctDataHandler, "dataHandler", entityClass);
        entityClass.addField(field);

        CtConstructor constructor = new CtConstructor(new CtClass[]{ ctDataHandler }, entityClass);
        StringBuilder bodyBuilder = new StringBuilder("{ this.dataHandler = $1;");

        scheme.getProperties().values().stream()
                .map(property -> Maps.immutableEntryOf(property, property.getAnnotations().getAnnotation(Generated.class)))
                .filter(entry -> entry.getValue().isPresent())
                .forEach(entry -> {
                    EntitySchemeProperty property = entry.getKey();
                    GenerationStrategy strategy = entry.getValue().get().strategy();

                    bodyBuilder.append("this.").append(property.getName()).append(" = (").append(property.getType().getName()).append(")dataHandler.generate(")
                            .append(property.getType().getName()).append(".class, ")
                            .append(GenerationStrategy.class.getName()).append(".").append(strategy.name()).append(");");
                });

        constructor.setBody(bodyBuilder.append("}").toString());
        entityClass.addConstructor(constructor);
    }

    private void generateFields(EntityScheme scheme, CtClass entityClass) throws CannotCompileException, NotFoundException {
        for (EntitySchemeProperty property : scheme.getProperties().values()) {
            CtField field = new CtField(CLASS_POOL.get(property.getType().getName()), property.getName(), entityClass);
            entityClass.addField(field);
        }
    }

    private void generateConstructors(RepositoryScheme repositoryScheme, CtClass entityClass) throws CannotCompileException, NotFoundException {
        for (EntitySchemeMethod method : repositoryScheme.getMethods().getOrDefault(RepositoryOperationType.CREATE, Collections.emptyList())) {
            CtConstructor constructor = generateConstructor(entityClass, method);
            entityClass.addConstructor(constructor);
        }
    }

    private CtConstructor generateConstructor(CtClass entityClass, EntitySchemeMethod method) throws CannotCompileException, NotFoundException {
        Parameter[] parameters = method.getMethod().getParameters();
        Class<?>[] types = method.getMethod().getParameterTypes();
        CtClass[] ctTypes = new CtClass[types.length];

        for (int index = 0; index < types.length; index++) {
            ctTypes[index] = CLASS_POOL.get(types[index].getName());
        }

        CtConstructor constructor = new CtConstructor(ArrayUtils.mergeArrays(new CtClass[] { ctDataHandler }, ctTypes), entityClass);
        StringBuilder bodyBuilder = new StringBuilder("{ this($1);");

        for (int index = 0; index < parameters.length; index++) {
            Parameter parameter = parameters[index];
            As as = parameter.getAnnotation(As.class);

            bodyBuilder.append("this.").append(as.value()).append(" = $").append(index + 2).append(";");
        }

        constructor.setBody(bodyBuilder.append("}").toString());
        return constructor;
    }

    private void generateMethods(EntityScheme entityScheme, CtClass entityClass) throws CannotCompileException, NotFoundException {
        for (EntitySchemeMethod method : entityScheme.getMethods()) {
            CtMethod generatedMethod = generateMethod(entityClass, method);
            entityClass.addMethod(generatedMethod);
        }
    }

    private CtMethod generateMethod(CtClass entityClass, EntitySchemeMethod method) throws CannotCompileException, NotFoundException {
        CtClass type = CLASS_POOL.get(method.getProperty().getType().getName());
        String name = method.getProperty().getAssociatedMethod().getName();

        switch (method.getType()) {
            case GET:
                CtMethod getMethod = new CtMethod(type, name, new CtClass[0], entityClass);
                getMethod.setBody("return ($r) this." + method.getProperty().getName() + ";");
                return getMethod;
            case SET:
                CtMethod setMethod = new CtMethod(CLASS_POOL.get(void.class.getName()), name, new CtClass[]{ type }, entityClass);
                setMethod.setBody("this." + method.getProperty().getName() + " = $1;");
                return setMethod;
        }

        return null;
    }

}
