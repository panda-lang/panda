package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.essential.assistant.FieldAssistant;
import org.panda_lang.panda.core.parser.essential.util.EqualityBuilder;
import org.panda_lang.panda.core.syntax.Equality;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.core.syntax.Operator;
import org.panda_lang.panda.core.syntax.Runtime;

import java.util.Collection;

public class EqualityParser implements Parser
{

    @Override
    public Factor parse(Atom atom)
    {
        EqualityBuilder equalityBuilder = new EqualityBuilder();
        String source = atom.getSourcesDivider().getLine();

        Collection<Operator> operators = Operator.getOperators(1);
        for (Operator operator : operators)
        {
            if (source.contains(operator.getOperator()))
            {
                String[] sides = source.split(operator.getOperator());
                sides[0] = FieldAssistant.clear(sides[0], 0);
                sides[1] = FieldAssistant.clear(sides[1], 0);

                FactorParser factorParser = new FactorParser();
                equalityBuilder.setOne(factorParser.parse(atom, sides[0]));
                equalityBuilder.setOther(factorParser.parse(atom, sides[1]));
                equalityBuilder.setOperator(operator);
            }
        }

        Equality equality = new Equality(equalityBuilder);
        return new Factor(new Runtime(equality));
    }

}
