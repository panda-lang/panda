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
import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.interpreter.source.PandaURLSource;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.interpreter.token.TokenUtils;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.panda.manager.PackageManagerUtils;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.Option;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class RequireParser implements ContextParser<Object, Void> {

    @Override
    public String name() {
        return "require";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.HEAD);
    }

    @Override
    public Option<CompletableFuture<Void>> parse(Context<?> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        if (sourceReader.read(Keywords.REQUIRE).isEmpty()) {
            return Option.none();
        }

        Option<Snippet> moduleQualifier = sourceReader.optionalRead(sourceReader::readPandaQualifier);

        if (moduleQualifier.isDefined()) {
            parseModule(context, moduleQualifier.get());
        }
        else {
            Option<TokenInfo> fileQualifier = sourceReader.read(TokenTypes.SEQUENCE);

            if (fileQualifier.isEmpty()) {
                throw new PandaParserFailure(context, context.getSource(), "");
            }

            parseFile(context, Objects.requireNonNull(fileQualifier.get()));
        }

        return Option.of(CompletableFuture.completedFuture(null));
    }

    private Module parseModule(Context<?> context, Snippet require) {
        String moduleName = require.asSource();

        return context.getEnvironment().getModulePath().forModule(moduleName)
                .peek(module -> context.getImports().importModule(module))
                .orThrow(() -> {
                    throw new PandaParserFailure(context, require,
                            "Unknown module " + moduleName,
                            "Make sure that the name does not have a typo and module is added to the module path");
                });
    }

    private void parseFile(Context<?> context, TokenInfo requiredFile) {
        if (!TokenUtils.hasName(requiredFile, "String")) {
            throw new PandaParserFailure(context, requiredFile, "Invalid token", "You should use string sequence to import file");
        }

        Environment environment = context.getEnvironment();

        File environmentDirectory = environment.getDirectory();
        File file = new File(environmentDirectory, requiredFile.getValue() + ".panda");

        if (file.exists()) {
            context.getSourceSet().addSource(PandaURLSource.fromFile(file));
            return;
        }

        file = new File(environmentDirectory, requiredFile.getValue());

        if (!file.exists()) {
            throw new PandaParserFailure(context, requiredFile, "File " + file + " does not exist", "Make sure that the path does not have a typo");
        }

        try {
            PackageManagerUtils.loadToEnvironment(environment, file);
        } catch (IOException e) {
            throw new PandaParserFailure(context, requiredFile, e.getMessage());
        }

        environment.getModulePath().forModule(requiredFile.getValue())
                .peek(module -> context.getImports().importModule(module))
                .onEmpty(() -> environment.getLogger().warn("Imported local package " + requiredFile.getValue() + " does not have module with the same name"));
    }

}
