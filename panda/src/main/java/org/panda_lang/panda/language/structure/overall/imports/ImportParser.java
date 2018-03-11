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

package org.panda_lang.panda.language.structure.overall.imports;

import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactorHollows;
import org.panda_lang.panda.design.architecture.prototype.module.Module;
import org.panda_lang.panda.language.structure.overall.module.ModuleRegistry;
import org.panda_lang.panda.language.structure.prototype.mapper.ClassPrototypeMappingManager;
import org.panda_lang.panda.language.syntax.PandaSyntax;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@ParserRegistration(target = DefaultPipelines.OVERALL, parserClass = ImportParser.class, handlerClass = ImportParserHandler.class)
public class ImportParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "import +** ;")
            .build();

    private static final Collection<URL> BOOT_CLASS_PATH;

    @Override
    public void parse(ParserInfo info) {
        CasualParserGenerationAssistant.delegateImmediately(info, new ImportDeclarationCasualParserCallback());
    }

    @LocalCallback
    private static class ImportDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(PATTERN, delegatedInfo);

            TokenizedSource hollow = hollows.getGap(0);
            StringBuilder groupNameBuilder = new StringBuilder();

            for (TokenRepresentation representation : hollow.getTokensRepresentations()) {
                Token token = representation.getToken();
                groupNameBuilder.append(token.getTokenValue());
            }

            String importedGroupName = groupNameBuilder.toString();

            ModuleRegistry registry = ModuleRegistry.getDefault();
            Module module = registry.get(importedGroupName);

            if (module == null) {
                Configuration configuration = ConfigurationBuilder
                        .build(importedGroupName, new SubTypesScanner(false))
                        .addUrls(BOOT_CLASS_PATH);

                Reflections reflections = new Reflections(configuration);
                Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

                ClassPrototypeMappingManager mappingManager = new ClassPrototypeMappingManager();
                mappingManager.loadClasses(classes);
                mappingManager.generate();

                module = registry.get(importedGroupName);

                if (module == null) {
                    throw new PandaParserException("Unknown module " + importedGroupName);
                }
            }

            Import anImport = new Import(module);
            ImportStatement importStatement = new ImportStatement(anImport);

            PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);
            script.getStatements().add(importStatement);

            ImportRegistry importRegistry = script.getImportRegistry();
            importRegistry.include(anImport.getModule());
        }

    }

    static {
        String bootClassPath = ManagementFactory.getRuntimeMXBean().getBootClassPath();
        String[] bootClassPathUrls = bootClassPath.split(Character.toString(File.pathSeparatorChar));
        BOOT_CLASS_PATH = new ArrayList<>(bootClassPathUrls.length);

        for (String bootClassPathUrl : bootClassPathUrls) {
            File file = new File(bootClassPathUrl);

            if (!file.exists()) {
                continue;
            }

            try {
                BOOT_CLASS_PATH.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

}
