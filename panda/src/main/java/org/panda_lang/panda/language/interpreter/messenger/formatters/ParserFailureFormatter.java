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

package org.panda_lang.panda.language.interpreter.messenger.formatters;

import org.panda_lang.framework.design.interpreter.messenger.MessengerTypeFormatter;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.ParserFailure;
import org.panda_lang.framework.design.interpreter.parser.stage.StageController;
import org.panda_lang.framework.design.interpreter.parser.stage.Stage;
import org.panda_lang.panda.language.interpreter.messenger.MessengerDataFormatter;

import org.panda_lang.utilities.commons.function.Option;

public final class ParserFailureFormatter implements MessengerDataFormatter<ParserFailure> {

    @Override
    public void onInitialize(MessengerTypeFormatter<ParserFailure> typeFormatter) {
        typeFormatter
                .register("{{note}}", (formatter, failure) -> failure.getNote())
                .register("{{cycle}}", (formatter, failure) -> {
                    StageController stageController = failure.getContext().getComponent(Components.GENERATION);
                    Option<Stage> cycleValue = stageController.getCurrentCycle();

                    if (cycleValue.isEmpty()) {
                        return "<out of cycle>";
                    }

                    Stage cycle = cycleValue.get();
                    return cycle.name() + " { " + cycle.currentPhase().toString() + " }";
                });
    }

    @Override
    public Class<ParserFailure> getType() {
        return ParserFailure.class;
    }

}
