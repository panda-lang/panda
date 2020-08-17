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

package org.panda_lang.language.runtime;

import java.io.PrintWriter;

public final class PandaProcessFailure extends PandaRuntimeException {

    private final ProcessStack stack;
    private final Exception exception;

    public PandaProcessFailure(ProcessStack stack, Exception exception) {
        super(exception);
        this.stack = stack;
        this.exception = exception;
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        /*
        &b- - ~ ~< Runtime Exception >~ ~ - -&r

        {{message}}
        {{stacktrace}}

        {{exception.short}}

        Environment: {{environment}}

        End of Exception
         */
        super.printStackTrace(s);
    }

    public Exception getException() {
        return exception;
    }

    public ProcessStack getStack() {
        return stack;
    }

}
