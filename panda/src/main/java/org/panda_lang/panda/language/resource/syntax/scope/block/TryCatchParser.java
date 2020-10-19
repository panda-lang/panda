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

package org.panda_lang.panda.language.resource.syntax.scope.block;

import org.panda_lang.language.architecture.statement.PandaBlock;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.architecture.statement.Variable;
import org.panda_lang.language.architecture.statement.VariableData;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.panda.language.resource.syntax.scope.variable.VariableDataInitializer;
import org.panda_lang.utilities.commons.function.Option;

import java.util.concurrent.CompletableFuture;

public final class TryCatchParser extends BlockParser<TryCatch> {

    @Override
    public String name() {
        return "try-catch";
    }

    @Override
    public Option<CompletableFuture<TryCatch>> parse(Context<?> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());
        Location tryLocation = sourceReader.toLocation();

        if (sourceReader.read(Keywords.TRY).isEmpty()) {
            return Option.none();
        }

        Option<Snippet> tryBody = sourceReader.readBody();

        if (tryBody.isEmpty()) {
            throw new PandaParserFailure(context, "Missing try body");
        }

        Location catchLocation = sourceReader.toLocation();

        if (sourceReader.read(Keywords.CATCH).isEmpty()) {
            throw new PandaParserFailure(context, "Missing try body");
        }

        Option<Snippet> catchWhat = sourceReader.readArguments();

        if (catchWhat.isEmpty()) {
            throw new PandaParserFailure(context, "Missing catch arguments");
        }

        Option<Snippet> catchBody = sourceReader.readBody();

        if (catchBody.isEmpty()) {
            throw new PandaParserFailure(context, "Missing catch body");
        }

        Scope parent = context.getScope();

        Scope tryBlock = SCOPE_PARSER.parse(context, new PandaBlock(parent, tryLocation), tryBody.get());
        TryCatch tryCatch = new TryCatch(tryLocation, tryBlock, new PandaBlock(parent, tryLocation));
        context.getScope().addStatement(tryCatch);

        Scope catchBlock = new PandaBlock(parent, catchLocation);
        VariableDataInitializer dataInitializer = new VariableDataInitializer(context, catchBlock);
        VariableData variableData = dataInitializer.createVariableData(catchWhat.get(), false, false);
        Variable variable = catchBlock.createVariable(variableData);
        SCOPE_PARSER.parse(context, catchBlock, catchBody.get());

        if (variableData.getKnownType().isAssignableFrom(context.getTypeLoader().requireType("panda::Throwable"))) {
            //noinspection unchecked
            tryCatch.addHandler((Class<? extends Throwable>) variable.getKnownType().getType().getAssociated().get(), variable, catchBlock);
        }

        return Option.of(CompletableFuture.completedFuture(tryCatch));
    }

}
