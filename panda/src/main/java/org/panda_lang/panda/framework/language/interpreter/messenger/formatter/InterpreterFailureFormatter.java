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

package org.panda_lang.panda.framework.language.interpreter.messenger.formatter;

import org.panda_lang.panda.framework.design.interpreter.InterpreterFailure;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerTypeFormatter;
import org.panda_lang.panda.framework.design.interpreter.messenger.formatters.MessengerDataFormatter;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserFailure;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationPipeline;
import org.panda_lang.panda.utilities.commons.console.Colored;
import org.panda_lang.panda.utilities.commons.console.Effect;

public final class InterpreterFailureFormatter implements MessengerDataFormatter<InterpreterFailure> {

    @Override
    public void onInitialize(MessengerTypeFormatter<InterpreterFailure> typeFormatter) {
        typeFormatter
                .register("{{line}}", (formatter, failure) -> failure.getLine() < 0 ? "?" : failure.getLine() + 1)
                .register("{{position}}", (formatter, failure) -> "~") // TODO: position
                .register("{{location}}", (formatter, failure) -> failure.getLocation())
                .register("{{details}}", (formatter, failure) -> failure.getDetails() != null ? failure.getDetails() : "<unknown details>")
                .register("{{index}}", (formatter, failure) -> failure.getSource().indexOf(failure.getElement()))
                .register("{{source}}", (formatter, failure) -> {
                    String source = failure.getSource();
                    String element = failure.getElement();

                    int index = source.indexOf(element);
                    int endIndex = index + element.length();

                    return index < 0 ? source : source.substring(0, index)
                        + Colored.on(source.substring(index, endIndex)).effect(Effect.RED)
                        + source.substring(endIndex);
                })
                .register("{{generation}}", (formatter, failure) -> {
                    if (!(failure instanceof ParserFailure)) {
                        return "<unknown>";
                    }

                    ParserData data = ((ParserFailure) failure).getData();
                    Generation generation = data.getComponent(UniversalComponents.GENERATION);
                    GenerationPipeline pipeline = generation.currentPipeline();

                    if (pipeline == null) {
                        return "<out of pipeline>";
                    }

                    return pipeline.currentLayer().toString() + " (" + pipeline.name() + ")";
                });
    }

    @Override
    public Class<InterpreterFailure> getType() {
        return InterpreterFailure.class;
    }

}
