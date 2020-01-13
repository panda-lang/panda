/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.DynamicClass;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.architecture.prototype.State;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;

public class PandaPrototypeBuilder<BUILDER extends PandaPrototypeBuilder<BUILDER, ?>, TYPE extends PandaPrototype> {

    protected Reference reference;
    protected String name;
    protected Module module;
    protected SourceLocation location;
    protected DynamicClass associated;
    protected String model;
    protected State state;
    protected Visibility visibility;
    protected boolean isNative;

    protected PandaPrototypeBuilder() { }

    public BUILDER name(String name) {
        this.name = name;
        return getThis();
    }

    public BUILDER reference(Reference reference) {
        this.reference = reference;
        return getThis();
    }

    public BUILDER module(Module module) {
        this.module = module;
        return getThis();
    }

    public BUILDER location(SourceLocation location) {
        this.location = location;
        return getThis();
    }

    public BUILDER associated(DynamicClass associated) {
        this.associated = associated;

        if (name == null) {
            this.name = associated.getSimpleName();
        }

        return getThis();
    }

    public BUILDER model(String model) {
        this.model = model;
        return getThis();
    }

    public BUILDER state(State state) {
        this.state = state;
        return getThis();
    }

    public BUILDER visibility(Visibility visibility) {
        this.visibility = visibility;
        return getThis();
    }

    public BUILDER isNative(boolean isNative) {
        this.isNative = isNative;
        return getThis();
    }

    @SuppressWarnings("unchecked")
    public TYPE build() {
        return (TYPE) new PandaPrototype(this);
    }

    @SuppressWarnings("unchecked")
    protected BUILDER getThis() {
        return (BUILDER) this;
    }

}
