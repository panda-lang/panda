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

package org.panda_lang.panda.framework.language.interpreter.parser;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserFailure;
import org.panda_lang.panda.framework.design.interpreter.source.SourceFragment;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippetable;
import org.panda_lang.panda.framework.language.interpreter.PandaInterpreterFailure;
import org.panda_lang.panda.framework.language.interpreter.source.PandaSourceFragmentCreator;

public class PandaParserFailure extends PandaInterpreterFailure implements ParserFailure {

    private final ParserData data;

    protected PandaParserFailure(PandaParserFailureBuilder builder) {
        super(builder.message, builder.fragment, builder.note);
        this.data = builder.data;
    }

    @Override
    public ParserData getData() {
        return data;
    }

    public static PandaParserFailureBuilder builder(String message, ParserData data) {
        return new PandaParserFailureBuilder(message, data);
    }

    public static class PandaParserFailureBuilder {

        private final String message;
        private final ParserData data;
        private SourceFragment fragment;
        private String note;

        private PandaParserFailureBuilder(String message, ParserData data) {
            this.message = message;
            this.data = data;
        }

        public PandaParserFailureBuilder withSourceFragment(SourceFragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public PandaParserFailureBuilder withSource(Snippetable source, Snippetable indicated) {
            return withSourceFragment()
                    .of(source, indicated)
                    .create();
        }

        public PandaParserFailureBuilder withStreamOrigin(Snippetable indicated) {
            return withSourceFragment()
                    .ofStreamOrigin(data, indicated)
                    .create();
        }

        public PandaSourceFragmentCreator<PandaParserFailureBuilder> withSourceFragment() {
            return new PandaSourceFragmentCreator<>(source -> {
                this.fragment = source;
                return this;
            });
        }

        public PandaParserFailureBuilder withNote(String note) {
            this.note = note;
            return this;
        }

        public PandaParserFailure build() {
            return new PandaParserFailure(this);
        }

    }

}
