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

package org.panda_lang.panda.framework.language.architecture.prototype.standard.generator;

import javassist.ClassPool;
import javassist.CtClass;
import org.panda_lang.panda.utilities.commons.ClassUtils;

public class ClassPrototypeTypeGenerator {

    private static final ClassPool POOL = ClassPool.getDefault();

    public Class<?> generateType(String className) throws Exception {
        if (ClassUtils.exists(className)) {
            return Class.forName(className);
        }

        CtClass generatedClass = POOL.makeClass(className);
        return generatedClass.toClass();
    }

}
