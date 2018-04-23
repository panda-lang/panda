package org.panda_lang.panda.framework.design.interpreter.parser.component;

import org.panda_lang.panda.framework.*;

import java.util.*;

public class Component<T> {

    private static final Map<String, Component<?>> components = new HashMap<>();

    private final String name;
    private final Class<T> type;

    private Component(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    public static <T> Component<T> of(String name, Class<T> type) {
        Component<?> existingComponent = components.get(name);

        if (existingComponent != null) {
            throw new PandaFrameworkException("Component '" + name + "' already exists (type: " + existingComponent.getType() + ")");
        }

        Component<T> component = new Component<>(name, type);
        components.put(name, component);

        return component;
    }

}
