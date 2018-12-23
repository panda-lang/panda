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

package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;

public abstract class UnifiedParserBootstrap<T> implements UnifiedParser<T>, ParserHandler {

    protected BootstrapParserBuilder<T> builder = PandaParserBootstrap.<T> builder().instance(this);
    protected ParserRepresentation<UnifiedParser<T>> parser;

    @Override
    public boolean handle(ParserData data, Tokens source) {
        return get().getHandler().handle(data, source);
    }

    @Override
    public final T parse(ParserData data) throws Throwable {
        return get().getParser().parse(data);
    }

    private ParserRepresentation<UnifiedParser<T>> get() {
        if (parser != null) {
            return parser;
        }

        if (builder == null) {
            throw new PandaParserException("BootstrapParser does not have associated ParserRepresentation or BootstrapBuilder");
        }

        parser = builder.build();
        return parser;
    }

    protected BootstrapParserBuilder<T> builder() {
        return this.builder;
    }

}
