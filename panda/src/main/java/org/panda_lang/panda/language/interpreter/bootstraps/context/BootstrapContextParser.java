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

package org.panda_lang.panda.language.interpreter.bootstraps.context;

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.ContextParser;
import org.panda_lang.panda.language.interpreter.bootstraps.context.data.InterceptorData;
import org.panda_lang.panda.language.interpreter.bootstraps.context.data.LocalData;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.utilities.commons.StackUtils;

import java.util.List;

final class BootstrapContextParser<T> implements ContextParser<T> {

    protected final BootstrapContent content;
    protected final List<? extends BootstrapMethod> methods;

    BootstrapContextParser(BootstrapContent content, List<BootstrapMethod> methods) {
        this.content = content;
        this.methods = methods;
    }

    @Override
    public final T parse(Context context) throws Exception {
        SourceStream stream = context.getComponent(Components.STREAM);
        Snippet source = stream.toSnippet();
        int length = stream.getUnreadLength();

        InterceptorData interceptorData = content.getInterceptor()
                .map(interceptor -> interceptor.handle(new InterceptorData(), context))
                .orElse(new InterceptorData());

        int difference = length - stream.getUnreadLength();

        if (difference > 0) {
            context.withComponent(BootstrapComponents.CURRENT_SOURCE, source.subSource(0, difference));
        }

        BootstrapTaskScheduler<T> scheduler = new BootstrapTaskScheduler<>(content, StackUtils.reverse(StackUtils.of(methods)));
        return scheduler.schedule(context, interceptorData, new LocalData());
    }

}
