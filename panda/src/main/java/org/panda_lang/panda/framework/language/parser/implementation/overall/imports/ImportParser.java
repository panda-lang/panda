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
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactorHollows;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSyntax;

@ParserRegistration(target = UniversalPipelines.OVERALL, parserClass = ImportParser.class, handlerClass = ImportParserHandler.class)
public class ImportParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "import +** ;")
            .build();

    @Override
    public boolean parse(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
        PandaScript script = delegatedData.getComponent(PandaComponents.PANDA_SCRIPT);
        SourceStream stream = delegatedData.getComponent(UniversalComponents.SOURCE_STREAM);

        TokenizedSource source = stream.toTokenizedSource();
        AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(PATTERN, delegatedData);
        TokenizedSource hollow = hollows.getGap(0);

        StringBuilder groupNameBuilder = new StringBuilder();

        for (TokenRepresentation representation : hollow.getTokensRepresentations()) {
            Token token = representation.getToken();
            groupNameBuilder.append(token.getTokenValue());
        }

        ModulePath registry = delegatedData.getComponent(PandaComponents.MODULE_REGISTRY);
        String importedGroupName = groupNameBuilder.toString();
        Module module = registry.get(importedGroupName);

        if (module == null) {
            throw new PandaParserException("Unknown module " + importedGroupName);
        }

        ImportStatement importStatement = new ImportStatement(module);
        script.getStatements().add(importStatement);

        ModuleLoader moduleLoader = script.getModuleLoader();
        moduleLoader.include(module);
        return true;
    }

}
