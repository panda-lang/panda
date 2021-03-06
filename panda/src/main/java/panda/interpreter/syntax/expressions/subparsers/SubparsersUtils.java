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

package panda.interpreter.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.parser.expression.ExpressionContext;
import panda.interpreter.parser.expression.ExpressionResult;
import panda.interpreter.syntax.PandaSourceReader;
import panda.interpreter.syntax.type.SignatureParser;
import panda.std.Result;

final class SubparsersUtils {

    private static final SignatureParser SIGNATURE_PARSER = new SignatureParser();

    private SubparsersUtils() { }

    protected static Result<Signature, ExpressionResult> readType(@Nullable Signature parent, ExpressionContext<?> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getSynchronizedSource().getAvailableSource().toStream());

        return sourceReader.readSignature()
                .map(source -> Result.<Signature, ExpressionResult> ok(SIGNATURE_PARSER.parse(context, source, false, parent)))
                .orElseGet(() -> Result.error(ExpressionResult.error("Unknown type", context.getSynchronizedSource().getSource())))
                .peek(signature -> context.getSynchronizedSource().next(sourceReader.getStream().getReadLength()));
    }

}
