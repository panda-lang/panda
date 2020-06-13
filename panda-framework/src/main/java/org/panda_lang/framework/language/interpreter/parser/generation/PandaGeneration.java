/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.parser.generation;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.parser.generation.CycleType;
import org.panda_lang.framework.design.interpreter.parser.generation.Generation;
import org.panda_lang.framework.design.interpreter.parser.generation.GenerationCycle;
import org.panda_lang.utilities.commons.function.Option;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PandaGeneration implements Generation {

    private final Map<String, GenerationCycle> cycles = new LinkedHashMap<>();
    private GenerationCycle currentCycle;

    public PandaGeneration initialize(List<? extends CycleType> types) {
        Collections.sort(types);

        for (CycleType type : types) {
            cycles.put(type.getName(), new PandaGenerationCycle(this, type.getName()));
        }

        return this;
    }

    @Override
    public void launch() throws Exception {
        while (countTasks(null) > 0) {
            executeOnce();
        }
    }

    private void executeOnce() throws Exception {
        for (GenerationCycle cycle : cycles.values()) {
            currentCycle = cycle;

            if (!cycle.execute()) {
                break;
            }
        }

        currentCycle = null;
    }

    @Override
    public int countTasks(@Nullable GenerationCycle to) {
        int count = 0;

        for (GenerationCycle cycle : cycles.values()) {
            count += cycle.countTasks();

            if (cycle.equals(to)) {
                break;
            }
        }

        return count;
    }

    @Override
    public int countTasks() {
        return countTasks(null);
    }

    @Override
    public Option<GenerationCycle> getCurrentCycle() {
        return Option.of(currentCycle);
    }

    @Override
    public GenerationCycle getCycle(CycleType type) {
        return getCycle(type.getName());
    }

    @Override
    public GenerationCycle getCycle(String name) {
        GenerationCycle cycle = cycles.get(name);

        if (cycle == null) {
            throw new IllegalArgumentException("Cycle " + name + " does not exist");
        }

        return cycle;
    }

    @Override
    public String toString() {
        return "Generation { cycles: " + cycles.toString() + " }";
    }

}
