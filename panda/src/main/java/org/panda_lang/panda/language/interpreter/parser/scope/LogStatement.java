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

package org.panda_lang.panda.language.interpreter.parser.scope;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.expression.ExpressionUtils;
import org.panda_lang.framework.design.interpreter.messenger.Messenger;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.AbstractExecutableStatement;
import org.panda_lang.utilities.commons.text.ContentJoiner;
import org.slf4j.event.Level;

final class LogStatement extends AbstractExecutableStatement {

    private final Messenger messenger;
    private final Expression[] expressions;

    LogStatement(SourceLocation location, Messenger messenger, Expression[] expressions) {
        super(location);
        this.messenger = messenger;
        this.expressions = expressions;
    }

    @Override
    public Object execute(ProcessStack stack, Object instance) throws Exception {
        Object[] values = ExpressionUtils.evaluate(stack, instance, expressions);
        messenger.send(Level.INFO, ContentJoiner.on(", ").join(values));
        return values;
    }

}
