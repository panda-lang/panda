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

package org.panda_lang.panda.framework.language.parsers.general.number;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.architecture.value.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.language.architecture.value.*;
import org.panda_lang.panda.framework.language.interpreter.parser.*;

public class NumberExpressionParser implements ParticularParser<Value> {

    @Override
    public @Nullable Value parse(ParserData data, TokenizedSource source) {
        if (!NumberUtils.isNumeric(source)) {
            return null;
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

        ModuleRegistry registry = data.getComponent(PandaComponents.MODULE_REGISTRY);
        Value value;

        switch (numberType) {
            case BYTE:
                value = new PandaValue(PandaModuleRegistryAssistant.forName(registry, "byte"), parsedNumber.byteValue());
                break;
            case SHORT:
                value = new PandaValue(PandaModuleRegistryAssistant.forName(registry, "short"), parsedNumber.shortValue());
                break;
            case INT:
                value = new PandaValue(PandaModuleRegistryAssistant.forName(registry, "int"), parsedNumber.intValue());
                break;
            case LONG:
                value = new PandaValue(PandaModuleRegistryAssistant.forName(registry, "long"), parsedNumber.longValue());
                break;
            case FLOAT:
                value = new PandaValue(PandaModuleRegistryAssistant.forName(registry, "float"), parsedNumber.floatValue());
                break;
            case DOUBLE:
                value = new PandaValue(PandaModuleRegistryAssistant.forName(registry, "double"), parsedNumber.doubleValue());
                break;
            default:
                throw new PandaParserException("Unknown number type: " + numberType);
        }

        return value;
    }

}
