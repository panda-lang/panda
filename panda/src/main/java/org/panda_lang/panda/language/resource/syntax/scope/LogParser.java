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

package org.panda_lang.panda.language.resource.syntax.scope;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.logging.Logger;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.language.interpreter.parser.pool.Target;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredInitializer;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredParser;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.autowired.handlers.TokenHandler;
import org.panda_lang.utilities.commons.ArrayUtils;

import java.util.Arrays;

public final class LogParser extends AutowiredParser<Void> {

    @Override
    public Target<? extends Parser>[] pipeline() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    protected AutowiredInitializer<Void> initialize(Context context, AutowiredInitializer<Void> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.LOG))
                .functional(pattern -> pattern.keyword(Keywords.LOG).arguments("arguments"));
    }

    @Autowired(order = 1)
    public void parse(Context context, @Ctx ExpressionParser parser, @Ctx Scope scope, @Channel Location location, @Src("arguments") ExpressionTransaction[] transactions) {
        Expression[] expressions = Arrays.stream(transactions)
                .map(ExpressionTransaction::getExpression)
                .toArray(Expression[]::new);

        Logger logger = context.getLogger();
        scope.addStatement(new LogStatement(location, logger, expressions));
    }

}
