package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.parser.essential.util.BlockInfo;
import org.panda_lang.panda.core.syntax.Block;

public class Atom {

    private PandaScript pandaScript;
    private PandaParser pandaParser;
    private SourcesDivider sourcesDivider;
    private PatternExtractor patternExtractor;
    private BlockInfo blockInfo;
    private String sourceCode;
    private Block previous;
    private Block current;
    private Block parent;

    public Atom(PandaScript pandaScript, PandaParser pandaParser, SourcesDivider sourcesDivider, PatternExtractor patternExtractor, BlockInfo blockInfo, String sourceCode, Block previous, Block current, Block parent) {
        this.pandaScript = pandaScript;
        this.pandaParser = pandaParser;
        this.sourcesDivider = sourcesDivider;
        this.patternExtractor = patternExtractor;
        this.blockInfo = blockInfo;
        this.sourceCode = sourceCode;
        this.previous = previous;
        this.current = current;
        this.parent = parent;
    }

    public Atom() {
    }

    public void update(Block current, Block parent) {
        this.current = current;
        this.parent = parent;
    }

    public Atom pandaScript(PandaScript pandaScript) {
        this.pandaScript = pandaScript;
        return this;
    }

    public Atom pandaParser(PandaParser pandaParser) {
        this.pandaParser = pandaParser;
        return this;
    }

    public Atom sourcesDivider(SourcesDivider sourcesDivider) {
        this.sourcesDivider = sourcesDivider;
        return this;
    }

    public Atom patternExtractor(PatternExtractor patternExtractor) {
        this.patternExtractor = patternExtractor;
        return this;
    }

    public Atom blockInfo(BlockInfo blockInfo) {
        this.blockInfo = blockInfo;
        return this;
    }

    public Atom sourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
        return this;
    }

    public Atom previous(Block previous) {
        this.previous = previous;
        return this;
    }

    public Atom current(Block current) {
        this.current = current;
        return this;
    }

    public Atom parent(Block parent) {
        this.parent = parent;
        return this;
    }

    public PandaScript getPandaScript() {
        return pandaScript;
    }

    public void setPandaScript(PandaScript pandaScript) {
        this.pandaScript = pandaScript;
    }

    public PandaParser getPandaParser() {
        return pandaParser;
    }

    public void setPandaParser(PandaParser pandaParser) {
        this.pandaParser = pandaParser;
    }

    public SourcesDivider getSourcesDivider() {
        return sourcesDivider;
    }

    public void setSourcesDivider(SourcesDivider sourcesDivider) {
        this.sourcesDivider = sourcesDivider;
    }

    public PatternExtractor getPatternExtractor() {
        return patternExtractor;
    }

    public void setPatternExtractor(PatternExtractor patternExtractor) {
        this.patternExtractor = patternExtractor;
    }

    public BlockInfo getBlockInfo() {
        return blockInfo;
    }

    public void setBlockInfo(BlockInfo blockInfo) {
        this.blockInfo = blockInfo;
    }

    public String getSourceCode() {
        return sourceCode == null && sourcesDivider != null ? new String(sourcesDivider.getSource()) : sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public Block getPrevious() {
        return previous;
    }

    public void setPrevious(Block previous) {
        this.previous = previous;
    }

    public Block getCurrent() {
        return current;
    }

    public void setCurrent(Block current) {
        this.current = current;
    }

    public Block getParent() {
        return parent;
    }

    public void setParent(Block parent) {
        this.parent = parent;
    }

    public Atom copy() {
        return new Atom(pandaScript, pandaParser, sourcesDivider, patternExtractor, blockInfo, sourceCode, previous, current, parent);
    }

}
