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

package org.panda_lang.panda.bootstrap;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.framework.design.resource.Syntax;
import org.panda_lang.panda.language.resource.PandaLanguage;
import org.panda_lang.panda.language.resource.PandaResources;

public class PandaBootstrap {

    protected final Panda.PandaBuilder panda = Panda.PandaBuilder.builder();
    protected final PandaLanguage.PandaLanguageBuilder language = PandaLanguage.builder();
    protected final PandaResources.PandaResourcesBuilder resources = PandaResources.builder();

    private PandaBootstrap() { }

    public PandaBootstrap withSyntax(Syntax syntax) {
        this.language.withSyntax(syntax);
        return this;
    }

    public ParsersPandaBootstrap initializeParsers() {
        return new ParsersPandaBootstrap(this);
    }

    public PipelinePandaBootstrap initializePipelines() {
        return new PipelinePandaBootstrap(this);
    }

    public MessengerPandaBootstrap initializeMessenger() {
        return new MessengerPandaBootstrap(this);
    }

    public Panda get() {
        return panda
                .withLanguage(language.build())
                .withResources(resources.build())
                .build();
    }

    public static PandaBootstrap initializeBootstrap() {
        return new PandaBootstrap();
    }

}