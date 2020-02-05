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

package org.panda_lang.framework.language.resource.internal.panda;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.language.architecture.type.PandaTypeUtils;
import org.panda_lang.framework.language.architecture.type.array.PandaArray;
import org.panda_lang.framework.language.resource.internal.InternalModuleInfo;
import org.panda_lang.utilities.commons.StringUtils;

public final class PandaModule implements InternalModuleInfo {

    @Override
    public void initialize(Module module) {
        PandaTypeUtils.generateOf(module, PandaArray.class);
    }

    @Override
    public String[] getNames() {
        return StringUtils.EMPTY_ARRAY;
    }

    @Override
    public String getPackageName() {
        return "org.panda_lang";
    }

    @Override
    public String getModule() {
        return "panda";
    }

}
