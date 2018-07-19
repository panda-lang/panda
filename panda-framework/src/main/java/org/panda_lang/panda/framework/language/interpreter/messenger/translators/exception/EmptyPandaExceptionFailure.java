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

package org.panda_lang.panda.framework.language.interpreter.messenger.translators.exception;

import org.panda_lang.panda.framework.design.interpreter.InterpreterFailure;

public class EmptyPandaExceptionFailure extends InterpreterFailure {

    public EmptyPandaExceptionFailure() {
        super(null);
    }

    @Override
    public String getDetails() {
        return null;
    }

    @Override
    public String getElement() {
        return null;
    }

    @Override
    public String getSource() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public String getLocation() {
        return null;
    }

    @Override
    public int getLine() {
        return 0;
    }

}
