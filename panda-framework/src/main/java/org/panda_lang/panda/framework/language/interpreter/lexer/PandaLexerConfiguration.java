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

package org.panda_lang.panda.framework.language.interpreter.lexer;

import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.design.resource.Syntax;

public class PandaLexerConfiguration {

    private Source source;
    private Syntax syntax;
    private boolean ignoringCase;
    private boolean includedIndentation;
    private boolean respectingWhitespaces = true;

    public PandaLexerConfiguration(Syntax syntax, Source source) {
        this.syntax = syntax;
        this.source = source;
    }

    public PandaLexerConfiguration() {
        this(null, null);
    }

    public PandaLexer build() {
        return new PandaLexer(this);
    }

    public PandaLexerConfiguration equalsIgnoreCase(boolean flag) {
        this.ignoringCase = flag;
        return this;
    }

    public PandaLexerConfiguration includeIndentation(boolean flag) {
        this.includedIndentation = flag;
        return this;
    }

    public PandaLexerConfiguration respectWhitespaces(boolean flag) {
        this.respectingWhitespaces = flag;
        return this;
    }

    public boolean hasIncludedIndentation() {
        return includedIndentation;
    }

    public boolean isRespectingWhitespaces() {
        return respectingWhitespaces;
    }

    protected boolean isIgnoringCase() {
        return ignoringCase;
    }

    protected Syntax getSyntax() {
        return syntax;
    }

    public Source getSource() {
        return source;
    }

}
