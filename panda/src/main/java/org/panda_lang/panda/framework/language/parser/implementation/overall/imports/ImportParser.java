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

package org.panda_lang.panda.framework.language.parser.implementation.overall.imports;

import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.statement.ImportStatement;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.defaults.keyword.Keywords;
import org.panda_lang.panda.framework.language.parser.bootstrap.BootstrapParser;
import org.panda_lang.panda.framework.language.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.ComponentQualifier;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Redactor;
import org.panda_lang.panda.framework.language.parser.bootstrap.handlers.FirstTokenHandler;

@ParserRegistration(target = UniversalPipelines.OVERALL)
public class ImportParser extends BootstrapParser {

    {
        bootstrapParser = PandaParserBootstrap.builder()
                .handler(new FirstTokenHandler(Keywords.IMPORT))
                .pattern("import +** ;", "import")
                .instance(this)
                .build();
    }

    @Autowired
    public void parse(ParserData data, @ComponentQualifier ModulePath modulePath, @ComponentQualifier PandaScript script, @Redactor("import") TokenizedSource importSource) {
        StringBuilder moduleBuilder = new StringBuilder();

        for (TokenRepresentation representation : importSource.getTokensRepresentations()) {
            Token token = representation.getToken();
            moduleBuilder.append(token.getTokenValue());
        }

        String moduleName = moduleBuilder.toString();
        Module module = modulePath.get(moduleName);

        if (module == null) {
            throw new PandaParserException("Unknown module " + moduleName);
        }

        ImportStatement importStatement = new ImportStatement(module);
        script.getStatements().add(importStatement);

        ModuleLoader moduleLoader = script.getModuleLoader();
        moduleLoader.include(module);
    }

}
