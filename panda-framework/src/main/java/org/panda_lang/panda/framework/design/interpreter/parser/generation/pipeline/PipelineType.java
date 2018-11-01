package org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline;

import org.jetbrains.annotations.NotNull;

public class PipelineType implements Comparable<PipelineType> {

    private final String name;
    private final double priority;

    public PipelineType(String name, double priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull PipelineType o) {
        return Double.compare(priority, o.priority);
    }

    public double getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

}
