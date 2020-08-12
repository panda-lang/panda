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

package org.panda_lang.language.interpreter.token;

/**
 * Represents objects that can be reduced to the {@link org.panda_lang.language.interpreter.token.Snippet}
 */
public interface Snippetable extends Streamable {

    /**
     * Converts current object into the {@link Snippet}
     *
     * @return a new snippet containing content of the current object
     */
    Snippet toSnippet();

    @Override
    default SourceStream toStream() {
        return new PandaSourceStream(toSnippet());
    }

}
