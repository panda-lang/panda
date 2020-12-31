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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.type.State;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.VisibilityComparator;
import org.panda_lang.language.architecture.type.member.MemberInvoker;
import org.panda_lang.language.architecture.type.member.ParametrizedMember;
import org.panda_lang.language.architecture.type.member.constructor.TypeConstructor;
import org.panda_lang.language.architecture.type.signature.AdjustedExpression;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.SynchronizedSource;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.auxiliary.Section;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.language.runtime.PandaRuntimeException;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.panda.language.resource.syntax.type.SignatureParser;
import org.panda_lang.utilities.commons.function.Option;

import java.util.List;

public final class ConstructorExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new ConstructorWorker().withSubparser(this);
    }

    @Override
    public int minimalRequiredLengthOfSource() {
        return 3;
    }

    @Override
    public ExpressionCategory category() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public String name() {
        return "constructor";
    }

    private static final class ConstructorWorker extends AbstractExpressionSubparserWorker {

        private static final SignatureParser SIGNATURE_PARSER = new SignatureParser();
        private static final ArgumentsParser ARGUMENT_PARSER = new ArgumentsParser();

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            // require 'new' keyword
            if (!token.contentEquals(Keywords.NEW)) {
                return null;
            }

            // backup current index
            SynchronizedSource source = context.getSynchronizedSource();

            if (!source.hasNext()) {
                return null;
            }

            SourceStream typeStream = source.getAvailableSource().toStream();
            PandaSourceReader sourceReader = new PandaSourceReader(typeStream);

            // read type

            Option<Signature> signatureValue = sourceReader.readSignature()
                    .map(signatureSource -> SIGNATURE_PARSER.parse(context, signatureSource, false, null));

            if (signatureValue.isEmpty()) {
                return ExpressionResult.error("Missing type signature", source);
            }

            source.next(typeStream.getReadLength());
            Signature signature = signatureValue.get();

            if (signature.isGeneric()) {
                throw new UnsupportedOperationException("Cannot create instance of generic type"); // TODO: In fact, you should be able to this in the future
            }

            if (!source.hasNext()) {
                return null;
            }

            // look for () section
            TokenInfo next = source.next();

            if (next.getType() != TokenTypes.SECTION) {
                return null;
            }

            Section section = next.toToken();

            if (!section.getSeparator().equals(Separators.PARENTHESIS_LEFT)) {
                return null;
            }

            // parse constructor call
            Type type = signature.toTyped().fetchType();
            VisibilityComparator.requireAccess(type, context.toContext(), typeStream);
            State.requireInstantiation(context.toContext(), type, typeStream);

            Snippet argsSource = next.toToken(Section.class).getContent();
            List<Expression> arguments = ARGUMENT_PARSER.parse(context, argsSource);
            Option<TypeConstructor> typeConstructor = type.getConstructors().getConstructor(arguments);

            if (typeConstructor.isEmpty()) {
                return ExpressionResult.error(type.getSimpleName() + " does not have constructor with the required parameters: " + arguments, next);
            }

            //noinspection Convert2Lambda
            return ExpressionResult.of(new AdjustedExpression(new MemberInvoker<ParametrizedMember, Object, Object>() {
                @Override
                public Object invoke(ParametrizedMember property, ProcessStack stack, @Nullable Object instance, Object[] args) {
                    return type.getTypeScope().map(scope -> {
                        try {
                            return scope.revive(stack, instance, typeConstructor.get(), args);
                        } catch (Exception exception) {
                            throw new PandaRuntimeException("Cannot create scope instance", exception);
                        }
                    }).getOrNull();
                }
            }, signature, typeConstructor.get(), arguments));
        }

    }

}
