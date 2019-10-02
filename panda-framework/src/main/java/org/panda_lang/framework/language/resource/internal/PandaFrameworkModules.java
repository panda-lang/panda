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

package org.panda_lang.framework.language.resource.internal;

import org.panda_lang.framework.language.resource.internal.java.JavaModule;
import org.panda_lang.framework.language.resource.internal.panda.PandaModule;

public final class PandaFrameworkModules {

    @SuppressWarnings("unchecked")
    private static final Class<? extends InternalModuleInfo>[] MODULES = new Class[] {
            JavaModule.class,
            PandaModule.class
    };

    public static Class<? extends InternalModuleInfo>[] getClasses() {
        return MODULES;
    }

}
