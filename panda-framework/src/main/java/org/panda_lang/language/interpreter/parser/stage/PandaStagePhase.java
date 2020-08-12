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

package org.panda_lang.language.interpreter.parser.stage;

import org.panda_lang.language.interpreter.parser.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class PandaStagePhase implements StagePhase {

    private static final AtomicInteger ID = new AtomicInteger();

    private final int id;
    private final Stage cycle;
    private final Map<StageOrder, List<DelegatedTask>> tasks = new HashMap<>();
    private DelegatedTask currentUnit;

    public PandaStagePhase(Stage cycle) {
        this.id = ID.getAndIncrement();
        this.cycle = cycle;
    }

    @Override
    public void callTasks() throws Exception {
        Map<StageOrder, List<DelegatedTask>> unitsMap = new TreeMap<>(tasks);
        tasks.clear();

        for (List<DelegatedTask> units : unitsMap.values()) {
            for (DelegatedTask unit : units) {
                currentUnit = unit;
                unit.getTask().call(cycle, unit.getDelegated());
            }
        }

        currentUnit = null;
    }

    @Override
    public StagePhase delegate(StageOrder priority, StageTask<?> task, Context delegated) {
        tasks.computeIfAbsent(priority, (key) -> new ArrayList<>(2)).add(new DelegatedTask(task, delegated));
        return this;
    }

    @Override
    public int countTasks() {
        return tasks.size();
    }

    public DelegatedTask getCurrentUnit() {
        return currentUnit;
    }

    @Override
    public String toString() {
        return "GenerationPhase " + id + ": " + countTasks() + " tasks";
    }

}
