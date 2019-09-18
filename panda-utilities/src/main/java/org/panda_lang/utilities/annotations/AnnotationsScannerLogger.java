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

package org.panda_lang.utilities.annotations;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.function.Consumer;

public class AnnotationsScannerLogger {

    private final @Nullable Logger logger;
    private boolean mute;

    public AnnotationsScannerLogger(@Nullable Logger logger) {
        this.logger = logger;
    }

    public void debug(String message) {
        log(logger -> logger.debug(message));
    }

    public void info(String message) {
        log(logger -> logger.info(message));
    }

    public void warn(String message) {
        log(logger -> logger.warn(message));
    }

    public void error(String message) {
        log(logger -> logger.error(message));
    }

    public void exception(Throwable exception) {
        log(logger -> exception.printStackTrace());
    }

    private void log(Consumer<Logger> loggerConsumer) {
        if (logger != null && !mute) {
            loggerConsumer.accept(logger);
        }
    }

    public void mute() {
        this.mute = true;
    }

    public void unmute() {
        this.mute = false;
    }

}
