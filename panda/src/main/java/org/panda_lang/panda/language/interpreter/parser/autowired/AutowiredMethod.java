/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.interpreter.parser.autowired;

import org.panda_lang.language.interpreter.parser.stage.Layer;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Autowired;
import org.panda_lang.utilities.inject.GeneratedMethodInjector;

final class AutowiredMethod {

    protected final Autowired autowired;
    protected final GeneratedMethodInjector generatedMethod;

    AutowiredMethod(GeneratedMethodInjector generatedMethod) {
        this.generatedMethod = generatedMethod;
        this.autowired = generatedMethod.getMethod().getAnnotation(Autowired.class);

        if (autowired == null) {
            throw new AutowiredException("Method " + generatedMethod.getMethod().getName() + " is not annotated by @Autowired");
        }
    }

    protected Layer getDelegation() {
        return autowired.phase();
    }

    protected int getOrder() {
        return autowired.order();
    }

    protected String getCycle() {
        return autowired.stage();
    }

    protected GeneratedMethodInjector getGeneratedMethod() {
        return generatedMethod;
    }

    public String getName() {
        return generatedMethod.getMethod().getName();
    }

    @Override
    public String toString() {
        return "Bootstrap Method: " + generatedMethod.getMethod() + " / " + autowired;
    }

}
