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

package org.panda_lang.framework.design.interpreter;

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.Failure;
import org.panda_lang.framework.design.interpreter.source.IndicatedSource;

/**
 * InterpreterFailures are dedicated exceptions thrown by the Panda Framework.
 * It contains extra data about the error and may be used to enhance logging/tooling.
 */
public abstract class InterpreterFailure extends PandaFrameworkException implements Failure {

    protected InterpreterFailure(String message, Throwable cause) {
        super(message, cause);
    }

    protected InterpreterFailure(String message) {
        super(message);
    }

    /**
     * Get indicated source
     *
     * @return the indicated source
     */
    public abstract IndicatedSource getIndicatedSource();

}
