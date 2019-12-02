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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.number;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.module.ModuleLoaderUtils;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.SourceParser;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.expression.PandaExpression;
import org.panda_lang.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.framework.language.resource.internal.java.JavaModule;
import org.panda_lang.utilities.commons.StringUtils;

public final class NumberParser implements SourceParser<Expression> {

    @Override
    public Expression parse(Context context, Snippet source) {
        String unknownNumber = StringUtils.replace(source.asSource(), "_", StringUtils.EMPTY);
        char numberTypeDefinitionCharacter = unknownNumber.charAt(unknownNumber.length() - 1);

        NumberType numberTypeDefinition = NumberType.of(numberTypeDefinitionCharacter);
        String number = numberTypeDefinition == null ? unknownNumber : unknownNumber.substring(0, unknownNumber.length() - 1);

        if (numberTypeDefinition == null && !Character.isDigit(numberTypeDefinitionCharacter)) {
            throw new PandaParserException("Unknown number type " + numberTypeDefinitionCharacter);
        }

        NumberType numberType = NumberType.INT;
        int radix = 10;

        if (number.contains(".")) {
            numberType = NumberType.DOUBLE;
        }
        else if (number.contains("x")) {
            number = number.substring(2);
            radix = 16;
        }

        if (numberTypeDefinition != null) {
            numberType = numberTypeDefinition;
        }

        Prototype prototype = ModuleLoaderUtils.forClass(context, numberType.getJavaType());

        switch (numberType) {
            case BYTE:
                return new PandaExpression(prototype, Byte.parseByte(number, radix));
            case SHORT:
                return new PandaExpression(prototype, Short.parseShort(number, radix));
            case INT:
                return new PandaExpression(prototype, Integer.parseInt(number, radix));
            case LONG:
                return new PandaExpression(prototype, Long.parseLong(number, radix));
            case FLOAT:
                return new PandaExpression(prototype, Float.parseFloat(number));
            case DOUBLE:
                return new PandaExpression(prototype, Double.parseDouble(number));
            default:
                throw new PandaParserException("Unknown number type: " + numberType);
        }
    }

}
