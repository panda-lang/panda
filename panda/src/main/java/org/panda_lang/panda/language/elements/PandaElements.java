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

package org.panda_lang.panda.language.elements;

import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistrationLoader;
import org.panda_lang.panda.framework.design.interpreter.lexer.Syntax;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.ParserPipelineRegistry;
import org.panda_lang.panda.language.structure.prototype.registry.ClassPrototypeModel;
import org.panda_lang.panda.language.syntax.PandaSyntax;

import java.util.Collection;

public class PandaElements {

    private final ParserPipelineRegistry pipelineRegistry;
    private Syntax syntax;
    private Collection<Collection<Class<? extends ClassPrototypeModel>>> mappings;

    public PandaElements() {
        this.syntax = PandaSyntax.getInstance();

        ParserRegistrationLoader parserRegistrationLoader = new ParserRegistrationLoader();
        this.pipelineRegistry = parserRegistrationLoader.load();
    }

    public void setMappings(Collection<Collection<Class<? extends ClassPrototypeModel>>> mappings) {
        this.mappings = mappings;
    }

    public void setSyntax(Syntax syntax) {
        this.syntax = syntax;
    }

    public Collection<Collection<Class<? extends ClassPrototypeModel>>> getMappings() {
        return mappings;
    }

    public ParserPipelineRegistry getPipelineRegistry() {
        return pipelineRegistry;
    }

    public Syntax getSyntax() {
        return syntax;
    }

}
