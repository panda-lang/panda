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

package org.panda_lang.framework.language.architecture.type.array;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.Reference;
import org.panda_lang.framework.design.architecture.type.Visibility;
import org.panda_lang.framework.language.architecture.type.PandaType;
import org.panda_lang.framework.language.architecture.type.PandaStubReference;
import org.panda_lang.framework.language.architecture.type.dynamic.PandaDynamicClass;
import org.panda_lang.framework.language.interpreter.source.PandaClassSource;

public final class ArrayType extends PandaType {

    private final Type type;

    public ArrayType(Module module, Class<?> associated, Type type) {
        this(module, associated.getSimpleName(), associated, type);
    }

    public ArrayType(Module module, String name, Class<?> associated, Type type) {
        super(builder()
                .reference(type.toReference())
                .module(module)
                .name(name)
                .location(new PandaClassSource(associated).toLocation())
                .associated(new PandaDynamicClass(associated))
                .model(type.getModel())
                .state(type.getState())
                .visibility(Visibility.PUBLIC)
        );

        this.type = type;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    public Type getArrayType() {
        return type;
    }

    @Override
    public Reference toReference() {
        return new PandaStubReference(this);
    }

}
