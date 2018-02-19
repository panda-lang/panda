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

package org.panda_lang.panda;

import org.panda_lang.panda.language.structure.overall.module.Module;
import org.panda_lang.panda.language.structure.overall.module.ModuleRegistry;
import org.panda_lang.panda.language.structure.prototype.structure.PandaClassPrototype;

public class PandaFactory {

    public Panda createPanda() {
        Panda panda = new Panda();

        Module defaultModule = ModuleRegistry.getDefault().getDefaultModule();
        defaultModule.add(new PandaClassPrototype(defaultModule, boolean.class, "Boolean"));
        defaultModule.add(new PandaClassPrototype(defaultModule, char.class, "Char"));
        defaultModule.add(new PandaClassPrototype(defaultModule, byte.class, "Byte"));
        defaultModule.add(new PandaClassPrototype(defaultModule, short.class, "Short"));
        defaultModule.add(new PandaClassPrototype(defaultModule, int.class, "Int"));
        defaultModule.add(new PandaClassPrototype(defaultModule, float.class, "Float"));
        defaultModule.add(new PandaClassPrototype(defaultModule, double.class, "Double"));

        return panda;
    }

}
