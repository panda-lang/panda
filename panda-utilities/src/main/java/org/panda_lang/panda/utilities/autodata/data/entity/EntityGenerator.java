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
import javassist.NotFoundException;
import org.panda_lang.panda.utilities.autodata.AutomatedDataException;
import org.panda_lang.panda.utilities.autodata.data.repository.DataHandler;
import org.panda_lang.panda.utilities.commons.ClassUtils;

import java.lang.reflect.Method;
import java.util.Optional;

final class EntityGenerator {

    private static final ClassPool CLASS_POOL = ClassPool.getDefault();

    private CtClass ctDataHandler;

    protected void initialize() throws NotFoundException {
        ctDataHandler = CLASS_POOL.get(DataHandler.class.getName());
    }

    @SuppressWarnings("unchecked")
    protected Class<? extends DataEntity> generate(EntityScheme scheme, DataHandler<?> dataHandler) throws NotFoundException, CannotCompileException {
        Class<?> clazz = scheme.getRootClass();

        if (!clazz.isInterface()) {
            throw new AutomatedDataException("Entity class is not an interface (source: " + clazz.toGenericString() + ")");
        }

        String name = clazz.getPackage().getName() + ".Controlled" + clazz.getSimpleName();
        Optional<Class<? extends DataEntity>> loadedEntityClass = ClassUtils.forName(CLASS_POOL.getClassLoader(), name);

        if (loadedEntityClass.isPresent()) {
            return loadedEntityClass.get();
        }

        CtClass entityClass = CLASS_POOL.makeClass(name);
        generateConstructor(entityClass);

        CtClass entityInterface = CLASS_POOL.get(clazz.getName());
        entityClass.addInterface(entityInterface);

        for (Method method : clazz.getDeclaredMethods()) {
            generate(entityClass, method);
        }

        return (Class<? extends DataEntity>) entityClass.toClass();
    }

    private void generateConstructor(CtClass entityClass) throws CannotCompileException {
        CtField field = new CtField(ctDataHandler, "dataHandler", entityClass);
        entityClass.addField(field);

        CtConstructor constructor = new CtConstructor(new CtClass[]{ ctDataHandler }, entityClass);
        constructor.setBody("this.dataHandler = $1;");
        entityClass.addConstructor(constructor);

    }

    private void generate(CtClass entityClass, Method method) {

    }

}
