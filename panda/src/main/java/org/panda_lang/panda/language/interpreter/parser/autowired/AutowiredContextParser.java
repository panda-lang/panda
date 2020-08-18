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

import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.utilities.commons.StackUtils;

import java.util.List;

final class AutowiredContextParser<T> implements ContextParser<T> {

    protected final AutowiredContent<?> content;
    protected final List<? extends AutowiredMethod> methods;

    AutowiredContextParser(AutowiredContent<?> content, List<AutowiredMethod> methods) {
        this.content = content;
        this.methods = methods;
    }

    @Override
    public final T parse(Context context) {
        content.getInitializer().handle(context, context.getComponent(Components.CHANNEL));
        return new TaskScheduler<T>(content, StackUtils.reverse(StackUtils.of(methods))).schedule(context);
    }

}
