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

import org.panda_lang.panda.framework.design.interpreter.parser.ParserDebug;

public class PandaParserDebug implements ParserDebug {

    private boolean tailing;

    public PandaParserDebug(boolean tailing) {
        this.tailing = tailing;
    }

    public PandaParserDebug enableTailing(boolean flag) {
        this.tailing = flag;
        return this;
    }

    @Override
    public boolean isTailingEnabled() {
        return tailing;
    }

}
