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
import org.panda_lang.panda.utilities.autodata.AutomatedDataException;

import java.lang.reflect.Method;

final class EntityGenerator {

    private static final ClassPool CLASS_POOL = ClassPool.getDefault();

    @SuppressWarnings("unchecked")
    protected Class<? extends DataEntity> generate(Class<?> clazz) {
        if (!clazz.isInterface()) {
            throw new AutomatedDataException("Entity class is not an interface (source: " + clazz.toGenericString() + ")");
        }

        CtClass entityClass = CLASS_POOL.makeClass(clazz.getName() + "ADS");

        for (Method method : clazz.getMethods()) {
            generate(entityClass, method);
        }

        try {
            return (Class<? extends DataEntity>) entityClass.toClass();
        } catch (CannotCompileException e) {
            throw new AutomatedDataException("Cannot compile generated class: " + e.getMessage());
        }
    }

    private void generate(CtClass entityClass, Method method) {

    }

}
