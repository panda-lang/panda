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

package org.panda_lang.panda.language.interpreter.parsers.general.number;

import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.ParticularParser;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;

public class NumberExpressionParser implements ParticularParser<Value> {

    @Override
    public Value parse(ParserInfo info, TokenizedSource source) {
        if (!NumberUtils.isNumeric(source)) {
            return null;
        }

        Environment environment = info.getComponent(PandaComponents.ENVIRONMENT);
        ModuleRegistry registry = environment.getModuleRegistry();

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

        Value value;

        switch (numberType) {
            case BYTE:
                value = new PandaValue(registry.forName("byte"), parsedNumber.byteValue());
                break;
            case SHORT:
                value = new PandaValue(registry.forName("short"), parsedNumber.shortValue());
                break;
            case INT:
                value = new PandaValue(registry.forName("int"), parsedNumber.intValue());
                break;
            case LONG:
                value = new PandaValue(registry.forName("long"), parsedNumber.longValue());
                break;
            case FLOAT:
                value = new PandaValue(registry.forName("float"), parsedNumber.floatValue());
                break;
            case DOUBLE:
                value = new PandaValue(registry.forName("double"), parsedNumber.doubleValue());
                break;
            default:
                throw new PandaParserException("Unknown number type: " + numberType);
        }

        return value;
    }

}
