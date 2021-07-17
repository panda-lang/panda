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

package panda.interpreter.lexer;

import panda.interpreter.source.Source;
import panda.interpreter.token.Snippet;
import panda.interpreter.resource.Syntax;

public final class PandaLexer implements Lexer {

    private final PandaLexerConfiguration configuration;

    protected PandaLexer(PandaLexerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Snippet convert(Source source) {
        PandaLexerWorker worker = new PandaLexerWorker(this, source);
        return worker.convert();
    }

    public PandaLexerConfiguration getConfiguration() {
        return configuration;
    }

    public static PandaLexerConfiguration of(Syntax syntax) {
        return new PandaLexerConfiguration().withSyntax(syntax);
    }

    public static PandaLexerConfiguration builder() {
        return new PandaLexerConfiguration();
    }

}
