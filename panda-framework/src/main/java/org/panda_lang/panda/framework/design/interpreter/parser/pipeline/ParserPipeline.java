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

package org.panda_lang.panda.framework.design.interpreter.parser.pipeline;

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;

import java.util.Collection;

public interface ParserPipeline<P extends Parser> {

    /**
     * Search for the parser through the pipeline with untouched source
     *
     * @param source the source
     * @return parser which fits to the source
     */
    P handle(Context context, Snippet source);

    /**
     * @param parserRepresentation specified parser representation which will be registered in the pipeline
     */
    void registerParserRepresentation(ParserRepresentation<P> parserRepresentation);

    /**
     * @return a collection of registered parser
     */
    Collection<? extends ParserRepresentation<P>> getRepresentations();

    /**
     * @return total handle nano time
     */
    long getHandleTime();

}
