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

package org.panda_lang.panda.framework.language.interpreter.source;

import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.source.SourceFragment;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippetable;

public final class PandaSourceFragmentUtils {

    public static SourceFragment ofOriginal(ParserData data, Snippetable indicated) {
        return of(data.getComponent(UniversalComponents.SOURCE), indicated);
    }

    public static SourceFragment ofStream(ParserData data) {
        return ofStream(data, data.getComponent(UniversalComponents.SOURCE_STREAM));
    }

    public static SourceFragment ofStream(ParserData data, Snippetable indicated) {
        return of(data.getComponent(UniversalComponents.SOURCE_STREAM), indicated);
    }

    public static SourceFragment ofStreamOrigin(ParserData data, Snippetable indicated) {
        return of(data.getComponent(UniversalComponents.SOURCE_STREAM).getOriginalSource(), indicated);
    }

    public static SourceFragment of(Snippetable source, Snippetable indicated) {
        Snippet sourceSnippet = source.toSnippet();

        if (sourceSnippet.isEmpty()) {
            throw new PandaFrameworkException("Source snippet cannot be empty");
        }

        Snippet indicatedSource = indicated.toSnippet();

        if (indicatedSource.isEmpty()) {
            throw new PandaFrameworkException("Indicated snippet cannot be empty");
        }

        return new PandaSourceFragment(indicatedSource.getFirst().getLocation(), sourceSnippet, indicatedSource);
    }

}
