package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.syntax.Equality;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.Runtime;
import org.panda_lang.panda.util.EqualityBuilder;

public class EqualityParser implements Parser {

    @Override
    public Parameter parse(Atom atom) {
        EqualityBuilder equalityBuilder = new EqualityBuilder();

        Equality equality = new Equality(null);
        return new Parameter("Boolean", atom.getParent().getVariables(), new Runtime(equality));
    }

}
