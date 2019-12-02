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

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoaderUtils;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.language.resource.internal.InternalModuleInfo;
import org.panda_lang.framework.language.resource.internal.PandaResourcesUtils;
import org.panda_lang.utilities.commons.StringUtils;

import java.util.Optional;

public final class JavaModule implements InternalModuleInfo {

    public static final String VOID = "void";
    public static final String OBJECT = "Object";
    public static final String INT = "Int";
    public static final String CHAR = "Char";
    public static final String BOOL = "Bool";
    public static final String BYTE = "Byte";
    public static final String SHORT = "Short";
    public static final String LONG = "Long";
    public static final String FLOAT = "Float";
    public static final String DOUBLE = "Double";
    public static final String STRING = "String";
    public static final String NUMBER = "Number";
    public static final String ITERABLE = "Iterable";

    @Override
    public void initialize(Module module) {
        PandaResourcesUtils.of(module, void.class, VOID);
        PandaResourcesUtils.generate(module, Object.class, OBJECT);
        PandaResourcesUtils.generate(module, Integer.class, INT);
        PandaResourcesUtils.generate(module, Character.class, CHAR);
        PandaResourcesUtils.generate(module, Boolean.class, BOOL);
    }

    @Override
    public String[] getNames() {
        return new String[] {
                BYTE,
                SHORT,
                LONG,
                FLOAT,
                DOUBLE,
                STRING,
                NUMBER,
                ITERABLE
        };
    }

    @Override
    public String getPackageName() {
        return "java.lang";
    }

    @Override
    public String getModule() {
        return "java";
    }

}
