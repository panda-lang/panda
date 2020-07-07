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

package org.panda_lang.framework.language.architecture.type;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.type.State;
import org.panda_lang.framework.design.architecture.type.TypeModels;
import org.panda_lang.framework.design.architecture.type.Visibility;
import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.language.interpreter.source.PandaClassSource;

public class PandaTypeMetadata<BUILDER extends PandaTypeMetadata<BUILDER, ?>, TYPE extends PandaType> {

    protected String name;
    protected Module module;
    protected Location location;
    protected Class<?> javaType;
    protected String model = TypeModels.CLASS;
    protected State state = State.DEFAULT;
    protected Visibility visibility = Visibility.OPEN;
    protected boolean isNative;

    protected PandaTypeMetadata() { }

    public BUILDER name(String name) {
        this.name = name;
        return getThis();
    }

    public BUILDER module(Module module) {
        this.module = module;
        return getThis();
    }

    public BUILDER location(Location location) {
        this.location = location;
        return getThis();
    }

    public BUILDER location(Class<?> javaType) {
        return location(new PandaClassSource(javaType).toLocation());
    }

    public BUILDER javaType(Class<?> javaType) {
        this.javaType = javaType;

        if (name == null) {
            this.name = javaType.getSimpleName();
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
        return (TYPE) new PandaType(this);
    }

    @SuppressWarnings("unchecked")
    protected BUILDER getThis() {
        return (BUILDER) this;
    }

}
