/*
 * Copyright (c) 2015-2019 Dzikoysk
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

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.generation.GenerationCycle;
import org.panda_lang.framework.design.interpreter.parser.generation.GenerationPhase;
import org.panda_lang.framework.design.interpreter.parser.generation.GenerationTask;
import org.panda_lang.framework.design.interpreter.parser.generation.GenerationTaskPriority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PandaGenerationPhase implements GenerationPhase {

    private static final AtomicInteger ID = new AtomicInteger();

    private final int id;
    private final GenerationCycle cycle;
    private final Map<GenerationTaskPriority, List<GenerationUnit>> tasks = new HashMap<>();
    private GenerationUnit currentUnit;

    public PandaGenerationPhase(GenerationCycle cycle) {
        this.id = ID.getAndIncrement();
        this.cycle = cycle;
    }

    @Override
    public void callTasks() throws Exception {
        Map<GenerationTaskPriority, List<GenerationUnit>> unitsMap = new TreeMap<>(tasks);
        tasks.clear();

        for (List<GenerationUnit> units : unitsMap.values()) {
            for (GenerationUnit unit : units) {
                currentUnit = unit;
                unit.getTask().call(cycle, unit.getDelegated());
            }
        }

        currentUnit = null;
    }

    @Override
    public GenerationPhase delegate(GenerationTaskPriority priority, GenerationTask task, Context delegated) {
        tasks.computeIfAbsent(priority, (key) -> new ArrayList<>(2)).add(new GenerationUnit(task, delegated));
        return this;
    }

    @Override
    public int countTasks() {
        return tasks.size();
    }

    @Override
    public String toString() {
        if (countTasks() == 0) {
            return "layer: empty";
        }

        String layerName = currentUnit != null ? currentUnit.getTask().toString() : "<ne";
        return layerName + ":" + id + ">/" + countTasks();
    }

}
