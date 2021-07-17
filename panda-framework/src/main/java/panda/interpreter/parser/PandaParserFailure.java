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

package panda.interpreter.parser;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.Failure;
import panda.interpreter.InterpreterFailure;
import panda.interpreter.source.Location;
import panda.interpreter.source.PandaIndicatedSource;
import panda.interpreter.token.Snippetable;

public class PandaParserFailure extends InterpreterFailure implements Failure {

    public PandaParserFailure(Contextual<?> contextual, Location location, String message) {
        this(contextual, location, message, null);
    }

    public PandaParserFailure(Contextual<?> contextual, Location location, String message, @Nullable String note) {
        this(contextual, contextual.toContext().getScriptSource().getLine(location.getLine()), message, note);
    }

    public PandaParserFailure(Contextual<?> contextual, String message) {
        this(contextual, contextual.toContext().getSource(), message);
    }

    public PandaParserFailure(Contextual<?> contextual, Snippetable indicated, String message) {
        this(contextual, indicated, message, null);
    }

    public PandaParserFailure(Contextual<?> contextual, Snippetable indicated, String message, @Nullable String note) {
        this(contextual.toContext().getSource(), indicated, message, note);
    }

    public PandaParserFailure(Snippetable source, Snippetable indicated, String message) {
        this(source, indicated, message, null);
    }

    public PandaParserFailure(Snippetable source, Snippetable indicated, String message, @Nullable String note) {
        super(new PandaIndicatedSource(source, indicated), message, note);
    }

    public PandaParserFailure(Throwable cause, Contextual<?> contextual, Snippetable indicated, String message, @Nullable String note) {
        this(cause, contextual.toContext().getSource(), indicated, message, note);
    }

    public PandaParserFailure(Throwable cause, Snippetable source, Snippetable indicated, String message, @Nullable String note) {
        super(cause, new PandaIndicatedSource(source, indicated), message, note);
    }

}
