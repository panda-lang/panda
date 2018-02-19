/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.language.structure.general.number;

import org.panda_lang.panda.core.structure.value.PandaValue;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.framework.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.Parser;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.language.structure.prototype.structure.PandaClassPrototype;

public class NumberExpressionParser implements Parser {

    private Value value;

    public void parse(TokenizedSource source, ParserInfo info) {
        if (!NumberUtils.isNumeric(source)) {
            return;
        }

        String unknownNumber = source.asString();
        char numberTypeDefinitionCharacter = unknownNumber.charAt(unknownNumber.length() - 1);

        NumberType numberTypeDefinition = NumberType.of(numberTypeDefinitionCharacter);
        String number = numberTypeDefinition == null ? unknownNumber : unknownNumber.substring(0, unknownNumber.length() - 1);

        if (!Character.isDigit(numberTypeDefinitionCharacter) && numberTypeDefinition == null) {
            throw new PandaParserException("Unknown number type " + numberTypeDefinitionCharacter);
        }

        Number parsedNumber;
        NumberType numberType;

        if (number.contains(".")) {
            numberType = NumberType.DOUBLE;
            parsedNumber = Double.parseDouble(number);
        }
        else if (number.contains("x")) {
            numberType = NumberType.INT;
            parsedNumber = Long.parseLong(number.substring(2, number.length()), 16);
        }
        else {
            numberType = NumberType.INT;
            parsedNumber = Long.parseLong(number);
        }

        if (numberTypeDefinition != null) {
            numberType = numberTypeDefinition;
        }

        switch (numberType) {
            case BYTE:
                this.value = new PandaValue(PandaClassPrototype.forName("byte"), parsedNumber.byteValue());
                break;
            case SHORT:
                this.value = new PandaValue(PandaClassPrototype.forName("short"), parsedNumber.shortValue());
                break;
            case INT:
                this.value = new PandaValue(PandaClassPrototype.forName("int"), parsedNumber.intValue());
                break;
            case LONG:
                this.value = new PandaValue(PandaClassPrototype.forName("long"), parsedNumber.longValue());
                break;
            case FLOAT:
                this.value = new PandaValue(PandaClassPrototype.forName("float"), parsedNumber.floatValue());
                break;
            case DOUBLE:
                this.value = new PandaValue(PandaClassPrototype.forName("double"), parsedNumber.doubleValue());
                break;
        }

    }

    public Value getValue() {
        return value;
    }

}
