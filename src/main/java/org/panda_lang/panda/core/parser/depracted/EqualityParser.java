package org.panda_lang.panda.core.parser.depracted;

import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Equality;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.Runtime;
import org.panda_lang.panda.util.EqualityBuilder;

public class EqualityParser {

    private final String source;

    public EqualityParser(String source) {
        this.source = source;
    }

    public Parameter parse(Block block) {
        EqualityBuilder equalityBuilder = new EqualityBuilder();

        Equality equality = new Equality(null);
        return new Parameter("Boolean", block, new Runtime(equality));
    }

}
