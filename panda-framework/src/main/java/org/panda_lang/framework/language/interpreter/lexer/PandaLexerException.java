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

package org.panda_lang.framework.language.interpreter.lexer;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.language.interpreter.PandaInterpreterFailure;
import org.panda_lang.framework.language.interpreter.source.PandaSourceFragmentCreator;
import org.panda_lang.framework.language.interpreter.token.PandaToken;
import org.panda_lang.framework.language.interpreter.token.PandaTokenRepresentation;

public class PandaLexerException extends PandaInterpreterFailure {

    public PandaLexerException(String message, Snippetable token, Snippetable line, SourceLocation location, @Nullable String note) {
        super(message, new PandaSourceFragmentCreator<>(fragment -> fragment).of(line, token).create(), note);
    }

    private PandaLexerException(PandaLexerExceptionBuilder builder) {
        this(builder.message, builder.token, builder.line, builder.location, builder.note);
    }

    public static PandaLexerExceptionBuilder builder(String message) {
        return new PandaLexerExceptionBuilder(message);
    }

    static class PandaLexerExceptionBuilder {

        private final String message;
        private Snippetable token;
        private Snippetable line;
        private SourceLocation location;
        private String note;

        PandaLexerExceptionBuilder(String message) {
            this.message = message;
        }

        protected PandaLexerExceptionBuilder withNote(String note) {
            this.note = note;
            return this;
        }

        protected PandaLexerExceptionBuilder withLocation(SourceLocation location) {
            this.location = location;
            return this;
        }

        protected PandaLexerExceptionBuilder withToken(String token) {
            this.token = toSnippetable(token);
            return this;
        }

        protected PandaLexerExceptionBuilder withLine(String line) {
            this.line = toSnippetable(line);
            return this;
        }

        private Snippetable toSnippetable(String content) {
            return new PandaTokenRepresentation(new PandaToken(TokenType.UNKNOWN, content), location);
        }

        protected PandaLexerException build() {
            return new PandaLexerException(this);
        }

    }

}
