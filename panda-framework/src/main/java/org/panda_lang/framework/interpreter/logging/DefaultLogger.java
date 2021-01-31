/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.interpreter.logging;

import org.panda_lang.utilities.commons.console.Effect;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

public class DefaultLogger implements Logger {

    private final Consumer<String> messageConsumer;
    private final Channel threshold;
    private final ErrorFormatter errorFormatter = new ErrorFormatter(this);

    public DefaultLogger(Consumer<String> messageConsumer) {
        this(Channel.INFO, messageConsumer);
    }

    public DefaultLogger(Channel threshold, Consumer<String> messageConsumer) {
        this.threshold = threshold;
        this.messageConsumer = messageConsumer;
    }

    @Override
    public void log(Channel channel, String message) {
        if (channel.getPriority() >= threshold.getPriority()) {
            messageConsumer.accept(Effect.paint(message));
        }
    }

    @Override
    public void error(String message) {
        log(Channel.ERROR, "# " + message.replace("\n", "\n# "));
    }

    @Override
    public void exception(Throwable throwable) {
        errorFormatter.print(throwable);
    }

    @Override
    public PrintStream toPrintStream() {
        return new PrintStream(new OutputStream() {

            private final StringBuilder content = new StringBuilder();

            @Override
            public void write(int b) {
                byte[] bytes = new byte[1];
                bytes[0] = (byte) (b & 0xff);
                content.append(new String(bytes));

                if (content.toString().endsWith("\n")) {
                    content.setLength(content.length() - 1);
                    flush();
                }
            }

            @Override
            public void flush() {
                messageConsumer.accept(content.toString());
                content.setLength(0);
            }
        });
    }

}
