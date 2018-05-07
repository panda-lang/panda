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

import org.panda_lang.panda.framework.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;

public class PandaParserFailure extends PandaFrameworkException implements ParserFailure {

    private final ParserData data;
    private final String message;
    private final String details;

    public PandaParserFailure(String message, String details, ParserData data) {
        super(message);

        this.message = message;
        this.details = details;
        this.data = data.fork();
    }

    public PandaParserFailure(String message, ParserData data) {
        this(message, null, data);
    }

    @Override
    public ParserData getData() {
        return data;
    }

    @Override
    public String getElement() {
        return data.getComponent(UniversalComponents.SOURCE_STREAM).read().getTokenValue();
    }

    @Override
    public String getSource() {
        return data.getComponent(UniversalComponents.SOURCE).selectLine(this.getLine()).asString();
    }

    @Override
    public String getLocation() {
        return data.getComponent(UniversalComponents.SCRIPT).getScriptName();
    }

    @Override
    public int getLine() {
        return data.getComponent(UniversalComponents.SOURCE_STREAM).getCurrentLine();
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
