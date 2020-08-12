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

import org.panda_lang.language.architecture.statement.Statement;
import org.panda_lang.language.interpreter.messenger.MessengerTypeFormatter;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.runtime.PandaProcessFailure;
import org.panda_lang.panda.language.interpreter.messenger.MessengerDataFormatter;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.console.Colored;
import org.panda_lang.utilities.commons.console.Effect;

public final class ProcessFailureFormatter implements MessengerDataFormatter<PandaProcessFailure> {

    @Override
    public void onInitialize(MessengerTypeFormatter<PandaProcessFailure> typeFormatter) {
        typeFormatter
                .register("{{message}}", (messengerFormatter, e) -> {
                    return Colored.on(e.getException().getClass().getName()).effect(Effect.RED) + ": " + Colored.on(e.getException().getMessage()).effect(Effect.RED_BOLD);
                })
                .register("{{stacktrace}}", (messengerFormatter, runtimeException) -> {
                    StringBuilder message = new StringBuilder();

                    for (Statement statement : runtimeException.getStack().getLivingFramesOnStack()) {
                        Location location = statement.getSourceLocation();

                        message.append("  at ").append(location.getSource().getId()).append(" [")
                                .append(Colored.on(location.getDisplayLine()).effect(Effect.RED))
                                .append(":")
                                .append(Colored.on(location.getIndex()).effect(Effect.RED)).append("]")
                                .append(System.lineSeparator());
                    }

                    return StringUtils.trimEnd(message.toString());
                });
    }

    @Override
    public Class<PandaProcessFailure> getType() {
        return PandaProcessFailure.class;
    }

}
