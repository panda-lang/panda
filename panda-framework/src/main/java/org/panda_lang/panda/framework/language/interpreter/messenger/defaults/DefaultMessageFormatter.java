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

package org.panda_lang.panda.framework.language.interpreter.messenger.defaults;

import org.panda_lang.panda.framework.PandaFrameworkConstants;
import org.panda_lang.panda.utilities.commons.redact.MessageFormatter;

public class DefaultMessageFormatter {

    private static final MessageFormatter formatter = new MessageFormatter()
            .register("{{newline}}", System::lineSeparator)
            .register("{{java.version}}", () -> System.getProperty("java.version"))
            .register("{{panda.version}}", () -> PandaFrameworkConstants.VERSION)
            .register("{{os}}", () -> System.getProperty("os.name"));

    public static MessageFormatter getOriginalFormatter() {
        return formatter;
    }

    public static MessageFormatter getFormatter() {
        return formatter.fork();
    }

}
