package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.GlobalVariables;
import org.panda_lang.panda.core.parser.util.CodePatcher;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Variable;
import org.panda_lang.panda.core.syntax.block.ClassBlock;
import org.panda_lang.panda.core.parser.util.Error;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class PandaParser {

    private static PandaParser currentParser;
    private final String[] source;
    private final Recognizer recognizer;
    private final Collection<Block> blocks;
    private PandaScript script;
    private BlockParser parser;
    private Block latest;
    private int i, currentLine;

    public PandaParser(String source){
        this.source = CodePatcher.patch(source);
        this.recognizer = new Recognizer();
        this.blocks = new ArrayList<>();
        currentParser = this;
    }

    public PandaScript parse(){
        script = new PandaScript();
        ClassBlock me = new ClassBlock();
        me.setVariableMap(GlobalVariables.VARIABLES);
        try {
            Stack<Character> characters = new Stack<>();
            StringBuilder node = new StringBuilder();
            for (i = 0; i < source.length; i++) {
                String line = source[i];

                // {syntax_indication.recognize}
                SyntaxIndication indi = recognizer.recognize(line);
                if (indi == null) {
                    String info = recognizer.getLineIndication(line).toLowerCase();
                    if(info.equals("else")) {
                        indi = SyntaxIndication.SECTION;
                    } else {
                        System.out.println("Error at line " + i);
                        org.panda_lang.panda.core.parser.util.Error error = new Error("[SyntaxIndication] Not detected: " + line);
                        error.print();
                        break;
                    }
                }
                // {comment.continue}
                if(indi == SyntaxIndication.COMMENT){
                    continue;
                }
                // {section.begin}
                else if(indi == SyntaxIndication.SECTION){
                    if(characters.size() == 0) currentLine = i;
                    characters.push('{');
                }
                // {section.end}
                else if(indi == SyntaxIndication.CLOSE){
                    if(characters.size() != 0) characters.pop();
                    if(characters.size() == 0) {
                        String sectionSource = node.toString();
                        node.setLength(0);
                        parser = new BlockParser(script, recognizer, sectionSource);
                        parser.setParent(me);
                        parser.setLatest(latest);
                        Block block = parser.parse(this);
                        latest = block;
                        if(block == null){
                            System.out.println("[" + i + ":~" + parser.getCurrentLine() + "] Something went wrong...");
                            return null;
                        }
                        blocks.add(block);
                        continue;
                    }
                }
                // {global.variable}
                else if(indi == SyntaxIndication.VARIABLE && characters.size() == 0){
                    VariableParser parser = new VariableParser(me, line);
                    Variable variable = parser.parse();
                    me.addVariable(variable);
                    node.setLength(0);
                    continue;
                }

                // {default}
                node.append(line);
                node.append(System.lineSeparator());

            }
        } catch (Exception e){
            int l = 0;
            if(parser != null) l = parser.getCurrentLine();
            System.out.println("Error at line " + (i + l));
            e.printStackTrace();
        }

        // {global.variable.init}
        me.initVariables();
        for(Block block : blocks) script.addSection(block);
        return script.name(null).author(null).version(null);
    }


    protected void addSection(Block block){
        this.blocks.add(block);
    }

    protected void setCurrentLine(int i){
        this.currentLine = i;
    }

    public PandaScript getScript(){
        return script;
    }

    public Collection<Block> getBlocks() {
        return blocks;
    }

    public String[] getSource() {
        return source;
    }

    public static PandaParser getCurrentInstance(){
        return currentParser;
    }

}