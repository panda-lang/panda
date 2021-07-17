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

package panda.interpreter.logging;

import java.io.PrintStream;

public interface Logger {

    void log(Channel channel, String message);

    void exception(Throwable throwable);

    default void fatal(String message) {
        log(Channel.FATAL, message);
    }

    default void error(String message) {
        log(Channel.ERROR, message);
    }

    default void warn(String message) {
        log(Channel.WARN, message);
    }

    default void info(String message) {
        log(Channel.INFO, message);
    }

    default void debug(String message) {
        log(Channel.DEBUG, message);
    }

    default void trace(String message) {
        log(Channel.TRACE, message);
    }

    PrintStream toPrintStream();

}
