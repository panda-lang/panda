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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.overall;

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.architecture.statement.ModuleStatement;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.BootstrapParser;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Redactor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.handlers.FirstTokenHandler;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(target = UniversalPipelines.OVERALL)
public class ModuleParser extends BootstrapParser {

    {
        bootstrapParser = PandaParserBootstrap.builder()
                .handler(new FirstTokenHandler(Keywords.MODULE))
                .pattern("module +** ;", "module")
                .instance(this)
                .build();
    }

    @Autowired
    private void parse(ParserData data, @Component ModulePath modulePath, @Component PandaScript script, @Redactor("module") TokenizedSource moduleSource) {
        StringBuilder moduleName = new StringBuilder();

        for (TokenRepresentation representation : moduleSource.getTokensRepresentations()) {
            moduleName.append(representation.getTokenValue());
        }

        String groupName = moduleName.toString();

        if (!modulePath.hasModule(groupName)) {
            modulePath.create(groupName);
        }

        if (script.select(ModuleStatement.class).size() > 0) {
            throw new PandaParserException("Script contains more than one declaration of the group");
        }

        Module module = modulePath.get(groupName);
        ModuleStatement moduleStatement = new ModuleStatement(module);

        script.setModule(module);
        script.getModuleLoader().include(module);
        script.getStatements().add(moduleStatement);
    }

}
