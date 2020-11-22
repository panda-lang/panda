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

import org.panda_lang.language.architecture.Script;
import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaParserException;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.pool.PoolParser;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

public final class ModuleParser implements ContextParser<PoolParser<?>, ModuleStatement> {

    @Override
    public String name() {
        return "module";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.HEAD);
    }

    @Override
    public Option<Completable<ModuleStatement>> parse(Context<? extends PoolParser<?>> context) {
        PandaSourceReader reader = new PandaSourceReader(context.getStream());
        Option<TokenInfo> read = reader.read(Keywords.MODULE);

        if (read.isEmpty()) {
            return Option.none();
        }

        if (context.getScript().select(ModuleStatement.class).size() > 0) {
            throw new PandaParserException("Script contains more than one declaration of the group");
        }

        Completable<ModuleStatement> futureStatement = new Completable<>();

        reader.readPandaQualifier()
                .peek(source -> {
                    Module module = context.getEnvironment().getModulePath().acquire(source.asSource()).orThrow(() -> {
                        throw new PandaParserFailure(context, source, "Cannot access module");
                    });

                    ModuleStatement moduleStatement = new ModuleStatement(read.get().getLocation(), module);
                    context.getImports().importModule(module);

                    Script script = context.getScript();
                    script.addStatement(moduleStatement);
                    script.getModule().complete(module);
                })
                .orThrow(() -> {
                    throw new PandaParserFailure(context, read.get(), "Cannot read module name");
                });

        return Option.of(futureStatement);
    }

}
