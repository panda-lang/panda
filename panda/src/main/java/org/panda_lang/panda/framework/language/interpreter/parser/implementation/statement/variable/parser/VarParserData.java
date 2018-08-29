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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.parser;

import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;

import java.util.List;

public class VarParserData {

    private final StatementCell cell;
    private final List<TokenizedSource> hollows;
    private final boolean assignation;

    public VarParserData(StatementCell cell, List<TokenizedSource> hollows, boolean assignation) {
        this.cell = cell;
        this.hollows = hollows;
        this.assignation = assignation;
    }

    public boolean hasAssignation() {
        return assignation;
    }

    public List<TokenizedSource> getHollows() {
        return hollows;
    }

    public StatementCell getCell() {
        return cell;
    }

}
