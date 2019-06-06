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

import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerTypeFormatter;
import org.panda_lang.panda.framework.design.interpreter.messenger.formatters.MessengerDataFormatter;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.PackageUtils;

public final class ThrowableFormatter implements MessengerDataFormatter<Throwable> {

    @Override
    public void onInitialize(MessengerTypeFormatter<Throwable> typeFormatter) {
        typeFormatter
                .register("{{message}}", (formatter, exception) -> exception.getMessage() == null ? exception.getClass() : exception.getMessage())
                .register("{{stacktrace}}", (formatter, exception) -> new StacktraceSupplier(exception).get())
                .register("{{stacktrace-last}}", (formatter, exception) -> {
                    StackTraceElement lastElement = ArrayUtils.get(exception.getStackTrace(), 0);

                    if (lastElement == null) {
                        return "<unknown>";
                    }

                    return PackageUtils.getShortenPackage(lastElement.getClassName()) + " (" + lastElement.getFileName() + ":" + lastElement.getLineNumber() + ")";
                });
    }

    @Override
    public Class<Throwable> getType() {
        return Throwable.class;
    }

}
