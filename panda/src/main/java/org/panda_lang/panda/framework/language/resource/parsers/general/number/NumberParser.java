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

package org.panda_lang.panda.framework.language.resource.parsers.general.number;

import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.ParticularParser;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.utilities.commons.StringUtils;

public class NumberParser implements ParticularParser<Value> {

    @Override
    public Value parse(ParserData data, Snippet source) {
        String unknownNumber = StringUtils.replace(source.asString(), "_", "");
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

        switch (numberType) {
            case BYTE:
                return new PandaValue(PandaTypes.BYTE, Byte.parseByte(number, radix));
            case SHORT:
                return new PandaValue(PandaTypes.SHORT, Short.parseShort(number, radix));
            case INT:
                return new PandaValue(PandaTypes.INT, Integer.parseInt(number, radix));
            case LONG:
                return new PandaValue(PandaTypes.LONG, Long.parseLong(number, radix));
            case FLOAT:
                return new PandaValue(PandaTypes.FLOAT, Float.parseFloat(number));
            case DOUBLE:
                return new PandaValue(PandaTypes.DOUBLE, Double.parseDouble(number));
            default:
                throw new PandaParserException("Unknown number type: " + numberType);
        }
    }

}
