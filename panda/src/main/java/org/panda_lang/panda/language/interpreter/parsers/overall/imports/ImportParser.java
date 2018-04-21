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

package org.panda_lang.panda.language.interpreter.parsers.overall.imports;

import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.language.interpreter.parsers.PandaPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.module.ImportRegistry;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactorHollows;
import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;
import org.panda_lang.panda.language.interpreter.parsers.prototype.mapper.ClassPrototypeMappingManager;
import org.panda_lang.panda.language.interpreter.PandaSyntax;
import org.panda_lang.panda.language.interpreter.tokens.Keywords;
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

@ParserRegistration(target = PandaPipelines.OVERALL, parserClass = ImportParser.class, handlerClass = ImportParserHandler.class)
public class ImportParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "import +** ;")
            .build();

    protected static final AbyssPattern ATTACH_PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "attach +** ;")
            .build();

    private static final Collection<URL> BOOT_CLASS_PATH;

    @Override
    public void parse(ParserData data) {
        CasualParserGenerationAssistant.delegateImmediately(data, new ImportDeclarationCasualParserCallback());
    }

    @LocalCallback
    private static class ImportDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            PandaScript script = delegatedData.getComponent(PandaComponents.SCRIPT);
            SourceStream stream = delegatedData.getComponent(PandaComponents.SOURCE_STREAM);

            Environment environment = delegatedData.getComponent(PandaComponents.ENVIRONMENT);
            ModuleRegistry registry = environment.getModuleRegistry();

            TokenizedSource source = stream.toTokenizedSource();
            boolean attach = TokenUtils.equals(source.getFirst(), Keywords.ATTACH);

            AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(attach ? ATTACH_PATTERN : PATTERN, delegatedData);
            TokenizedSource hollow = hollows.getGap(0);
            StringBuilder groupNameBuilder = new StringBuilder();

            for (TokenRepresentation representation : hollow.getTokensRepresentations()) {
                Token token = representation.getToken();
                groupNameBuilder.append(token.getTokenValue());
            }

            String importedGroupName = groupNameBuilder.toString();

            if (attach) {
                if (Package.getPackage(importedGroupName) != null) {
                    Configuration configuration = ConfigurationBuilder
                            .build(importedGroupName, new SubTypesScanner(false))
                            .addUrls(BOOT_CLASS_PATH);

                    Reflections reflections = new Reflections(configuration);
                    Collection<Class<?>> classes = reflections.getSubTypesOf(Object.class);
                    Collection<Class<?>> selectedClasses = new ArrayList<>(classes.size());

                    for (Class<?> clazz : classes) {
                        if (clazz.getEnclosingClass() != null) {
                            continue;
                        }

                        selectedClasses.add(clazz);
                    }

                    ClassPrototypeMappingManager mappingManager = new ClassPrototypeMappingManager();
                    mappingManager.loadClasses(selectedClasses);
                    mappingManager.generate(registry);
                }
                else {
                    throw new PandaParserException("Cannot attach " + importedGroupName + " [package does not exist]");
                }
            }

            Module module = registry.get(importedGroupName);

            if (module == null) {
                throw new PandaParserException("Unknown module " + importedGroupName);
            }

            ImportStatement importStatement = new ImportStatement(module);
            script.getStatements().add(importStatement);

            ImportRegistry importRegistry = script.getImportRegistry();
            importRegistry.include(module);
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
