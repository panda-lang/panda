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

package org.panda_lang.panda.framework.language.resource.parsers.expression.xxx;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.architecture.value.VariableUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

import java.util.Optional;
import java.util.function.Predicate;

class SubparserUtils {

    public static Predicate<Token> NAMES_FILTER = token -> token.getType() == TokenType.UNKNOWN && !VariableUtils.isAllowedName(token);

    private SubparserUtils() { }

    static @Nullable Tokens readFirstOfType(Tokens source, TokenType type) {
        TokenRepresentation token = source.get(0);

        if (token.getToken().getType() != type) {
            return null;
        }

        return new PandaTokens(token);
    }

    static @Nullable Tokens readBetweenSeparators(Tokens source, Separator first) {
        MatchableDistributor matchable = new MatchableDistributor(new TokenDistributor(source));

        if (!matchable.nextVerified().contentEquals(first)) {
            return null;
        }

        matchable.nextVerified();

        while (matchable.hasNext() && !matchable.isMatchable()) {
            matchable.nextVerified();
        }

        if (!matchable.isMatchable()) {
            return null;
        }

        return source.subSource(0, matchable.getIndex());
    }

    static Expression toSimpleKnownExpression(ParserData data, String className, Object value) {
        Optional<ClassPrototypeReference> type = data.getComponent(UniversalComponents.MODULE_LOADER).forClass(className);

        if (!type.isPresent()) {
            throw new PandaParserFailure("Unknown type " + className, data);
        }

        return toSimpleKnownExpression(type.get().fetch(), value);
    }

    static Expression toSimpleKnownExpression(ClassPrototype type, Object value) {
        return new PandaExpression(new PandaValue(type, value));
    }

}
