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

package org.panda_lang.panda.language.syntax.expressions.subparsers.number;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.architecture.expression.PandaExpression;
import org.panda_lang.framework.architecture.module.TypeLoader;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.PandaParserException;
import org.panda_lang.framework.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.interpreter.token.PandaSnippet;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.framework.resource.syntax.TokenTypes;
import org.panda_lang.framework.resource.syntax.separator.Separators;
import org.panda_lang.framework.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import org.panda_lang.utilities.commons.StringUtils;

public final class NumberParser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new NumberWorker().withSubparser(this);
    }

    @Override
    public String name() {
        return "number";
    }

    private static final class NumberWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

        private Snippet content;
        private TokenInfo period;

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            if (Separators.PERIOD.equals(token)) {
                this.period = token;
                return ExpressionResult.empty();
            }

            if (token.getType() != TokenTypes.UNKNOWN || !NumberUtils.isNumeric(token.getValue())) {
                return dispose();
            }

            if (content == null) {
                this.content = PandaSnippet.createMutable();
            }

            if (this.period != null) {
                content.append(period);
            }

            content.append(token);
            Expression expression;

            try {
                expression = parse(context.toContext(), content);
            } catch (NumberFormatException numberFormatException) {
                return ExpressionResult.error(numberFormatException.getMessage(), content);
            }

            // remove previous result from stack
            if (period != null && context.hasResults()) {
                context.popExpression();
                dispose();
            }

            return ExpressionResult.of(expression);
        }

        private @Nullable ExpressionResult dispose() {
            this.content = null;
            this.period = null;
            return null;
        }

    }

    public static Expression parse(Context<?> context, Snippet source) {
        String unknownNumber = StringUtils.replace(source.asSource(), "_", StringUtils.EMPTY);

        if (unknownNumber.startsWith(".")) {
            unknownNumber = "0" + unknownNumber;
        }

        char numberTypeDefinitionCharacter = unknownNumber.charAt(unknownNumber.length() - 1);
        NumberType numberTypeDefinition = NumberType.of(numberTypeDefinitionCharacter);

        String number = numberTypeDefinition == null ? unknownNumber : unknownNumber.substring(0, unknownNumber.length() - 1);
        NumberType numberType = NumberType.INT;
        int radix = 10;

        // Floating point number
        if (number.contains(".")) {
            numberType = NumberType.DOUBLE;
        }
        // Hexagonal number
        else if (number.startsWith("0x")) {
            number = unknownNumber.substring(2);
            radix = 16;

            if (number.length() < 8) {
                numberTypeDefinition = NumberType.INT;
            }
            else if (number.length() < 16) {
                numberTypeDefinition = NumberType.LONG;
            }
            else throw new PandaParserFailure(context, source, "Hexadecimal numbers above 15 digits are not allowed" + number);
        }

        if (numberTypeDefinition == null && !Character.isDigit(numberTypeDefinitionCharacter)) {
            throw new PandaParserFailure(context, source, "Unknown number type " + numberTypeDefinitionCharacter);
        }

        if (numberTypeDefinition != null) {
            numberType = numberTypeDefinition;
        }

        TypeLoader loader = context.getTypeLoader();

        switch (numberType) {
            case BYTE:
                return new PandaExpression(loader.requireType("panda/panda@::Byte").getSignature(), Byte.parseByte(number, radix));
            case SHORT:
                return new PandaExpression(loader.requireType("panda/panda@::Short").getSignature(), Short.parseShort(number, radix));
            case INT:
                return new PandaExpression(loader.requireType("panda/panda@::Int").getSignature(), Integer.parseInt(number, radix));
            case LONG:
                return new PandaExpression(loader.requireType("panda/panda@::Long").getSignature(), Long.parseLong(number, radix));
            case FLOAT:
                return new PandaExpression(loader.requireType("panda/panda@::Float").getSignature(), Float.parseFloat(number));
            case DOUBLE:
                return new PandaExpression(loader.requireType("panda/panda@::Double").getSignature(), Double.parseDouble(number));
            default:
                throw new PandaParserException("Unknown number type: " + numberType);
        }
    }

}
