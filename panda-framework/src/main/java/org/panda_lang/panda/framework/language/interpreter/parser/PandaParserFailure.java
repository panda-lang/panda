/*
 * Copyright (c) 2015-2018 Dzikoysk
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
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

public class PandaParserFailure extends ParserFailure {

    private final ParserData data;
    private final String message;
    private final String details;

    private final int currentLine;
    private final String element;

    public PandaParserFailure(String message, ParserData data) {
        this(message, null, data);
    }

    public PandaParserFailure(String message, String details, ParserData data) {
        this(builder().message(message).details(details).data(data));
    }

    private PandaParserFailure(PandaParserFailureBuilder builder) {
        super(builder.message);

        this.message = builder.message;
        this.details = builder.details;
        this.data = builder.data.fork();

        SourceStream source = builder.source != null ? builder.source : this.data.getComponent(UniversalComponents.SOURCE_STREAM);
        source.restoreCachedSource();

        this.currentLine = source.getCurrentLine();
        this.element = source.readLineResidue().toString();
    }

    @Override
    public ParserData getData() {
        return data;
    }

    @Override
    public String getElement() {
        return element;
    }

    @Override
    public String getSource() {
        return data.getComponent(UniversalComponents.SOURCE).selectLine(this.getLine()).toString();
    }

    @Override
    public String getLocation() {
        return data.getComponent(UniversalComponents.SCRIPT).getScriptName();
    }

    @Override
    public int getLine() {
        return currentLine;
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static PandaParserFailureBuilder builder() {
        return new PandaParserFailureBuilder();
    }

    public static class PandaParserFailureBuilder {

        private String message;
        private String details;
        private ParserData data;
        private SourceStream source;

        public PandaParserFailureBuilder message(String message) {
            this.message = message;
            return this;
        }

        public PandaParserFailureBuilder details(String details) {
            this.details = details;
            return this;
        }

        public PandaParserFailureBuilder data(ParserData data) {
            this.data = data;
            return this;
        }

        public PandaParserFailureBuilder source(TokenizedSource source) {
            this.source = new PandaSourceStream(source);
            return this;
        }

        public PandaParserFailureBuilder source(SourceStream source) {
            this.source = source;
            return this;
        }

        public PandaParserFailure build() {
            return new PandaParserFailure(this);
        }

    }

}
