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

package org.panda_lang.panda.language.interpreter.messenger.layouts;

import org.panda_lang.language.interpreter.messenger.MessengerFormatter;
import org.panda_lang.language.interpreter.source.Source;
import org.panda_lang.language.interpreter.source.PandaSource;
import org.panda_lang.language.interpreter.source.PandaURLSource;
import org.panda_lang.panda.language.interpreter.messenger.PandaTranslatorLayout;
import org.slf4j.event.Level;

import java.util.Map;

public final class ExceptionTranslatorLayout implements PandaTranslatorLayout<Throwable> {

    @Override
    public void onHandle(MessengerFormatter formatter, Throwable element, Map<String, Object> context) {
        context.put("throwable", element);
        context.put("stacktrace", element.getStackTrace());

        formatter.getTypeFormatter(Throwable.class)
                .register("{{message}}", (f, e) -> {
                    String simpleName = element.getClass().getSimpleName();
                    return element.getMessage() != null ? simpleName + ": " + element.getMessage() : simpleName;
                });
    }

    @Override
    public boolean isInterrupting() {
        return true;
    }

    @Override
    public String getPrefix() {
        return " #!# ";
    }

    @Override
    public Level getLevel() {
        return Level.ERROR;
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
