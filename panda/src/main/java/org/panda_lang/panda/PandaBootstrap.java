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

import org.panda_lang.panda.bootstrap.PipelinePandaBootstrap;
import org.panda_lang.panda.framework.design.resource.Syntax;
import org.panda_lang.panda.framework.language.resource.PandaLanguage;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

public class PandaBootstrap {

    private Syntax syntax;

    private PandaBootstrap() {}

    public PandaBootstrap withSyntax(Syntax syntax) {
        this.syntax = syntax;
        return this;
    }

    public PipelinePandaBootstrap initializePipelines() {
        return new PipelinePandaBootstrap(this);
    }

    public Panda get() {
        if (syntax == null) {
            this.syntax = new PandaSyntax();
        }

        return new Panda(new PandaLanguage(syntax));
    }

    public static PandaBootstrap initializeBootstrap() {
        return new PandaBootstrap();
    }

}