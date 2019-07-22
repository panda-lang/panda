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

package org.panda_lang.panda.utilities.commons.javassist.implementer;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import java.util.LinkedHashMap;

public final class FunctionalInterfaceImplementer {

    public Class<?> generate(String className, Class<?> anInterface, LinkedHashMap<String, CtClass> parameters, CharSequence body) throws CannotCompileException, NotFoundException {
        return new FunctionalInterfaceImplementerGenerator(className, anInterface, parameters, body.toString()).generate();
    }

}
