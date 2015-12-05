package org.pandalang.panda.core.parser.improved.essential;

import org.pandalang.panda.core.parser.improved.Atom;
import org.pandalang.panda.core.parser.improved.Parser;
import org.pandalang.panda.core.syntax.Equality;
import org.pandalang.panda.core.syntax.Parameter;
import org.pandalang.panda.core.syntax.Runtime;
import org.pandalang.panda.util.EqualityBuilder;

public class EqualityParser implements Parser {

    @Override
    public Parameter parse(Atom atom) {
        EqualityBuilder equalityBuilder = new EqualityBuilder();

        Equality equality = new Equality(null);
        return new Parameter("Boolean", atom.getParent(), new Runtime(equality));
    }

}
