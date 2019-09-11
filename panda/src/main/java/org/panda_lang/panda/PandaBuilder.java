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

package org.panda_lang.panda;

import org.panda_lang.panda.language.resource.PandaLanguage;
import org.panda_lang.panda.language.resource.PandaResources;

public class PandaBuilder {

    protected PandaLanguage language;
    protected PandaResources resources;

    private PandaBuilder() { }

    public PandaBuilder withLanguage(PandaLanguage language) {
        this.language = language;
        return this;
    }

    public PandaBuilder withResources(PandaResources resources) {
        this.resources = resources;
        return this;
    }

    public Panda build() {
        return new Panda(this);
    }

    public static PandaBuilder builder() {
        return new PandaBuilder();
    }

}
