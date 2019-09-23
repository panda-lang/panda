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

package org.panda_lang.framework.language.interpreter.parser;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.ParserFailure;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.language.interpreter.PandaInterpreterFailure;
import org.panda_lang.framework.language.interpreter.source.PandaIndicatedSource;

public class PandaParserFailure extends PandaInterpreterFailure implements ParserFailure {

    private final Context context;

    public PandaParserFailure(Context context, Snippetable source, Snippetable indicated, String message, @Nullable String note) {
        super(new PandaIndicatedSource(source, indicated), message, note);
        this.context = context;
    }

    public PandaParserFailure(Context context, Snippetable indicated, String message, @Nullable String note) {
        this(context, context.getComponent(Components.CURRENT_SOURCE), indicated, message, note);
    }

    public PandaParserFailure(Context context, Snippetable indicated, String message) {
        this(context, indicated, message, null);
    }

    public PandaParserFailure(Context context, String message, @Nullable String note) {
        this(context, context.getComponent(Components.CURRENT_SOURCE), message, note);
    }

    public PandaParserFailure(Context context, String message) {
        this(context, message, null);
    }

    @Override
    public Context getContext() {
        return context;
    }

}
