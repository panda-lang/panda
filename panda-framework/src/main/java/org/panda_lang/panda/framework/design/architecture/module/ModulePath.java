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

package org.panda_lang.panda.framework.design.architecture.module;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface ModulePath {

    Module create(String groupName);

    default Module create(Class<?> clazz) {
        return this.create(clazz.getPackage().toString());
    }

    @Nullable Module get(String groupName);

    default @Nullable Module get(Class<?> clazz) {
        return this.get(clazz.getSimpleName());
    }

    Collection<? extends Module> getModules();

}
