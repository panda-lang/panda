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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.panda_lang.language.architecture.type.Signature;
import org.panda_lang.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.panda.language.resource.syntax.type.SignatureParser;
import org.panda_lang.utilities.commons.function.Result;

final class SubparsersUtils {

    private static final SignatureParser SIGNATURE_PARSER = new SignatureParser();

    private SubparsersUtils() { }

    protected static Result<Signature, ExpressionResult> readType(ExpressionContext<?> context) {
        SourceStream stream = context.toStream();
        PandaSourceReader sourceReader = new PandaSourceReader(context.toStream());
        int index = stream.getUnreadLength();

        return sourceReader.readSignature()
                .map(source -> Result.<Signature, ExpressionResult> ok(SIGNATURE_PARSER.parse(context, source)))
                .orElseGet(() -> Result.error(ExpressionResult.error("Unknown type", context.getSynchronizedSource().getSource())))
                .peek(signature -> context.getSynchronizedSource().next(index - stream.getUnreadLength()));
    }

}
