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

package org.panda_lang.panda.framework.language.interpreter.messenger.translators;

import org.panda_lang.panda.framework.design.interpreter.*;
import org.panda_lang.panda.framework.design.interpreter.messenger.*;
import org.panda_lang.panda.framework.language.interpreter.messenger.*;
import org.panda_lang.panda.framework.language.interpreter.messenger.defaults.*;
import org.panda_lang.panda.utilities.commons.redact.MessageFormatter;
import org.panda_lang.panda.utilities.redact.format.*;

public class InterpreterFailureTranslator implements MessengerMessageTranslator<InterpreterFailure> {

    private final Interpretation interpretation;

    public InterpreterFailureTranslator(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    @Override
    public void handle(Messenger messenger, InterpreterFailure element) {
        interpretation.getFailures().add(element);

        MessageFormatter formatter = DefaultMessageFormatter.getFormatter()
                .register("{{details}}", () -> DefaultFailureTemplateBuilder.indentation(element.getDetails()));

        DefaultFailureTemplateBuilder templateBuilder = new DefaultFailureTemplateBuilder()
                .applyPlaceholders(formatter, element)
                .includeCause()
                .includeDetails(element.getDetails())
                .includeSource()
                .includeMarker(formatter.getValue("{{index}}"))
                .includeEnvironment()
                .includeEnd();

        PandaMessengerMessage message = new PandaMessengerMessage(MessengerMessage.Level.FAILURE, templateBuilder.getAsLines(formatter, "InterpreterFailure"));
        messenger.sendMessage(message);
    }

    @Override
    public Class<InterpreterFailure> getType() {
        return InterpreterFailure.class;
    }

}
