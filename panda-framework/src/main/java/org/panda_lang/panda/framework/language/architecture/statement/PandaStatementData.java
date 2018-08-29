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

package org.panda_lang.panda.framework.language.architecture.statement;

import org.panda_lang.panda.framework.design.architecture.statement.StatementData;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;

public class PandaStatementData implements StatementData {

    protected final int sourceLine;

    public PandaStatementData(int sourceLine) {
        this.sourceLine = sourceLine;
    }

    @Override
    public int getSourceLine() {
        return sourceLine;
    }

    public static PandaStatementData of(SourceStream source) {
        return new PandaStatementData(source.read().getLine());
    }

}
