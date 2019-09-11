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

package org.panda_lang.panda.language.interpreter.parser.pipeline;

import org.panda_lang.panda.framework.design.interpreter.InterpreterFailure;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.HandleResult;

import java.util.Optional;

final class PandaHandleResult<T extends Parser> implements HandleResult<T> {

    private final T parser;
    private final InterpreterFailure failure;

    public PandaHandleResult(T parser) {
        this(parser, null);
    }

    public PandaHandleResult(InterpreterFailure failure) {
        this(null, failure);
    }

    private PandaHandleResult(T parser, InterpreterFailure failure) {
        this.parser = parser;
        this.failure = failure;
    }

    @Override
    public Optional<InterpreterFailure> getFailure() {
        return Optional.ofNullable(failure);
    }

    @Override
    public Optional<T> getParser() {
        return Optional.ofNullable(parser);
    }

}
