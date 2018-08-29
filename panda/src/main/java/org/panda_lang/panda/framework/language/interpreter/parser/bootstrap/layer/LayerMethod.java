/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer;

import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.ParserBootstrapException;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class LayerMethod {

    protected final Method method;
    protected final Autowired autowired;

    public LayerMethod(Method method) {
        this.method = method;
        this.autowired = getAnnotation();
    }

    private Autowired getAnnotation() {
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation.annotationType() == Autowired.class) {
                return (Autowired) annotation;
            }
        }

        throw new ParserBootstrapException("Method " + method.getName() + " is not annotated by @Autowired");
    }

    public int getOrder() {
        return autowired.order();
    }

    public Delegation getDelegation() {
        return autowired.value();
    }

    public Method getMethod() {
        return method;
    }

}
