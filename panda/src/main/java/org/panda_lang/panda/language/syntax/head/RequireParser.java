/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.language.syntax.head;

import org.panda_lang.framework.architecture.Environment;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.ContextParser;
import org.panda_lang.framework.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.interpreter.parser.pool.Targets;
import org.panda_lang.framework.interpreter.source.PandaURLSource;
import org.panda_lang.framework.interpreter.source.SourceService.Priority;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.framework.interpreter.token.TokenUtils;
import org.panda_lang.framework.resource.syntax.TokenTypes;
import org.panda_lang.framework.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.syntax.PandaSourceReader;
import org.panda_lang.panda.manager.PackageManagerUtils;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.framework.interpreter.parser.Component;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

import java.io.File;
import java.util.Objects;

public final class RequireParser implements ContextParser<Object, Boolean> {

    @Override
    public String name() {
        return "require";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.HEAD);
    }

    @Override
    public Option<Completable<Boolean>> parse(Context<?> context) {
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

        return Option.ofCompleted(true);
    }

    private void parseModule(Context<?> context, Snippet require) {
        String moduleName = require.asSource();

        context.getEnvironment().getModulePath().forModule(moduleName).then(result -> result
                .peek(module -> context.getImports().importModule(module))
                .orThrow(() -> {
                    throw new PandaParserFailure(context, require,
                            "Unknown module " + moduleName,
                            "Make sure that the name does not have a typo and module is added to the module path");
                }));
    }

    private void parseFile(Context<?> context, TokenInfo requiredFile) {
        if (!TokenUtils.hasName(requiredFile, "String")) {
            throw new PandaParserFailure(context, requiredFile, "Invalid token", "You should use string sequence to import file");
        }

        Environment environment = context.getEnvironment();

        File environmentDirectory = environment.getDirectory();
        File file = new File(environmentDirectory, requiredFile.getValue() + ".panda");

        if (file.exists()) {
            context.getEnvironment().getSources().addSource(Priority.REQUIRED, PandaURLSource.fromFile(file)).then(script -> {
                script.getModule().then(module -> context.getImports().importModule(module));
            });

            return;
        }

        File directory = new File(environmentDirectory, requiredFile.getValue());

        if (!directory.exists()) {
            throw new PandaParserFailure(context, requiredFile, "File " + file + " does not exist", "Make sure that the path does not have a typo");
        }

        PackageManagerUtils.loadToEnvironment(environment, directory)
            .onError(error -> {
                throw new PandaParserFailure(context, requiredFile, "Cannot load module directory");
            })
            .get()
            .thenCompose(script -> environment.getModulePath().forModule(requiredFile.getValue()))
            .then(result -> result
                .peek(module -> context.getImports().importModule(module))
                .onEmpty(() -> {
                    throw new PandaParserFailure(context, requiredFile, "Imported local package " + requiredFile.getValue() + " does not have module with the same name");
                })
            );
    }

}
