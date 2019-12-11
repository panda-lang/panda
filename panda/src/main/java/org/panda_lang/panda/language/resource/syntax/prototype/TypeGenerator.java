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

package org.panda_lang.panda.language.resource.syntax.prototype;

import javassist.ClassPool;
import javassist.CtClass;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.utilities.commons.ClassUtils;

final class TypeGenerator {

    private static final ClassPool CLASS_POOL = ClassPool.getDefault();


    public Class<?> generateType(Prototype prototype, String className) throws Exception {
        if (ClassUtils.exists(className)) {
            return Class.forName(className);
        }

        CtClass generatedClass = CLASS_POOL.makeClass(className);

        for (PrototypeMethod method : prototype.getMethods().getProperties()) {

        }

        return generatedClass.toClass();
    }

}
