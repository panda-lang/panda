package org.panda_lang.panda.framework.design.interpreter.parser.pipeline;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.AbstractComponent;

import java.util.HashMap;
import java.util.Map;

public class PipelineComponent<P extends Parser> extends AbstractComponent<P> {

    private static final Map<String, AbstractComponent> COMPONENTS = new HashMap<>();

    private PipelineComponent(String name, Class<P> type) {
        super(name, type);
    }

    public static <T extends Parser> PipelineComponent<T> of(String name, Class<T> type) {
        return ofComponents(COMPONENTS, name, () -> new PipelineComponent<>(name, type));
    }

    public static @Nullable PipelineComponent<? extends Parser> get(String name) {
        for (Map.Entry<String, AbstractComponent> entry : COMPONENTS.entrySet()) {
            if (entry.getKey().equals(name)) {
                return (PipelineComponent<?>) entry.getValue();
            }
        }

        return null;
    }

}
