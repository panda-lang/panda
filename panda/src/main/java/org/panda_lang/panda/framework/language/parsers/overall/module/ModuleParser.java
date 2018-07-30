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

package org.panda_lang.panda.framework.language.parsers.overall.module;

import org.panda_lang.panda.framework.design.architecture.*;
import org.panda_lang.panda.framework.design.architecture.statement.ModuleStatement;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSyntax;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.architecture.module.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.*;
import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.language.interpreter.parser.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;

import java.util.*;

@ParserRegistration(target = UniversalPipelines.OVERALL, parserClass = ModuleParser.class, handlerClass = ModuleParserHandler.class)
public class ModuleParser implements UnifiedParser {

    private static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "module +** ;")
            .build();

    @Override
    public void parse(ParserData data) {
        CasualParserGeneration generation = data.getComponent(UniversalComponents.GENERATION);

        generation.getLayer(CasualParserGenerationType.HIGHER)
                .delegateImmediately(new GroupDeclarationCasualParserCallback(), data)
                .delegateAfter(new GroupAfterCasualParserCallback(), data.fork());
    }

    @LocalCallback
    private static class GroupDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(PATTERN, delegatedData);
            TokenizedSource hollow = hollows.getGap(0);

            StringBuilder groupNameBuilder = new StringBuilder();

            for (TokenRepresentation representation : hollow.getTokensRepresentations()) {
                Token token = representation.getToken();
                groupNameBuilder.append(token.getTokenValue());
            }

            String groupName = groupNameBuilder.toString();

            ModuleRegistry registry = delegatedData.getComponent(PandaComponents.MODULE_REGISTRY);
            Module module = registry.getOrCreate(groupName);

            PandaScript script = delegatedData.getComponent(PandaComponents.PANDA_SCRIPT);
            script.setModule(module);

            ImportRegistry importRegistry = script.getImportRegistry();
            importRegistry.include(module);

            ModuleStatement moduleStatement = new ModuleStatement(module);
            script.getStatements().add(moduleStatement);
        }

    }

    @LocalCallback
    private static class GroupAfterCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            Script script = delegatedData.getComponent(UniversalComponents.SCRIPT);
            Collection<ModuleStatement> moduleStatements = script.select(ModuleStatement.class);

            if (moduleStatements.size() == 0) {
                return;
            }

            if (moduleStatements.size() > 1) {
                throw new PandaParserException("Script contains more than one declaration of the group");
            }
        }

    }

}
