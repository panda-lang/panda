package org.panda_lang.panda.framework.design.interpreter.parser.component;

import org.panda_lang.panda.framework.PandaFrameworkException;

import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractComponent<R> {

    private final String name;
    private final Class<R> type;

    protected AbstractComponent(String name, Class<R> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<R> getType() {
        return type;
    }

    protected static <TYPE extends AbstractComponent, RETURN> TYPE ofComponents(Map<String, AbstractComponent> components, String name, Supplier<TYPE> supplier) {
        AbstractComponent existingComponent = components.get(name);

        if (existingComponent != null) {
            throw new PandaFrameworkException("Component '" + name + "' already exists (type: " + existingComponent.getType() + ")");
        }

        TYPE component = supplier.get();
        components.put(name, component);

        return component;
    }

}
