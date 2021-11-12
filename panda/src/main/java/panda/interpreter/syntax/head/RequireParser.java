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

package panda.interpreter.syntax.head;

import panda.interpreter.architecture.module.Imports;
import panda.interpreter.architecture.packages.Package;
import panda.interpreter.architecture.packages.Packages;
import panda.interpreter.parser.Component;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.source.SourceService;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.resource.syntax.TokenTypes;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.resource.syntax.separator.Separators;
import panda.interpreter.syntax.PandaSourceReader;
import panda.utilities.ArrayUtils;
import panda.std.reactive.Completable;
import panda.std.Option;

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

        return Option.withCompleted(true);
    }

}
