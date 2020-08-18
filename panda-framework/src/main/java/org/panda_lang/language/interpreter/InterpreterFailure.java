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

package org.panda_lang.language.interpreter;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.Failure;
import org.panda_lang.language.PandaFrameworkException;
import org.panda_lang.language.interpreter.source.IndicatedSource;
import org.panda_lang.utilities.commons.function.Option;

public class InterpreterFailure extends PandaFrameworkException implements Failure {

    private final IndicatedSource indicatedSource;
    private final String note;

    public InterpreterFailure(IndicatedSource indicatedSource, String message, @Nullable String note) {
        super(message);
        this.indicatedSource = indicatedSource;
        this.note = note;
    }

    public InterpreterFailure(Throwable cause, IndicatedSource indicatedSource, String message, String note) {
        super(message, cause);
        this.indicatedSource = indicatedSource;
        this.note = note;
    }

    @Override
    public Option<String> getNote() {
        return Option.of(note);
    }

    @Override
    public IndicatedSource getIndicatedSource() {
        return indicatedSource;
    }

}
