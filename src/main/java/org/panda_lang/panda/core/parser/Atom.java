package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.parser.essential.util.BlockInfo;
import org.panda_lang.panda.core.parser.essential.util.Dependencies;
import org.panda_lang.panda.core.syntax.Block;

import java.util.HashMap;
import java.util.Map;

public class Atom {

    private final Map<String, Object> map;
    private PandaScript pandaScript;
    private PandaParser pandaParser;
    private Dependencies dependencies;
    private SourcesDivider sourcesDivider;
    private PatternExtractor patternExtractor;
    private BlockInfo blockInfo;
    private String sourceCode;
    private Pattern variant;
    private Block previous;
    private Block current;
    private Block parent;

    public Atom(PandaScript pandaScript, PandaParser pandaParser, Dependencies dependencies, SourcesDivider sourcesDivider, PatternExtractor patternExtractor, BlockInfo blockInfo, String sourceCode, Block previous, Block current, Block parent) {
        this();
        this.pandaScript = pandaScript;
        this.pandaParser = pandaParser;
        this.dependencies = dependencies;
        this.sourcesDivider = sourcesDivider;
        this.patternExtractor = patternExtractor;
        this.blockInfo = blockInfo;
        this.sourceCode = sourceCode;
        this.previous = previous;
        this.current = current;
        this.parent = parent;
    }

    public Atom() {
        this.map = new HashMap<>(10);
    }

    public void set(String key, Object value) {
        this.map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public void update(Block current, Block parent) {
        this.current = current;
        this.parent = parent;
    }

    public Atom current(Block current) {
        this.current = current;
        return this;
    }

    public void setPandaScript(PandaScript pandaScript) {
        this.pandaScript = pandaScript;
    }

    public void setPandaParser(PandaParser pandaParser) {
        this.pandaParser = pandaParser;
    }

    public void setSourcesDivider(SourcesDivider sourcesDivider) {
        this.sourcesDivider = sourcesDivider;
    }

    public void setPatternExtractor(PatternExtractor patternExtractor) {
        this.patternExtractor = patternExtractor;
    }

    public void setBlockInfo(BlockInfo blockInfo) {
        this.blockInfo = blockInfo;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public void setPrevious(Block previous) {
        this.previous = previous;
    }

    public void setCurrent(Block current) {
        this.current = current;
    }

    public void setParent(Block parent) {
        this.parent = parent;
    }

    public void setVariant(Pattern variant) {
        this.variant = variant;
    }

    public String getSourceCode() {
        return sourceCode == null && sourcesDivider != null ? new String(sourcesDivider.getSource()) : sourceCode;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public PandaScript getPandaScript() {
        return pandaScript;
    }

    public PandaParser getPandaParser() {
        return pandaParser;
    }

    public Dependencies getDependencies() {
        return dependencies;
    }

    public SourcesDivider getSourcesDivider() {
        return sourcesDivider;
    }

    public PatternExtractor getPatternExtractor() {
        return patternExtractor;
    }

    public BlockInfo getBlockInfo() {
        return blockInfo;
    }

    public Block getPrevious() {
        return previous;
    }

    public Block getCurrent() {
        return current;
    }

    public Block getParent() {
        return parent;
    }

    public Pattern getVariant() {
        return variant;
    }

}
