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

package org.panda_lang.panda.language.resource.syntax.head;

import org.panda_lang.language.architecture.Environment;
import org.panda_lang.language.architecture.module.Imports;
import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.language.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.parser.PandaParserException;
import org.panda_lang.language.interpreter.parser.stage.Stages;
import org.panda_lang.language.interpreter.pattern.functional.elements.QualifierElement;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.architecture.PandaScript;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredInitializer;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredParser;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.autowired.handlers.TokenHandler;
import org.panda_lang.utilities.commons.ArrayUtils;

public final class ModuleParser extends AutowiredParser<Void> {

    @Override
    public PipelineComponent<? extends Parser>[] pipeline() {
        return ArrayUtils.of(Pipelines.HEAD);
    }

    @Override
    protected AutowiredInitializer<Void> initialize(Context context, AutowiredInitializer<Void> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.MODULE))
                .functional(builder -> builder.token(Keywords.MODULE).qualifier("module").consume(QualifierElement::pandaModule));
    }

    @Autowired(order = 1, stage = Stages.TYPES_LABEL)
    public void parse(@Ctx Environment environment, @Ctx Imports imports, @Ctx PandaScript script, @Channel Location location, @Src("module") Snippet source) {
        if (script.select(ModuleStatement.class).size() > 0) {
            throw new PandaParserException("Script contains more than one declaration of the group");
        }

        Module module = environment.getModulePath().allocate(source.asSource());
        imports.importModule(module);

        ModuleStatement moduleStatement = new ModuleStatement(location, module);
        script.addStatement(moduleStatement);
        script.setModule(module);
    }

}
