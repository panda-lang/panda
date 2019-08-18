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

package org.panda_lang.panda.framework.language.interpreter.messenger.formatters;

import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerTypeFormatter;
import org.panda_lang.panda.framework.design.interpreter.messenger.formatter.MessengerDataFormatter;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.GenerationCycle;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;

import java.util.Optional;

public final class ParserFailureFormatter implements MessengerDataFormatter<PandaParserFailure> {

    @Override
    public void onInitialize(MessengerTypeFormatter<PandaParserFailure> typeFormatter) {
        typeFormatter
                .register("{{note}}", (formatter, failure) -> failure.getNote())
                .register("{{cycle}}", (formatter, failure) -> {
                    Generation generation = failure.getContext().getComponent(UniversalComponents.GENERATION);
                    Optional<GenerationCycle> cycleValue = generation.getCurrentCycle();

                    if (!cycleValue.isPresent()) {
                        return "<out of cycle>";
                    }

                    GenerationCycle cycle = cycleValue.get();
                    return cycle.name() + " { " + cycle.currentPhase().toString() + " }";
                });
    }

    @Override
    public Class<PandaParserFailure> getType() {
        return PandaParserFailure.class;
    }

}
