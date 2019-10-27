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

package org.panda_lang.framework.language.resource.internal.panda;

import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.language.architecture.module.PandaLazyModule;
import org.panda_lang.framework.language.architecture.prototype.array.PandaArray;
import org.panda_lang.framework.language.resource.internal.InternalModuleInfo;
import org.panda_lang.framework.language.resource.internal.PandaResourcesUtils;

import java.util.Optional;

public final class PandaModule implements InternalModuleInfo {

    private static final PandaLazyModule MODULE = new PandaLazyModule("panda");

    public static final Prototype ARRAY = PandaResourcesUtils.generate(MODULE, PandaArray.class, "Array");

    @Override
    public String[] getNames() {
        return new String[0];
    }

    @Override
    public String getPackageName() {
        return "org.panda_lang";
    }

    @Override
    public Optional<PandaLazyModule> getCustomModule() {
        return Optional.of(MODULE);
    }

    @Override
    public String getModule() {
        return "panda";
    }

}
