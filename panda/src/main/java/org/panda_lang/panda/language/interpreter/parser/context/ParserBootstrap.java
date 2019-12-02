/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.language.interpreter.parser.context;

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.ContextParser;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;
import org.panda_lang.framework.design.interpreter.parser.ParserRepresentation;
import org.panda_lang.framework.design.interpreter.token.Snippet;

public abstract class ParserBootstrap<T> implements ContextParser<T>, Handler {

    protected ParserRepresentation<ContextParser<T>> parser;

    protected abstract BootstrapInitializer<T> initialize(Context context, BootstrapInitializer<T> initializer);

    protected Object customHandle(Handler handler, Context context, Channel channel, Snippet source) {
        return handler.handle(context, channel, source);
    }

    @Override
    public final Object handle(Context context, Channel channel, Snippet source) {
        return customHandle(get(context).getHandler(), context, channel, source);
    }

    @Override
    public final T parse(Context context) throws Exception {
        return get(context).getParser().parse(context);
    }

    protected ParserRepresentation<ContextParser<T>> get(Context context) {
        if (parser != null) {
            return parser;
        }

        return this.parser = initialize(context, new BootstrapInitializer<T>().instance(this)).build(context);
    }

}
