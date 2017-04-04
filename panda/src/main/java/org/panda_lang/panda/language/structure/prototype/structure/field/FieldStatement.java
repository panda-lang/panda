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

package org.panda_lang.panda.language.structure.prototype.structure.field;

import org.panda_lang.panda.framework.language.structure.Statement;

public class FieldStatement implements Statement {

    private final Field field;
    private final int wrapperID;
    private final int fieldID;

    public FieldStatement(int wrapperID, int fieldID, Field field) {
        this.field = field;
        this.wrapperID = wrapperID;
        this.fieldID = fieldID;
    }

    public int getFieldID() {
        return fieldID;
    }

    public int getWrapperID() {
        return wrapperID;
    }

    public Field getField() {
        return field;
    }

}
