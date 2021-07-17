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
import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.type.State;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.type.VisibilityComparator;
import panda.interpreter.architecture.type.member.MemberInvoker;
import panda.interpreter.architecture.type.member.ParametrizedMember;
import panda.interpreter.architecture.type.member.constructor.TypeConstructor;
import panda.interpreter.architecture.type.signature.AdjustedExpression;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import panda.interpreter.parser.expression.ExpressionCategory;
import panda.interpreter.parser.expression.ExpressionContext;
import panda.interpreter.parser.expression.ExpressionResult;
import panda.interpreter.parser.expression.ExpressionSubparser;
import panda.interpreter.parser.expression.ExpressionSubparserWorker;
import panda.interpreter.token.Snippet;
import panda.interpreter.token.SourceStream;
import panda.interpreter.token.SynchronizedSource;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.resource.syntax.TokenTypes;
import panda.interpreter.resource.syntax.auxiliary.Section;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.resource.syntax.separator.Separators;
import panda.interpreter.runtime.PandaRuntimeException;
import panda.interpreter.runtime.ProcessStack;
import panda.interpreter.syntax.PandaSourceReader;
import panda.interpreter.syntax.type.SignatureParser;
import panda.std.Option;

import java.util.List;

public final class InstanceCreationParser implements ExpressionSubparser {

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
                            return scope.revive(stack);
                        } catch (Exception exception) {
                            throw new PandaRuntimeException("Cannot create scope instance", exception);
                        }
                    }).getOrNull();
                }
            }, signature, typeConstructor.get(), arguments));
        }

    }

}
