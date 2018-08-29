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
import org.panda_lang.panda.utilities.commons.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;

public class LayerObject {

    private final Method method;

    public LayerObject() {
        List<Method> methods = ReflectionUtils.getMethodsAnnotatedWith(getClass(), Autowired.class);

        if (methods.size() == 0) {
            throw new ParserBootstrapException("InjectedLayer does not have annotated by @Autowired methods");
        }

        if (methods.size() > 1) {
            throw new ParserBootstrapException("You can use only one @Autowired method per layer");
        }

        this.method = methods.get(0);
    }

    public LayerMethod toLayerMethod() {
        return new LayerMethod(method);
    }

}
