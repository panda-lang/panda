package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.essential.assistant.FactorAssistant;
import org.panda_lang.panda.core.statement.Equality;
import org.panda_lang.panda.core.statement.RuntimeValue;
import org.panda_lang.panda.core.statement.Math;
import org.panda_lang.panda.core.statement.Runtime;

public class RuntimeParser implements Parser {

    @Override
    public RuntimeValue parse(ParserInfo parserInfo) {
        String parameter = parserInfo.getSourceCode();

        // Math
        if (FactorAssistant.isMath(parameter)) {
            parserInfo.getSourcesDivider().setLine(parameter);
            Math math = new MathParser().parse(parserInfo);
            Runtime runtime = new Runtime(math);
            return new RuntimeValue(runtime);
        }
        // Equality
        else if (FactorAssistant.isEquality(parserInfo, parameter)) {
            parserInfo.getSourcesDivider().setLine(parameter);
            Equality equality = new EqualityParser().parse(parserInfo);
            Runtime runtime = new Runtime(equality);
            return new RuntimeValue(runtime);
        }
        else if (FactorAssistant.isMethod(parserInfo, parameter)) {
            // Constructor
            if (parameter.startsWith("new") && parameter.charAt(3) == ' ') {
                Runtime runtime = parseConstructor(parserInfo, parameter);
                return new RuntimeValue(runtime);
            }
            // Method
            Runtime runtime = parseMethod(parserInfo, parameter);
            return new RuntimeValue(runtime);
        }

        return null;
    }

    public Runtime parseConstructor(ParserInfo parserInfo, String source) {
        ConstructorParser constructorParser = new ConstructorParser();
        parserInfo.setSourceCode(source);
        return constructorParser.parse(parserInfo);
    }

    public Runtime parseMethod(ParserInfo parserInfo, String source) {
        MethodParser parser = new MethodParser();
        parserInfo.getSourcesDivider().setLine(source);
        return parser.parse(parserInfo);
    }

}
