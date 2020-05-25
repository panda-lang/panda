/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.util.embed;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class PandaEngineCore {

    private final Logger logger = LoggerFactory.getLogger(PandaEngineCore.class);
    private Panda panda;

    protected Panda getPanda() {
        if (panda == null) {
            PandaFactory pandaFactory = new PandaFactory();
            this.panda = pandaFactory.createPanda(logger);
        }

        return panda;
    }

}
