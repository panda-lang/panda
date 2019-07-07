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

package org.panda_lang.panda.framework.language.architecture.prototype.standard.constructor;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.PandaParameterizedExecutable;

import java.lang.reflect.Constructor;

public class PandaConstructor extends PandaParameterizedExecutable implements PrototypeConstructor {

    // private final ClassPrototypeScope classScope;
    // private final ConstructorScope constructorScope;

    private PandaConstructor(PandaConstructorBuilder builder) {
        super(builder);
    }

    /*
    @Override
    public ClassPrototypeScopeFrame createInstance(Frame frame, Value... values) {
        ClassPrototypeScopeFrame classInstance = classScope.createInstance(frame);
        Value instance = new PandaStaticValue(classPrototype, classInstance);

        ConstructorScopeFrame constructorInstance = constructorScope.createInstance(frame);
        ParameterUtils.assignValues(constructorInstance, values);

        frame.instance(instance);
        frame.call(constructorInstance);

        return classInstance;
    }
    */

    public static PandaConstructorBuilder builder() {
        return new PandaConstructorBuilder();
    }

    public static class PandaConstructorBuilder extends PandaParametrizedExecutableBuilder<PandaConstructorBuilder> {

        private PandaConstructorBuilder() { }

        public PandaConstructorBuilder constructor(ClassPrototypeReference reference, Constructor<?> constructor) {
            return type(reference).name("constructor " + reference.getName());
        }

        public PandaConstructor build() {
            return new PandaConstructor(this);
        }

    }

}
