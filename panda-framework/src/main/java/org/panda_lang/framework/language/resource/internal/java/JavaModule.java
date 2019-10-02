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

package org.panda_lang.framework.language.resource.internal.java;

import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.language.architecture.module.PandaLazyModule;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototypeUtils;
import org.panda_lang.framework.language.resource.internal.InternalModuleInfo;
import org.panda_lang.utilities.commons.StringUtils;

import java.util.Optional;

public final class JavaModule implements InternalModuleInfo {

    private static final PandaLazyModule MODULE = new PandaLazyModule("java");

    public static final Prototype VOID = PandaPrototypeUtils.of(MODULE, void.class, "void").fetch();
    public static final Prototype OBJECT = PandaPrototypeUtils.generateOf(MODULE, Object.class).fetch();
    public static final Prototype INT = PandaPrototypeUtils.generateOf(MODULE, Integer.class, "Int").fetch();
    public static final Prototype CHAR = PandaPrototypeUtils.generateOf(MODULE, Character.class, "Char").fetch();
    public static final Prototype BOOLEAN = PandaPrototypeUtils.generateOf(MODULE, Boolean.class).fetch();
    public static final Prototype BYTE = PandaPrototypeUtils.generateOf(MODULE, Byte.class).fetch();
    public static final Prototype SHORT = PandaPrototypeUtils.generateOf(MODULE, Short.class).fetch();
    public static final Prototype LONG = PandaPrototypeUtils.generateOf(MODULE, Long.class).fetch();
    public static final Prototype FLOAT = PandaPrototypeUtils.generateOf(MODULE, Float.class).fetch();
    public static final Prototype DOUBLE = PandaPrototypeUtils.generateOf(MODULE, Double.class).fetch();
    public static final Prototype STRING = PandaPrototypeUtils.generateOf(MODULE, String.class).fetch();
    public static final Prototype NUMBER = PandaPrototypeUtils.generateOf(MODULE, Number.class).fetch();
    public static final Prototype ITERABLE = PandaPrototypeUtils.generateOf(MODULE, Iterable.class).fetch();

    @Override
    public String[] getNames() {
        return StringUtils.EMPTY_ARRAY;
    }

    @Override
    public String getPackageName() {
        return "java.lang";
    }

    @Override
    public Optional<PandaLazyModule> getCustomModule() {
        return Optional.of(MODULE);
    }

    @Override
    public String getModule() {
        return "java";
    }

}
