package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.essential.assistant.FieldAssistant;
import org.panda_lang.panda.core.parser.essential.util.EqualityBuilder;
import org.panda_lang.panda.core.statement.Equality;
import org.panda_lang.panda.core.statement.Operator;

import java.util.Collection;

public class EqualityParser implements Parser {

    @Override
    public Equality parse(ParserInfo parserInfo) {
        EqualityBuilder equalityBuilder = new EqualityBuilder();
        String source = parserInfo.getSourcesDivider().getLine();

        Collection<Operator> operators = Operator.getOperators(1);
        for (Operator operator : operators) {
            if (source.contains(operator.getOperator())) {
                String[] sides = source.split(operator.getOperator());
                sides[0] = FieldAssistant.clear(sides[0], 0);
                sides[1] = FieldAssistant.clear(sides[1], 0);

                FactorParser factorParser = new FactorParser();
                equalityBuilder.setOne(factorParser.parse(parserInfo, sides[0]));
                equalityBuilder.setOther(factorParser.parse(parserInfo, sides[1]));
                equalityBuilder.setOperator(operator);
            }
        }

        return new Equality(equalityBuilder);
    }

}
