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

package org.panda_lang.panda.implementation.element.struct;

import org.panda_lang.framework.structure.Statement;
import org.panda_lang.framework.structure.StatementCell;
import org.panda_lang.framework.structure.Wrapper;
import org.panda_lang.framework.structure.WrapperInstance;

import java.util.List;

public class ClassWrapper implements Wrapper {

    @Override
    public WrapperInstance createInstance() {
        return null;
    }

    @Override
    public StatementCell addStatement(Statement statement) {
        return null;
    }

    @Override
    public List<StatementCell> getStatementCells() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getID() {
        return 0;
    }

}
