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

package org.panda_lang.panda.language.interpreter.messenger.layouts;

import org.panda_lang.framework.design.interpreter.messenger.MessengerFormatter;
import org.panda_lang.framework.design.interpreter.messenger.MessengerLevel;
import org.panda_lang.panda.language.interpreter.messenger.PandaTranslatorLayout;
import org.panda_lang.framework.design.interpreter.source.Source;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.source.PandaSource;
import org.panda_lang.framework.language.interpreter.source.PandaURLSource;

import java.util.Map;

public class ExceptionTranslatorLayout implements PandaTranslatorLayout<Throwable> {

    private String location;
    private SourceStream source;

    @Override
    public void onHandle(MessengerFormatter formatter, Throwable element, Map<String, Object> context) {
        context.put("stacktrace", element.getStackTrace());

        formatter.getTypeFormatter(Throwable.class)
                .register("{{message}}", (f, e) -> {
                    String simpleName = element.getClass().getSimpleName();
                    return element.getMessage() != null ? simpleName + ": " + element.getMessage() :simpleName;
                });
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
