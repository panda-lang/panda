/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.lexer;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.language.interpreter.PandaInterpreterFailure;
import org.panda_lang.framework.language.interpreter.source.PandaIndicatedSource;
import org.panda_lang.framework.language.interpreter.token.PandaToken;
import org.panda_lang.framework.language.interpreter.token.PandaTokenRepresentation;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;

public final class PandaLexerFailure extends PandaInterpreterFailure {

    public PandaLexerFailure(Snippetable line, Snippetable indicated, String message, @Nullable String note) {
        super(new PandaIndicatedSource(line, indicated), message, note);
    }

    public PandaLexerFailure(CharSequence line, CharSequence indicated, SourceLocation location, String message, @Nullable String note) {
        this(of(line, location), of(indicated, location), message, note);
    }

    private static Snippetable of(CharSequence content, SourceLocation location) {
        return new PandaTokenRepresentation(new PandaToken(TokenTypes.UNKNOWN, content.toString()), location);
    }

}
