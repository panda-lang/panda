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

package org.panda_lang.panda.framework.language.interpreter.lexer;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.InterpreterFailure;

public class PandaLexerException extends InterpreterFailure {

    private final String tokenPreview;
    private final String linePreview;
    private final String location;
    private final int line;

    public PandaLexerException(String message, String tokenPreview, String linePreview, String location, int line) {
        super(message);

        this.tokenPreview = tokenPreview;
        this.linePreview = linePreview;
        this.location = location;
        this.line = line;
    }

    @Override
    public @Nullable String getDetails() {
        return null;
    }

    @Override
    public String getElement() {
        return tokenPreview;
    }

    @Override
    public String getSource() {
        return linePreview;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public int getLine() {
        return line;
    }

}
