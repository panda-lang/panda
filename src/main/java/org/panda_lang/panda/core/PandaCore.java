package org.panda_lang.panda.core;

import org.panda_lang.panda.core.parser.ParserCenter;
import org.panda_lang.panda.core.parser.essential.BlockCenter;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.syntax.Vial;

public class PandaCore {

    public PandaCore() {
        parsers();
        blocks();
        objects();
    }

    public void registerParser(ParserLayout parser) {
        ParserCenter.registerPatterns(parser.getPatterns());
    }

    public void registerBlock(BlockLayout blockLayout) {
        BlockCenter.registerBlock(blockLayout);
    }

    public void registerVial(Vial vial) {
        VialCenter.registerVial(vial);
    }

    protected void parsers() {
        ElementsPuller.loadClasses("org.panda_lang.panda.core.parser.essential",
                "ConstructorParser",
                "EqualityParser",
                "MathParser",
                "MethodParser",
                "ParameterParser",
                "VariableParser",
                "BlockParser");
    }

    protected void blocks() {
        ElementsPuller.loadClasses("org.panda_lang.panda.core.syntax.block",
                "ElseThenBlock",
                "ForBlock",
                "IfThenBlock",
                "MethodBlock",
                "RunnableBlock",
                "VialBlock",
                "ThreadBlock",
                "WhileBlock");
    }

    protected void objects() {
        ElementsPuller.loadClasses("org.panda_lang.panda.lang",
                "PArray",
                "PBoolean",
                "PCharacter",
                "PFile",
                "PList",
                "PMap",
                "PNumber",
                "PObject",
                "PPanda",
                "PRunnable",
                "PStack",
                "PString",
                "PSystem",
                "PThread");
    }

}
