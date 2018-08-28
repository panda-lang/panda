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

package org.panda_lang.panda.framework.language.parser.implementation.overall.module;

import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.Script;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.statement.ModuleStatement;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactorHollows;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSyntax;
import org.panda_lang.panda.framework.language.interpreter.token.defaults.keyword.Keywords;
import org.panda_lang.panda.framework.language.interpreter.token.utils.TokenUtils;

import java.util.Collection;

@ParserRegistration(target = UniversalPipelines.OVERALL)
public class ModuleParser implements UnifiedParser, ParserHandler {

    private static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "module +** ;")
            .build();

    @Override
    public boolean handle(TokenReader reader) {
        return TokenUtils.equals(reader.read(), Keywords.MODULE);
    }

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(PATTERN, data);
        TokenizedSource hollow = hollows.getGap(0);

        StringBuilder groupNameBuilder = new StringBuilder();

        for (TokenRepresentation representation : hollow.getTokensRepresentations()) {
            Token token = representation.getToken();
            groupNameBuilder.append(token.getTokenValue());
        }

        String groupName = groupNameBuilder.toString();
        ModulePath modulePath = data.getComponent(PandaComponents.MODULE_REGISTRY);

        if (!modulePath.hasModule(groupName)) {
            modulePath.create(groupName);
        }

        Module module = modulePath.get(groupName);

        PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
        script.setModule(module);

        ModuleLoader moduleLoader = script.getModuleLoader();
        moduleLoader.include(module);

        ModuleStatement moduleStatement = new ModuleStatement(module);
        script.getStatements().add(moduleStatement);

        nextLayer.delegateAfter(new GroupAfterCasualParserCallback(), data.fork());
        return true;
    }

    @LocalCallback
    private static class GroupAfterCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, GenerationLayer nextLayer) {
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
