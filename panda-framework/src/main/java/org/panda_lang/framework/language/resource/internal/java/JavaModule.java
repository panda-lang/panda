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
import org.panda_lang.framework.language.resource.internal.InternalModuleInfo;
import org.panda_lang.framework.language.resource.internal.PandaResourcesUtils;
import org.panda_lang.utilities.commons.StringUtils;

import java.util.Optional;

public final class JavaModule implements InternalModuleInfo {

    private static final PandaLazyModule MODULE = new PandaLazyModule("java");

    public static final Prototype VOID = PandaResourcesUtils.of(MODULE, void.class, "void");
    public static final Prototype OBJECT = PandaResourcesUtils.of(MODULE, Object.class, "Object");
    public static final Prototype INT = PandaResourcesUtils.generate(MODULE, Integer.class, "Int");
    public static final Prototype CHAR = PandaResourcesUtils.generate(MODULE, Character.class, "Char");
    public static final Prototype BOOL = PandaResourcesUtils.generate(MODULE, Boolean.class, "Bool");
    public static final Prototype BYTE = PandaResourcesUtils.generate(MODULE, Byte.class);
    public static final Prototype SHORT = PandaResourcesUtils.generate(MODULE, Short.class);
    public static final Prototype LONG = PandaResourcesUtils.generate(MODULE, Long.class);
    public static final Prototype FLOAT = PandaResourcesUtils.generate(MODULE, Float.class);
    public static final Prototype DOUBLE = PandaResourcesUtils.generate(MODULE, Double.class);
    public static final Prototype STRING = PandaResourcesUtils.generate(MODULE, String.class);
    public static final Prototype NUMBER = PandaResourcesUtils.generate(MODULE, Number.class);
    public static final Prototype ITERABLE = PandaResourcesUtils.generate(MODULE, Iterable.class);

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
