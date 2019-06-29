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

import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserFailure;
import org.panda_lang.panda.framework.design.interpreter.source.SourceFragment;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippetable;
import org.panda_lang.panda.framework.language.interpreter.PandaInterpreterFailure;
import org.panda_lang.panda.framework.language.interpreter.source.PandaSourceFragmentCreator;

public class PandaParserFailure extends PandaInterpreterFailure implements ParserFailure {

    private final Context context;

    protected PandaParserFailure(PandaParserFailureBuilder builder) {
        super(builder.message, builder.fragment, builder.note);
        this.context = builder.context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public static PandaParserFailureBuilder builder(String message, Context context) {
        return new PandaParserFailureBuilder(message, context);
    }

    public static class PandaParserFailureBuilder {

        private final String message;
        private final Context context;
        private SourceFragment fragment;
        private String note;

        private PandaParserFailureBuilder(String message, Context context) {
            this.message = message;
            this.context = context;
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
                    .ofStreamOrigin(context, indicated)
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
