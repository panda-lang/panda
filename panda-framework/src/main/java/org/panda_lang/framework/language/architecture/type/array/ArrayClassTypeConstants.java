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

import org.panda_lang.framework.design.architecture.module.TypeLoader;
import org.panda_lang.framework.design.architecture.type.TypeMethod;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.language.architecture.type.PandaMethod;
import org.panda_lang.framework.language.interpreter.source.PandaClassSource;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

final class ArrayClassTypeConstants {

    private static final SourceLocation LOCATION = new PandaClassSource(ArrayClassTypeConstants.class).toLocation();

    protected static final Function<TypeLoader, TypeMethod> SIZE = loader -> PandaMethod.builder()
            .name("size")
            .location(LOCATION)
            .returnType(loader.requireType(Integer.class))
            .methodBody((branch, instance, arguments) -> ((Object[]) Objects.requireNonNull(instance)).length)
            .build();

    protected static final Function<TypeLoader, TypeMethod> TO_STRING = loader -> PandaMethod.builder()
            .name("toString")
            .location(LOCATION)
            .returnType(loader.requireType(String.class))
            .methodBody((branch, instance, arguments) -> Arrays.toString((Object[]) instance))
            .isNative(true)
            .build();

    private ArrayClassTypeConstants() { }

}
