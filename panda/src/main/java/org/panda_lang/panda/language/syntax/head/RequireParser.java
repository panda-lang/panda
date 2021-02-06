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

import org.panda_lang.framework.architecture.module.Imports;
import org.panda_lang.framework.architecture.packages.Package;
import org.panda_lang.framework.architecture.packages.Packages;
import org.panda_lang.framework.interpreter.parser.Component;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.ContextParser;
import org.panda_lang.framework.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.interpreter.parser.pool.Targets;
import org.panda_lang.framework.interpreter.source.SourceService;
import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.framework.resource.syntax.TokenTypes;
import org.panda_lang.framework.resource.syntax.keyword.Keywords;
import org.panda_lang.framework.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.syntax.PandaSourceReader;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

import java.util.Arrays;

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

        TokenInfo packageName = sourceReader.read(TokenTypes.SEQUENCE).orThrow(() -> {
            throw new PandaParserFailure(context, "Missing package name");
        });

        Packages packages = context.getEnvironment().getPackages();

        Package requiredPackage = packages.getPackage(packageName.getValue()).orThrow(() -> {
            throw new PandaParserFailure(context, packageName, "Cannot find package '" + packageName.getValue() + "'");
        });

        SourceService sources = context.getEnvironment().getSources();
        Imports imports = context.getImports();

        sourceReader.optionalRead(sourceReader::readBody)
                .onEmpty(() -> imports.importModule(requiredPackage.forMainModule(sources)))
                .toStream(body -> Arrays.stream(body.split(Separators.COMMA)))
                .forEach(qualifier -> {
                    requiredPackage.forModule(sources, qualifier.asSource().replace("base", ""))
                            .peek(imports::importModule)
                            .orThrow(() -> {
                                throw new PandaParserFailure(context, qualifier, "Cannot find module '" + qualifier.asSource() + "' in package '" + packageName.getValue() + "'");
                            });
                });

        return Option.ofCompleted(true);
    }

}
