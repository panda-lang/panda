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
import javassist.Modifier;
import javassist.NotFoundException;
import org.panda_lang.panda.utilities.autodata.AutomatedDataException;
import org.panda_lang.panda.utilities.autodata.data.repository.RepositoryModel;
import org.panda_lang.panda.utilities.autodata.data.repository.RepositoryOperation;
import org.panda_lang.panda.utilities.autodata.data.transaction.DefaultTransaction;
import org.panda_lang.panda.utilities.autodata.data.transaction.Transaction;
import org.panda_lang.panda.utilities.autodata.data.transaction.TransactionModification;
import org.panda_lang.panda.utilities.autodata.orm.As;
import org.panda_lang.panda.utilities.autodata.orm.Generated;
import org.panda_lang.panda.utilities.autodata.orm.GenerationStrategy;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.ClassPoolUtils;
import org.panda_lang.panda.utilities.commons.ClassUtils;
import org.panda_lang.panda.utilities.commons.FunctionUtils;
import org.panda_lang.panda.utilities.commons.collection.Maps;
import org.panda_lang.panda.utilities.commons.javassist.implementer.FunctionalInterfaceImplementer;

import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

final class EntityGenerator {

    private static final FunctionalInterfaceImplementer IMPLEMENTER = new FunctionalInterfaceImplementer();

    private static final CtClass[] TRANSACTION_RUN_TYPES = new CtClass[] { EntityGeneratorConstants.CT_RUNNABLE, EntityGeneratorConstants.CT_ARRAY_LIST };

    @SuppressWarnings("unchecked")
    protected Class<? extends DataEntity> generate(RepositoryModel repositoryModel) throws NotFoundException, CannotCompileException {
        EntityModel entityModel = repositoryModel.getCollectionScheme().getEntityModel();
        Class<?> entityInterface = entityModel.getRootClass();

        if (!entityInterface.isInterface()) {
            throw new AutomatedDataException("Entity class is not an interface (source: " + entityInterface.toGenericString() + ")");
        }

        String name = entityInterface.getPackage().getName() + ".Controlled" + entityInterface.getSimpleName();
        Optional<Class<? extends DataEntity>> loadedEntityClass = ClassUtils.forName(name);

        if (loadedEntityClass.isPresent()) {
            return loadedEntityClass.get();
        }

        CtClass entityClass = ClassPool.getDefault().makeClass(name);
        entityClass.addInterface(ClassPoolUtils.get(entityInterface));
        entityClass.setModifiers(Modifier.PUBLIC);

        generateDefaultFields(entityClass);
        generateFields(entityModel, entityClass);
        generateDefaultConstructor(entityModel, entityClass);
        generateConstructors(repositoryModel, entityClass);
        generateMethods(entityModel, entityClass);
        generateTransactions(entityClass);

        return (Class<? extends DataEntity>) entityClass.toClass();
    }

    private void generateDefaultFields(CtClass entityClass) throws CannotCompileException {
        CtField dataHandler = new CtField(EntityGeneratorConstants.CT_DATA_HANDLER, "_dataHandler", entityClass);
        entityClass.addField(dataHandler);

        CtField lock = new CtField(EntityGeneratorConstants.CT_ATOMIC_BOOLEAN, "_lock", entityClass);
        entityClass.addField(lock);

        CtField modifications = new CtField(EntityGeneratorConstants.CT_ARRAY_LIST, "_modifications", entityClass);
        entityClass.addField(modifications);
    }

    private void generateDefaultConstructor(EntityModel scheme, CtClass entityClass) throws CannotCompileException {
        CtConstructor constructor = new CtConstructor(new CtClass[]{ EntityGeneratorConstants.CT_DATA_HANDLER }, entityClass);

        StringBuilder bodyBuilder = new StringBuilder("{");
        bodyBuilder.append("this._lock = new ").append(AtomicBoolean.class.getName()).append("(false);");
        bodyBuilder.append("this._dataHandler = $1;");

        scheme.getProperties().values().stream()
                .map(property -> Maps.immutableEntryOf(property, property.getAnnotations().getAnnotation(Generated.class)))
                .filter(entry -> entry.getValue().isPresent())
                .forEach(entry -> {
                    Property property = entry.getKey();
                    GenerationStrategy strategy = entry.getValue().get().strategy();

                    bodyBuilder
                            .append("this.").append(property.getName()).append(" = (").append(property.getType().getName()).append(") this._dataHandler.generate(")
                            .append(property.getType().getName()).append(".class, ")
                            .append(GenerationStrategy.class.getName()).append(".").append(strategy.name()).append(");");
                });

        constructor.setBody(bodyBuilder.append("}").toString());
        entityClass.addConstructor(constructor);
    }

    private void generateFields(EntityModel scheme, CtClass entityClass) throws CannotCompileException, NotFoundException {
        for (Property property : scheme.getProperties().values()) {
            CtField field = new CtField(ClassPoolUtils.get(property.getType()), property.getName(), entityClass);
            field.setModifiers(Modifier.PUBLIC);
            entityClass.addField(field);
        }
    }

