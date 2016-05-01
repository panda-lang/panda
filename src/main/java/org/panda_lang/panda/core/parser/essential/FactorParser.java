package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.essential.assistant.FactorAssistant;
import org.panda_lang.panda.core.statement.RuntimeValue;

public class FactorParser implements Parser {

    @Override
    public RuntimeValue parse(ParserInfo parserInfo) {
        return parse(parserInfo, parserInfo.getSourceCode());
    }

    public RuntimeValue parse(ParserInfo parserInfo, String parameter) {
        parserInfo.setSourceCode(parameter);

        RuntimeParser runtimeParser = new RuntimeParser();
        RuntimeValue runtimeValue = runtimeParser.parse(parserInfo);

        if (runtimeValue != null) {
            return runtimeValue;
        }

        EssenceParser essenceParser = new EssenceParser();
        runtimeValue = essenceParser.parse(parserInfo);

        if (runtimeValue != null) {
            return runtimeValue;
        }

        VariableParser variableParser = new VariableParser();
        return variableParser.parse(parserInfo);
    }

    public RuntimeValue[] parse(ParserInfo parserInfo, String[] parametersSources) {
        RuntimeValue[] runtimeValues = new RuntimeValue[parametersSources.length];
        for (int i = 0; i < runtimeValues.length; i++) {
            String src = parametersSources[i];
            if (src == null || src.isEmpty()) {
                continue;
            }
            runtimeValues[i] = parse(parserInfo, src);
        }
        return runtimeValues;
    }

    public RuntimeValue[] splitAndParse(ParserInfo parserInfo) {
        String[] parametersSources = FactorAssistant.split(parserInfo.getSourceCode());
        return parse(parserInfo, parametersSources);
    }

}
