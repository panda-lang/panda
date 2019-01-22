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

package org.panda_lang.panda.framework.design.architecture.prototype;

import org.panda_lang.panda.framework.design.architecture.module.Module;

public class PandaClassPrototypeBuilder<BUILDER extends PandaClassPrototypeBuilder<BUILDER, ?>, TYPE extends PandaClassPrototype> {

    protected String name;
    protected Module module;
    protected Class<?> associated;

    protected PandaClassPrototypeBuilder() {
        this.associated = Object.class;
    }

    public BUILDER name(String name) {
        this.name = name;
        return getThis();
    }

    public BUILDER module(Module module) {
        this.module = module;
        return getThis();
    }

    public BUILDER associated(Class associated) {
        this.associated = associated;

        if (name == null) {
            this.name = associated.getSimpleName();
        }

        return getThis();
    }

    @SuppressWarnings("unchecked")
    public TYPE build() {
        if (name == null) {
            throw new IllegalArgumentException("ClassPrototype name is not defined");
        }

        return (TYPE) new PandaClassPrototype(this);
    }

    @SuppressWarnings("unchecked")
    protected BUILDER getThis() {
        return (BUILDER) this;
    }

}
