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

package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.data.InterceptorData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.data.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.utilities.commons.StackUtils;

import java.util.List;

final class BootstrapUnifiedParser<T> implements UnifiedParser<T> {

    protected final BootstrapContent content;
    protected final List<? extends BootstrapMethod> methods;

    BootstrapUnifiedParser(BootstrapContent content, List<BootstrapMethod> methods) {
        this.content = content;
        this.methods = methods;
    }

    @Override
    public final T parse(ParserData data) throws Exception {
        SourceStream stream = data.getComponent(UniversalComponents.SOURCE_STREAM);
        Snippet source = stream.toSnippet();
        int length = stream.getUnreadLength();

        InterceptorData interceptorData = content.getInterceptor()
                .map(interceptor -> interceptor.handle(new InterceptorData(), data))
                .orElse(new InterceptorData());

        int difference = length - stream.getUnreadLength();

        if (difference > 0) {
            data.setComponent(BootstrapComponents.CURRENT_SOURCE, source.subSource(0, difference));
        }

        BootstrapTaskScheduler<T> scheduler = new BootstrapTaskScheduler<>(content, StackUtils.reverse(StackUtils.of(methods)));
        return scheduler.schedule(data, interceptorData, new LocalData());
    }

}
