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

package org.panda_lang.panda.framework.design.interpreter.parser.expression;

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.DiffusedSource;

import java.util.Optional;

public interface ExpressionParser extends Parser {

    Optional<Expression> parseSilently(ParserData data, Snippet source);

    Optional<Expression> parseSilently(ParserData data, SourceStream source);

    Optional<Expression> parseSilently(ParserData data, SourceStream source, ExpressionParserSettings settings);

    Expression parse(ParserData data, Snippet source);

    Expression parse(ParserData data, DiffusedSource source);

    Expression parse(ParserData data, DiffusedSource source, ExpressionParserSettings settings);

    Expression parse(ParserData data, SourceStream source);

    Expression parse(ParserData data, SourceStream source, ExpressionParserSettings settings);

}
