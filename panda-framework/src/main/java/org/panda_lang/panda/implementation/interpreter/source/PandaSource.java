/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.implementation.interpreter.source;

import org.panda_lang.panda.framework.interpreter.source.Source;
import org.panda_lang.panda.framework.interpreter.source.CodeSource;

public class PandaSource implements Source {

    private final String title;
    private final String content;

    public PandaSource(CodeSource codeSource) {
        this(codeSource.getLocation().getPath(), codeSource.getContent());
    }

    public PandaSource(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

}
