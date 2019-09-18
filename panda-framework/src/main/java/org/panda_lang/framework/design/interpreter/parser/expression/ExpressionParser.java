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

package org.panda_lang.framework.design.interpreter.parser.expression;

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.token.SynchronizedSource;

import java.util.Optional;

public interface ExpressionParser extends Parser {

    Optional<ExpressionTransaction> parseSilently(Context context, Snippet source);

    Optional<ExpressionTransaction> parseSilently(Context context, SourceStream source);

    Optional<ExpressionTransaction> parseSilently(Context context, SourceStream source, ExpressionParserSettings settings);

    ExpressionTransaction parse(Context context, Snippet source);

    ExpressionTransaction parse(Context context, SynchronizedSource source);

    ExpressionTransaction parse(Context context, SynchronizedSource source, ExpressionParserSettings settings);

    ExpressionTransaction parse(Context context, SourceStream source);

    ExpressionTransaction parse(Context context, SourceStream source, ExpressionParserSettings settings);

}
