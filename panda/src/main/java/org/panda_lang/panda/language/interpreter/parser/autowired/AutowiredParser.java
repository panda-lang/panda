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

package org.panda_lang.panda.language.interpreter.parser.autowired;

import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.ParserRepresentation;
import org.panda_lang.language.interpreter.parser.LocalChannel;
import org.panda_lang.language.interpreter.parser.pipeline.Handler;
import org.panda_lang.language.interpreter.token.Snippet;

public abstract class AutowiredParser<T> implements ContextParser<T>, Handler {

    protected ParserRepresentation<ContextParser<T>> parser;

    protected abstract AutowiredInitializer<T> initialize(Context context, AutowiredInitializer<T> initializer);

    protected Object customHandle(Handler handler, Context context, LocalChannel channel, Snippet source) {
        return handler.handle(context, channel, source);
    }

    @Override
    public final Object handle(Context context, LocalChannel channel, Snippet source) {
        return customHandle(get(context).getHandler(), context, channel, source);
    }

    @Override
    public final T parse(Context context) {
        return get(context).getParser().parse(context);
    }

    protected ParserRepresentation<ContextParser<T>> get(Context context) {
        if (parser != null) {
            return parser;
        }

        return (this.parser = initialize(context, new AutowiredInitializer<T>().instance(this)).build(context));
    }

}