    private void generateConstructors(RepositoryModel repositoryModel, CtClass entityClass) throws CannotCompileException, NotFoundException {
        for (MethodModel method : repositoryModel.getMethods().getOrDefault(RepositoryOperation.CREATE, Collections.emptyList())) {
            CtConstructor constructor = generateConstructor(entityClass, method);
            entityClass.addConstructor(constructor);
        }
    }

    private CtConstructor generateConstructor(CtClass entityClass, MethodModel method) throws CannotCompileException, NotFoundException {
        Parameter[] parameters = method.getMethod().getParameters();
        Class<?>[] types = method.getMethod().getParameterTypes();
        CtClass[] ctTypes = new CtClass[types.length];

        for (int index = 0; index < types.length; index++) {
            ctTypes[index] = ClassPoolUtils.get(types[index]);
        }

        CtConstructor constructor = new CtConstructor(ArrayUtils.mergeArrays(new CtClass[] { EntityGeneratorConstants.CT_DATA_HANDLER }, ctTypes), entityClass);
        StringBuilder bodyBuilder = new StringBuilder("{ this($1);");

        for (int index = 0; index < parameters.length; index++) {
            Parameter parameter = parameters[index];
            As as = parameter.getAnnotation(As.class);

            bodyBuilder.append("this.").append(as.value()).append(" = $").append(index + 2).append(";");
        }

        constructor.setBody(bodyBuilder.append("}").toString());
        return constructor;
    }

    private void generateMethods(EntityModel entityModel, CtClass entityClass) throws CannotCompileException, NotFoundException {
        for (MethodModel method : entityModel.getMethods()) {
            CtMethod generatedMethod = generateMethod(entityClass, method);
            entityClass.addMethod(generatedMethod);
        }
    }

    private CtMethod generateMethod(CtClass entityClass, MethodModel method) throws CannotCompileException, NotFoundException {
        CtClass type = ClassPoolUtils.get(method.getProperty().getType());
        String name = method.getProperty().getAssociatedMethod().getName();

        switch (method.getType()) {
            case GET:
                CtMethod getMethod = new CtMethod(type, name, new CtClass[0], entityClass);
                getMethod.setBody("return ($r) this." + method.getProperty().getName() + ";");
                return getMethod;
            case SET:
                CtMethod setMethod = new CtMethod(EntityGeneratorConstants.CT_VOID, name, new CtClass[]{ type }, entityClass);

                String propertyType = method.getProperty().getName();
                String modType = TransactionModification.class.getName();
                String defTransType = DefaultTransaction.class.getName();

                //noinspection Duplicates
                setMethod.setBody(new StringBuilder()
                        .append("{")
                        .append("  this.").append(propertyType).append(" = $1;")
                        .append("  ").append(modType).append(" modification = new ").append(modType).append("(\"").append(propertyType).append("\", $1);")
                        .append("  if (this._lock.get() == true) { ")
                        .append("    this._modifications.add(modification);")
                        .append("    return;")
                        .append("  }")
                        .append("  ").append(defTransType).append(".of(this._dataHandler, this, modification).commit();")
                        .append("}")
                        .toString());

                return setMethod;
        }

        return null;
    }

    private void generateTransactions(CtClass entityClass) throws CannotCompileException, NotFoundException {
        CtMethod runnableMethod = new CtMethod(EntityGeneratorConstants.CT_VOID, "transactionRun", TRANSACTION_RUN_TYPES, entityClass);
        runnableMethod.setModifiers(Modifier.PUBLIC);
        runnableMethod.setBody(new StringBuilder()
                .append("{")
                .append("  synchronized (this._lock) { ")
                .append("    this._modifications = $2;")
                .append("    this._lock.set(true);")
                .append("    $1.run();")
                .append("    this._lock.set(false);")
                .append("    this._modifications = null;")
                .append("  }")
                .append("}")
                .toString());
        entityClass.addMethod(runnableMethod);

        LinkedHashMap<String, CtClass> parameters = new LinkedHashMap<>();
        parameters.put("entity", entityClass);
        parameters.put("runnable", EntityGeneratorConstants.CT_RUNNABLE);
        parameters.put("list", EntityGeneratorConstants.CT_ARRAY_LIST);

        Class<?> runnableClass = IMPLEMENTER.generate(entityClass.getName() + "TransactionRunnable", Runnable.class, parameters, "entity.transactionRun(this.runnable, this.list);");

        CtMethod transactionMethod = new CtMethod(EntityGeneratorConstants.CT_DATA_TRANSACTION, "transaction", new CtClass[]{ EntityGeneratorConstants.CT_RUNNABLE }, entityClass);
        transactionMethod.setBody(new StringBuilder("{")
                .append("  java.util.ArrayList list = new java.util.ArrayList();")
                .append("  return new ").append(Transaction.class.getName()).append("(this._dataHandler, this, ")
                .append("    new ").append(runnableClass.getName()).append("(this, $1, list), ")
                .append("    ").append(FunctionUtils.class.getName()).append(".toSupplier(list));")
                .append("}").toString());
        entityClass.addMethod(transactionMethod);
    }

}
