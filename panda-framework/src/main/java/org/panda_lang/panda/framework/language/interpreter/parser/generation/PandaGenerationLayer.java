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

package org.panda_lang.panda.framework.language.interpreter.parser.generation;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PandaGenerationLayer implements GenerationLayer {

    private static final AtomicInteger idAssigner = new AtomicInteger();

    private final int id;
    private final GenerationPipeline pipeline;

    private final List<GenerationUnit> before = new ArrayList<>(1);
    private final List<GenerationUnit> delegates = new ArrayList<>();
    private final List<GenerationUnit> after = new ArrayList<>(1);

    private GenerationUnit currentUnit;

    public PandaGenerationLayer(GenerationPipeline pipeline) {
        this.id = idAssigner.getAndIncrement();
        this.pipeline = pipeline;
    }

    private void call(ParserData currentData, GenerationLayer nextLayer) throws Throwable {
        call(before, currentData, nextLayer);
        call(delegates, currentData, nextLayer);
        call(after, currentData, nextLayer);
    }

    private void call(List<GenerationUnit> units, ParserData currentInfo, GenerationLayer nextLayer) throws Throwable {
        List<GenerationUnit> unitList = new ArrayList<>(units);
        units.clear();

        for (GenerationUnit unit : unitList) {
            currentUnit = unit;
            unit.getCallback().call(pipeline, unit.getDelegated());
        }

        currentUnit = null;
    }

    @Override
    public void callDelegates(GenerationPipeline pipeline, ParserData data) throws Throwable {
        call(data, pipeline.nextLayer());
    }

    @Override
    public GenerationLayer delegateBefore(GenerationCallback callback, ParserData delegated) {
        return delegate(before, callback, delegated);
    }

    @Override
    public GenerationLayer delegate(GenerationCallback callback, ParserData delegated) {
        return delegate(delegates, callback, delegated);
    }

    @Override
    public GenerationLayer delegateAfter(GenerationCallback callback, ParserData delegated) {
        return delegate(after, callback, delegated);
    }

    private GenerationLayer delegate(List<GenerationUnit> units, GenerationCallback callback, ParserData delegated) {
        units.add(new PandaGenerationUnit(callback, delegated));
        return this;
    }

    @Override
    public int countDelegates() {
        return before.size() + delegates.size() + after.size();
    }

    @Override
    public String toString() {
        if (countDelegates() == 0) {
            return "<empty>";
        }

        String layerName = currentUnit != null ? currentUnit.getCallback().toString() : "<ne";
        return layerName + ":" + id + ">/" + countDelegates();
    }

}
