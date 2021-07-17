/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package panda.interpreter.parser.stage;

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
