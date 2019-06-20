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
import org.panda_lang.panda.framework.design.interpreter.parser.generation.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.GenerationPipeline;

public class PandaGenerationPipeline implements GenerationPipeline {

    private final String name;
    private final Generation generation;
    private GenerationLayer currentLayer;
    private GenerationLayer nextLayer;

    public PandaGenerationPipeline(Generation generation, String name) {
        this.name = name;
        this.generation = generation;
        this.currentLayer = new PandaGenerationLayer(this);
        this.nextLayer = new PandaGenerationLayer(this);
    }

    @Override
    public boolean execute(ParserData data) throws Exception {
        while (true) {
            currentLayer.callDelegates(this, data);

            if (currentLayer.countDelegates() > 0) {
                continue;
            }

            if (nextLayer.countDelegates() == 0) {
                break;
            }

            currentLayer = nextLayer;
            nextLayer = new PandaGenerationLayer(this);

            //System.out.println("CNT: " + generation.countDelegates(this));

            if (generation.countDelegates(this) > 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int countDelegates() {
        return currentLayer.countDelegates() + nextLayer.countDelegates();
    }

    @Override
    public GenerationLayer currentLayer() {
        return currentLayer;
    }

    @Override
    public GenerationLayer nextLayer() {
        return nextLayer;
    }

    @Override
    public Generation generation() {
        return generation;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        if (countDelegates() == 0) {
            return name + " { <empty> }";
        }

        return name + " { c: " + currentLayer + " | n: " + nextLayer + " }";
    }

}
