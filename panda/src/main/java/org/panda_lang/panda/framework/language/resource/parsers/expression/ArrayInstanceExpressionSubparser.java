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

package org.panda_lang.panda.framework.language.resource.parsers.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.pattern.PandaTokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototypeUtils;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

import java.lang.reflect.Array;
import java.util.Optional;

public class ArrayInstanceExpressionSubparser implements ExpressionSubparser {

    private TokenPattern pattern;

    @Override
    public void initialize(ParserData data) {
        this.pattern = PandaTokenPattern.builder()
                .compile("new <type:reader type> `[ <*capacity:reader expression> `]")
                .build(data);
    }

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        if (!source.getFirst().contentEquals(Keywords.NEW)) {
            return null;
        }

        SourceStream stream = new PandaSourceStream(source);
        ExtractorResult result = pattern.extract(stream);

        if (!result.isMatched()) {
            return null;
        }

        return source.subSource(0, source.size() - stream.getUnreadLength());
    }

    @Override
    public @Nullable Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        ExtractorResult result = pattern.extract(source);

        if (!result.isMatched()) {
            return null;
        }

        String type = result.getWildcard("type").asString();
        Optional<ClassPrototypeReference> reference = ArrayClassPrototypeUtils.obtain(data.getComponent(UniversalComponents.MODULE_LOADER), type + "[]");

        if (!reference.isPresent()) {
            return null;
        }

        Tokens capacity = result.getWildcard("*capacity");
        Expression capacityExpression = main.parse(data, capacity);

        if (!PandaTypes.INT.isAssignableFrom(capacityExpression.getReturnType())) {
            return null;
        }

        return new PandaExpression(new ArrayInstanceExpression((ArrayClassPrototype) reference.get().fetch(), capacityExpression));
    }

    @Override
    public int getMinimumLength() {
        return 5;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.Dynamic.CONSTRUCTOR_CALL;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.ARRAY_INSTANCE;
    }

    private static class ArrayInstanceExpression implements ExpressionCallback {

        private final ArrayClassPrototype prototype;
        private final Expression capacity;

        private ArrayInstanceExpression(ArrayClassPrototype prototype, Expression capacity) {
            this.prototype = prototype;
            this.capacity = capacity;
        }

        @Override
        public Value call(Expression expression, ExecutableBranch branch) {
            return new PandaValue(prototype, Array.newInstance(prototype.getType(), (Integer) capacity.getExpressionValue(branch).getObject()));
        }

        @Override
        public ClassPrototype getReturnType() {
            return prototype;
        }

    }

}
