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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.number;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.expression.PandaExpression;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserException;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.utilities.commons.StringUtils;

public final class NumberParser implements Parser {

    @Override
    public String name() {
        return "number";
    }

    public Expression parse(Context<?> context, Snippet source) {
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
                return new PandaExpression(loader.requireType("panda::Byte").getSignature(), Byte.parseByte(number, radix));
            case SHORT:
                return new PandaExpression(loader.requireType("panda::Short").getSignature(), Short.parseShort(number, radix));
            case INT:
                return new PandaExpression(loader.requireType("panda::Int").getSignature(), Integer.parseInt(number, radix));
            case LONG:
                return new PandaExpression(loader.requireType("panda::Long").getSignature(), Long.parseLong(number, radix));
            case FLOAT:
                return new PandaExpression(loader.requireType("panda::Float").getSignature(), Float.parseFloat(number));
            case DOUBLE:
                return new PandaExpression(loader.requireType("panda::Double").getSignature(), Double.parseDouble(number));
            default:
                throw new PandaParserException("Unknown number type: " + numberType);
        }
    }

}
