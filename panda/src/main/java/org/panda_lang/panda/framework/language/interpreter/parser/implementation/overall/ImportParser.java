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
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.architecture.statement.ImportStatement;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.BootstrapParser;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.interceptor.TokenPatternInterceptor;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(target = UniversalPipelines.OVERALL)
public class ImportParser extends BootstrapParser {

    {
        parserBuilder = builder()
                .handler(new TokenHandler(Keywords.IMPORT))
                .interceptor(new TokenPatternInterceptor())
                .pattern("import <import: token {type:unknown}, token {value:-}>[;]");
    }

    @Autowired
    public void parse(ParserData data, @Component ModulePath modulePath, @Component PandaScript script, @Src("import") Tokens importSource) {
        StringBuilder moduleName = new StringBuilder();

        for (TokenRepresentation representation : importSource.getTokensRepresentations()) {
            moduleName.append(representation.getTokenValue());
        }

        Module module = modulePath.get(moduleName.toString());
        ImportStatement importStatement = new ImportStatement(module);

        if (module == null) {
            throw new PandaParserFailure("Unknown module " + moduleName, data);
        }

        script.getModuleLoader().include(module);
        script.getStatements().add(importStatement);
    }

}
