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

package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;

public abstract class BootstrapParser implements UnifiedParser, ParserHandler {

    protected ParserRepresentation bootstrapParser;

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        return bootstrapParser.getParser().parse(data, nextLayer);
    }

    @Override
    public boolean handle(TokenReader reader) {
        return bootstrapParser.getHandler().handle(reader);
    }

}
