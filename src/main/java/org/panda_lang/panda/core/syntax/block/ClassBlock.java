package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.parser.CustomParser;
import org.panda_lang.panda.core.parser.ParameterParser;
import org.panda_lang.panda.core.parser.util.BlockInfo;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Variable;

import java.util.ArrayList;
import java.util.Collection;

public class ClassBlock extends Block {
    
    static {
        new BlockScheme(ClassBlock.class, "class").parser(new CustomParser<Block>() {
            @Override
            public Block parse(BlockInfo blockInfo, Block current, Block latest) {
                current = new ClassBlock();
                current.setParameters(new ParameterParser().parse(current, blockInfo.getParameters()));
                return current;
            }
        });
    }

    private final Collection<Variable> variables;

    public ClassBlock(){
        super.setName("ClassBlock");
        this.variables = new ArrayList<>();
    }

    public void initVariables(){
        for(Variable variable : variables){
            variable.run();
        }
    }

    public void addVariable(Variable variable){
        this.variables.add(variable);
    }

}
