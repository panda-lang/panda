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

package org.panda_lang.framework;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.interpreter.source.IndicatedSource;
import org.panda_lang.utilities.commons.function.Option;

/**
 * Represents errors occurred in the framework
 */
public interface Failure {

    /**
     * Get indicated source
     *
     * @return the indicated source
     */
    IndicatedSource getIndicatedSource();

    /**
     * Get additional information about the failure
     *
     * @return the note
     */
    Option<String> getNote();

    /**
     * Returns the cause of this throwable or {@code null} if the
     * cause is nonexistent or unknown. (The cause is the throwable that
     * caused this throwable to get thrown.)
     *
     * @return the cause
     */
    @Nullable Throwable getCause();

    /**
     * Get failure message
     *
     * @return the failure message
     */
    String getMessage();

    /**
     * Get failure stacktrace
     *
     * @return the array of stack trace elements
     */
    StackTraceElement[] getStackTrace();

}
