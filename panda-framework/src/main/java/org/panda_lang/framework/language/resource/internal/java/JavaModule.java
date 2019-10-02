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

import java.util.Optional;

public final class JavaModule implements InternalModuleInfo {

    private static final PandaLazyModule MODULE = new PandaLazyModule("java");

    public static final Prototype VOID = PandaPrototypeUtils.of(MODULE, void.class, "void").fetch();
    public static final Prototype BOOLEAN = PandaPrototypeUtils.of(MODULE, Boolean.class, "Boolean").fetch();
    public static final Prototype CHAR = PandaPrototypeUtils.of(MODULE, Character.class, "Char").fetch();
    public static final Prototype BYTE = PandaPrototypeUtils.of(MODULE, Byte.class, "Byte").fetch();
    public static final Prototype SHORT = PandaPrototypeUtils.of(MODULE, Short.class, "Short").fetch();
    public static final Prototype INT = PandaPrototypeUtils.of(MODULE, Integer.class, "Int").fetch();
    public static final Prototype LONG = PandaPrototypeUtils.of(MODULE, Long.class, "Long").fetch();
    public static final Prototype FLOAT = PandaPrototypeUtils.of(MODULE, Float.class, "Float").fetch();
    public static final Prototype DOUBLE = PandaPrototypeUtils.of(MODULE, Double.class, "Double").fetch();
    public static final Prototype OBJECT = PandaPrototypeUtils.generateOf(MODULE, Object.class).fetch();
    public static final Prototype STRING = PandaPrototypeUtils.generateOf(MODULE, String.class).fetch();
    public static final Prototype NUMBER = PandaPrototypeUtils.generateOf(MODULE, Number.class).fetch();

    @Override
    public String[] getNames() {
        return new String[] {
                "Throwable",
                "Exception",
                "RuntimeException",
                "StringBuilder",
                "System"
        };
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
