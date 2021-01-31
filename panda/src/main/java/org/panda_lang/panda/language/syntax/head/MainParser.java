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

import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.ContextParser;
import org.panda_lang.framework.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.interpreter.parser.SourceReader;
import org.panda_lang.framework.interpreter.parser.pool.Targets;
import org.panda_lang.framework.interpreter.parser.stage.Layer;
import org.panda_lang.framework.interpreter.parser.stage.Phases;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.resource.syntax.auxiliary.Section;
import org.panda_lang.framework.resource.syntax.keyword.Keywords;
import org.panda_lang.framework.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.syntax.ScopeParser;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.framework.interpreter.parser.Component;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

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
