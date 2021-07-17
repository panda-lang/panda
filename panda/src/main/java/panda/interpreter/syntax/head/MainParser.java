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

import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.SourceReader;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.parser.stage.Layer;
import panda.interpreter.parser.stage.Phases;
import panda.interpreter.token.Snippet;
import panda.interpreter.resource.syntax.auxiliary.Section;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.resource.syntax.separator.Separators;
import panda.interpreter.syntax.ScopeParser;
import panda.utilities.ArrayUtils;
import panda.interpreter.parser.Component;
import panda.std.Completable;
import panda.std.Option;

public final class MainParser implements ContextParser<Object, MainScope> {

    private ScopeParser scopeParser;

    @Override
    public String name() {
        return "main";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.HEAD);
    }

    @Override
    public void initialize(Context<?> context) {
        this.scopeParser = new ScopeParser(context.getPoolService());
    }

    @Override
    public Option<Completable<MainScope>> parse(Context<?> context) {
        SourceReader sourceReader = new SourceReader(context.getStream());

        if (sourceReader.read(Keywords.MAIN).isEmpty()) {
            return Option.none();
        }

        Option<Snippet> body = sourceReader.readSection(Separators.BRACE_LEFT)
                .map(token -> token.toToken(Section.class).getContent());

        if (body.isEmpty()) {
            throw new PandaParserFailure(context, context.getSource(), "Missing body for main statement");
        }

        Completable<MainScope> futureScope = new Completable<>();
        MainScope mainScope = new MainScope(context.getSource().getLocation());

        context.getStageService().delegate("parse main body", Phases.CONTENT, Layer.NEXT_DEFAULT, contentPhase -> {
            scopeParser.parse(context.fork(), mainScope, body.get());
            context.getScript().addStatement(mainScope);
            futureScope.complete(mainScope);
        });

        return Option.of(futureScope);
    }

}
