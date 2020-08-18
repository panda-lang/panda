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

package org.panda_lang.language.interpreter.parser;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.interpreter.PandaInterpreterFailure;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.source.PandaIndicatedSource;
import org.panda_lang.language.interpreter.token.Snippetable;

public class PandaParserFailure extends PandaInterpreterFailure implements ParserFailure {

    private final Context context;

    public PandaParserFailure(Contextual contextual, Location location, String message) {
        this(contextual, location, message, null);
    }

    public PandaParserFailure(Contextual contextual, Location location, String message, @Nullable String note) {
        this(contextual, contextual.toContext().getComponent(Components.SOURCE).getLine(location.getLine()), message, note);
    }

    public PandaParserFailure(Contextual contextual, Snippetable indicated, String message) {
        this(contextual, indicated, message, null);
    }

    public PandaParserFailure(Contextual contextual, Snippetable indicated, String message, @Nullable String note) {
        this(contextual, contextual.toContext().getComponent(Components.CURRENT_SOURCE), indicated, message, note);
    }

    public PandaParserFailure(Contextual contextual, Snippetable source, Snippetable indicated, String message) {
        this(contextual, source, indicated, message, null);
    }

    public PandaParserFailure(Contextual contextual, Snippetable source, Snippetable indicated, String message, @Nullable String note) {
        super(new PandaIndicatedSource(source, indicated), message, note);
        this.context = contextual.toContext();
    }

    public PandaParserFailure(Throwable cause, Contextual contextual, Snippetable indicated, String message, @Nullable String note) {
        this(cause, contextual, contextual.toContext().getComponent(Components.CURRENT_SOURCE), indicated, message, note);
    }

    public PandaParserFailure(Throwable cause, Contextual contextual, Snippetable source, Snippetable indicated, String message, @Nullable String note) {
        super(cause, new PandaIndicatedSource(source, indicated), message, note);
        this.context = contextual.toContext();
    }

    @Override
    public Context getContext() {
        return context;
    }

}
