package org.panda_lang.language.interpreter.parser.stage;

import org.jetbrains.annotations.NotNull;

final class IdentifiedOrderedTask implements Comparable<IdentifiedOrderedTask> {

    private final String id;
    private final StageOrder order;
    private final StageTask task;

    IdentifiedOrderedTask(String id, StageOrder order, StageTask task) {
        this.id = id;
        this.order = order;
        this.task = task;
    }

    @Override
    public int compareTo(@NotNull IdentifiedOrderedTask to) {
        return order.compareTo(to.order);
    }

    String getId() {
        return id;
    }

    StageTask getTask() {
        return task;
    }

}
