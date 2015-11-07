package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.util.*;
import org.panda_lang.panda.core.parser.util.Error;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Variable;

import java.lang.*;
import java.util.Stack;

public class BlockParser {

    private final PandaScript script;
    private final String[] source;
    private final Recognizer recognizer;
    private Block parent, current, latest;
    private int i;

    public BlockParser(PandaScript script, Recognizer recognizer, String section){
        this.script = script;
        this.source = CodePatcher.getAsLines(section);
        this.recognizer = recognizer;
    }

    public Block parse(PandaParser panda){
        Stack<Character> characters = new Stack<>();
        StringBuilder node = new StringBuilder();
        for (i = 0; i < source.length; i++) {
            String line = source[i];
            if(line.isEmpty()) continue;
            if(i == 0){
                String info = recognizer.getLineIndication(line).toLowerCase();
                BlockInfo blockInfo = BlockParserUtils.getSectionIndication(line);
                boolean add = true;

                for(BlockScheme bs : ElementsBucket.getBlocks()){
                    for(String indi : bs.getIndications()){
                        if(indi.equals(info)){
                            current = bs.getParser().parse(blockInfo, current, latest);
                            add = bs.isConventional();
                            break;
                        }
                    } if(current != null) break;
                }

                if(current == null){
                    System.out.println("[BlockParser] Block type not detected: " + line);
                    return null;
                }
                if(add) parent.addExecutable(current);
                current.setParent(parent);
                continue;
            }

            SyntaxIndication indi = recognizer.recognize(line);
            if (indi == null) {
                String info = recognizer.getLineIndication(line).toLowerCase();
                if(info.equals("else")) {
                    indi = SyntaxIndication.BLOCK;
                } else {
                    System.out.println("Error at line " + i);
                    Error error = new org.panda_lang.panda.core.parser.util.Error("[SyntaxIndication] Not detected: " + line);
                    error.print();
                    break;
                }
            }
            switch (indi) {
                case CLOSE:
                    characters.pop();
                    if(characters.size() == 0) {
                        String sectionSource = node.toString();
                        node.setLength(0);
                        BlockParser parser = new BlockParser(script, recognizer, sectionSource);
                        parser.setParent(current);
                        parser.setLatest(latest);
                        Block block = parser.parse(panda);
                        latest = block;
                        break;
                    }
                    node.append(line);
                    node.append(System.lineSeparator());
                    break;
                case BLOCK:
                    characters.push('{');
                case METHOD:
                    if(characters.size() == 0){
                        MethodParser parser = new MethodParser(script, current, line);
                        Method method = parser.parse();
                        current.addExecutable(method);
                        break;
                    }
                case VARIABLE:
                    if(characters.size() == 0){
                        VariableParser parser = new VariableParser(current, line);
                        Variable variable = parser.parse();
                        current.addExecutable(variable);
                        break;
                    }
                default:
                    node.append(line);
                    node.append(System.lineSeparator());
                    break;
            }
        }
        panda.setCurrentLine(i);
        return current;
    }

    public void setLatest(Block block){
        this.latest = block;
    }

    public void setParent(Block block){
        this.parent = block;
    }

    public int getCurrentLine(){
        return this.i;
    }

}