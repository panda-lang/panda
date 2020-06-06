/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.source;

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.interpreter.source.IndicatedSource;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Snippetable;

public final class PandaIndicatedSource implements IndicatedSource {

    private final Snippet source;
    private final Snippet indicated;

    public PandaIndicatedSource(Snippetable source, Snippetable indicated) {
        this.source = source.toSnippet();

        if (this.source.isEmpty()) {
            throw new PandaFrameworkException("Source snippet cannot be empty");
        }

        this.indicated = indicated.toSnippet();

        if (this.indicated.isEmpty()) {
            throw new PandaFrameworkException("Indicated snippet cannot be empty");
        }
    }

    @Override
    public Snippet getIndicated() {
        return indicated;
    }

    @Override
    public Snippet getSource() {
        return source;
    }

    @Override
    public String toString() {
        return getIndicated().equals(getSource()) ? getIndicated().toString() : getIndicated() + " at " + getSource();
    }

}
