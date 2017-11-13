/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.composition;

import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.PandaParserPipelineRegistry;
import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.ParserRegistrationLoader;
import org.panda_lang.panda.framework.language.interpreter.lexer.Syntax;
import org.panda_lang.panda.language.structure.prototype.registry.ClassPrototypeRegistry;
import org.panda_lang.panda.language.syntax.PandaSyntax;

public class PandaComposition {

    private final PandaParserPipelineRegistry pipelineRegistry;
    private final ClassPrototypeRegistry prototypeRegistry;
    private Syntax syntax;

    public PandaComposition() {
        this.syntax = PandaSyntax.getInstance();

        ParserRegistrationLoader parserRegistrationLoader = new ParserRegistrationLoader();
        this.pipelineRegistry = parserRegistrationLoader.load();

        this.prototypeRegistry = new ClassPrototypeRegistry();
    }

    public void setSyntax(Syntax syntax) {
        this.syntax = syntax;
    }

    public ClassPrototypeRegistry getPrototypeRegistry() {
        return prototypeRegistry;
    }

    public PandaParserPipelineRegistry getPipelineRegistry() {
        return pipelineRegistry;
    }

    public Syntax getSyntax() {
        return syntax;
    }

}
