package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.framework.interpreter.parser.ParserInfo;

import java.util.HashMap;
import java.util.Map;

public class PandaParserInfo implements ParserInfo {

    private final Map<String, Object> components;

    public PandaParserInfo() {
        this(new HashMap<>());
    }

    private PandaParserInfo(Map<String, Object> components) {
        this.components = components;
    }

    @Override
    public PandaParserInfo clone() {
        return new PandaParserInfo(new HashMap<>(components));
    }

    @Override
    public void setComponent(String componentName, Object component) {
        this.components.put(componentName, component);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <T> T getComponent(String componentName) {
        return (T) components.get(componentName);
    }

}
