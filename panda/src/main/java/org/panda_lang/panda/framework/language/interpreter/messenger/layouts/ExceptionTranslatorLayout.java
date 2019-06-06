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

package org.panda_lang.panda.framework.language.interpreter.messenger.layouts;

import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerFormatter;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerLevel;
import org.panda_lang.panda.framework.design.interpreter.messenger.translator.PandaTranslatorLayout;
import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.source.PandaSource;
import org.panda_lang.panda.framework.language.interpreter.source.PandaURLSource;

public class ExceptionTranslatorLayout implements PandaTranslatorLayout<Throwable> {

    private String location;
    private SourceStream source;

    @Override
    public void onHandle(MessengerFormatter formatter, Throwable element) {
        /*
        MessageFormatter formatter = DefaultMessageFormatter.getFormatter()
                .register("{{message}}", () -> element.getMessage() != null ? element.getMessage() : element.getClass().getSimpleName())
                .register("{{location}}", () -> location != null ? location : "?")
                .register("{{line}}", () -> source != null && source.getCurrentLine() > -1 ? source.getCurrentLine() + 1 : "?")
                .register("{{details}}", () -> DefaultFailureTemplateBuilder.stacktraceToString(element));
        */
        this.location = null;
        this.source = null;
    }

    public ExceptionTranslatorLayout update(String location, SourceStream source) {
        this.location = location;
        this.source = source;
        return this;
    }

    @Override
    public boolean isInterrupting() {
        return true;
    }

    @Override
    public String getPrefix() {
        return "[Exception] #!# ";
    }

    @Override
    public MessengerLevel getLevel() {
        return MessengerLevel.ERROR;
    }

    @Override
    public Source getTemplateSource() {
        return new PandaSource(PandaURLSource.fromResource("/default-exception-template.messenger"));
    }

    @Override
    public Class<Throwable> getType() {
        return Throwable.class;
    }

}
