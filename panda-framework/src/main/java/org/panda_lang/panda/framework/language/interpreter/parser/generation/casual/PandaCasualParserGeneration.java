/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.generation.casual;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationType;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;

public class PandaCasualParserGeneration implements CasualParserGeneration {

    private GenerationLayer currentLayer;
    private GenerationLayer nextLayer;

    public PandaCasualParserGeneration() {
        this.currentLayer = new PandaCasualParserGenerationLayer();
        this.nextLayer = new PandaCasualParserGenerationLayer();
    }

    @Override
    public void execute(ParserData currentData) {
        while (currentLayer != null) {
            currentLayer.call(currentData, nextLayer);

            if (nextLayer.countDelegates() == 0) {
                break;
            }

            currentLayer = nextLayer;
            nextLayer = new PandaCasualParserGenerationLayer();
        }
    }

    @Override
    public GenerationLayer getLayer(CasualParserGenerationType generationType) {
        return generationType == CasualParserGenerationType.CURRENT ? currentLayer : nextLayer;
    }

}
