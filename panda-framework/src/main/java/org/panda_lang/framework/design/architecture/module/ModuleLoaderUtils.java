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

package org.panda_lang.framework.design.architecture.module;

import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;

public final class ModuleLoaderUtils {

    private ModuleLoaderUtils() { }

    public static Type forClass(Context context, Class<?> associatedClass) {
        return context.getComponent(Components.MODULE_LOADER).requirePrototype(associatedClass);
    }

    public static Type forName(Context context, String name) {
        return context.getComponent(Components.MODULE_LOADER).requirePrototype(name);
    }

}
