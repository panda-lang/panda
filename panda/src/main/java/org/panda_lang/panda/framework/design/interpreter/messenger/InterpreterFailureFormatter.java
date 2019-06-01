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

package org.panda_lang.panda.framework.design.interpreter.messenger;

import org.panda_lang.panda.framework.design.interpreter.InterpreterFailure;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserFailure;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationPipeline;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.PackageUtils;
import org.panda_lang.panda.utilities.commons.console.Colored;
import org.panda_lang.panda.utilities.commons.console.Effect;
import org.panda_lang.panda.utilities.commons.text.MessageFormatter;

import java.util.function.BiConsumer;

public final class InterpreterFailureFormatter implements BiConsumer<MessageFormatter, InterpreterFailure> {

    @Override
    public void accept(MessageFormatter formatter, InterpreterFailure exception) {
        String source = exception.getSource();
        String element = exception.getElement();

        int index = source.indexOf(element);
        int endIndex = index + element.length();

        formatter.register("{{line}}", () -> exception.getLine() < 0 ? "?" : exception.getLine() + 1)
                .register("{{position}}", () -> "~") // TODO: position
                .register("{{location}}", exception::getLocation)
                .register("{{message}}", exception::getMessage)
                .register("{{index}}", () -> index)
                .register("{{source}}", () -> index < 0 ? source : source.substring(0, index)
                        + Colored.on(source.substring(index, endIndex)).effect(Effect.RED)
                        + source.substring(endIndex))
                .register("{{stacktrace-last}}", () -> {
                    StackTraceElement lastElement = ArrayUtils.get(exception.getStackTrace(), 0);

                    if (lastElement == null) {
                        return "<unknown>";
                    }

                    return PackageUtils.getShortenPackage(lastElement.getClassName()) + " (" + lastElement.getFileName() + ":" + lastElement.getLineNumber() + ")";
                })
                .register("{{stacktrace}}", new StacktraceSupplier(exception))
                .register("{{details}}", () -> exception.getDetails())
                .register("{{generation}}", () -> {
                    if (!(exception instanceof ParserFailure)) {
                        return "<unknown>";
                    }

                    ParserData data = ((ParserFailure) exception).getData();
                    Generation generation = data.getComponent(UniversalComponents.GENERATION);
                    GenerationPipeline pipeline = generation.currentPipeline();

                    if (pipeline == null) {
                        return "<out of pipeline>";
                    }

                    return pipeline.currentLayer().toString() + " (" + pipeline.name() + ")";
                });

    }

}
