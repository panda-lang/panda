/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.interpreter.parser.expression;

import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.interpreter.parser.Contextual;
import org.panda_lang.framework.interpreter.parser.Parser;
import org.panda_lang.framework.interpreter.token.SourceStream;
import org.panda_lang.framework.interpreter.token.Streamable;
import org.panda_lang.framework.interpreter.token.SynchronizedSource;
import org.panda_lang.utilities.commons.function.Option;

/**
 * Expression parser
 */
public interface ExpressionParser extends Parser {

    /**
     * Parse expression
     *
     * @param context the current context
     * @param streamable the source with expression
     * @param settings the settings to use
     * @return the parsed expression wrapped into the transaction
     * @see org.panda_lang.framework.architecture.expression.Expression
     */
    Expression parse(Contextual<?> context, Streamable streamable, ExpressionParserSettings settings);

    /**
     * Try to parse expression
     *
     * @param context the current context
     * @param streamable the source that may contain expression
     * @param settings the settings to use
     * @return the parsed expression wrapped into the transaction or nothing
     */
    Option<Expression> parseSilently(Contextual<?> context, Streamable streamable, ExpressionParserSettings settings);

    /**
     * Utility method to call
     * {@link #parseSilently(org.panda_lang.framework.interpreter.parser.Contextual, org.panda_lang.framework.interpreter.token.Streamable, ExpressionParserSettings)}
     * with the default settings ({@link org.panda_lang.framework.interpreter.parser.expression.ExpressionParserSettings#DEFAULT})
     *
     * @see #parseSilently(org.panda_lang.framework.interpreter.parser.Contextual, org.panda_lang.framework.interpreter.token.Streamable, ExpressionParserSettings)
     */
    default Option<Expression> parseSilently(Contextual<?> context, Streamable streamable) {
        return parseSilently(context, streamable, ExpressionParserSettings.DEFAULT);
    }

    /**
     * Utility method to parse expression from the synchronized source
     *
     * @param context the current context
     * @param source the synchronized source that contain
     * @return the parsed expression wrapped into the transaction
     */
    default Expression parse(Contextual<?> context, SynchronizedSource source) {
        return parse(context, source, ExpressionParserSettings.DEFAULT);
    }

    /**
     * Utility method to parse expression from the synchronized source using custom settings
     *
     * @param context the current context
     * @param source the synchronized source that contain
     * @param settings the settings to use
     * @return the parsed expression wrapped into the transaction
     */
    default Expression parse(Contextual<?> context, SynchronizedSource source, ExpressionParserSettings settings) {
        SourceStream stream = source.toStream();

        Expression expression = parse(context, stream, settings);
        source.setIndex(source.getIndex() + stream.getReadLength());

        return expression;
    }

    /**
     * Utility method to call
     * {@link #parse(org.panda_lang.framework.interpreter.parser.Contextual, org.panda_lang.framework.interpreter.token.Streamable, ExpressionParserSettings)}
     * with the default settings ({@link org.panda_lang.framework.interpreter.parser.expression.ExpressionParserSettings#DEFAULT})
     *
     * @see #parse(org.panda_lang.framework.interpreter.parser.Contextual, org.panda_lang.framework.interpreter.token.Streamable, ExpressionParserSettings)
     */
    default Expression parse(Contextual<?> context, Streamable streamable) {
        return parse(context, streamable, ExpressionParserSettings.DEFAULT);
    }

}
