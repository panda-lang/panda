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

package org.panda_lang.framework.interpreter.lexer;

import org.panda_lang.framework.resource.Syntax;

public final class PandaLexerConfiguration {

    protected Syntax syntax;
    protected boolean ignoringCase;
    protected boolean enabledSections;
    protected boolean ignoringWhitespaces;
    protected boolean includingIndentation;

    protected PandaLexerConfiguration() { }

    /**
     * Syntax scheme used by lexer
     *
     * @param syntax the syntax to use
     * @return configuration instance
     */
    public PandaLexerConfiguration withSyntax(Syntax syntax) {
        this.syntax = syntax;
        return this;
    }

    /**
     * Enable wrapping content between separator pairs into section tokens
     *
     * @return configuration instance
     */
    public PandaLexerConfiguration enableSections() {
        this.enabledSections = true;
        return this;
    }

    /**
     * Enable ignoring case of tokens, e.g. SeLeCt, SELECT, select will be matched as the same token
     *
     * @return configuration instance
     */
    public PandaLexerConfiguration enableIgnoringCaseOfTokens() {
        this.ignoringCase = true;
        return this;
    }

    /**
     * Enable including indentation into the {@link org.panda_lang.framework.resource.syntax.auxiliary.Indentation} token
     *
     * @return configuration instance
     */
    public PandaLexerConfiguration includeIndentation() {
        this.includingIndentation = true;
        return this;
    }

    /**
     * Enable ignoring whitespaces
     *
     * @return configuration instance
     */
    public PandaLexerConfiguration ignoreWhitespaces() {
        this.ignoringWhitespaces = true;
        return this;
    }

    /**
     * Build lexer based on provided options
     *
     * @return the lexer instance
     */
    public PandaLexer build() {
        return new PandaLexer(this);
    }

}
