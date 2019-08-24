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

package org.panda_lang.panda.framework.language.architecture.prototype.standard.structure;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.language.architecture.statement.AbstractStatement;

public class ClassPrototypeReferenceStatement extends AbstractStatement {

    private final ClassPrototype classPrototype;
    private final ClassPrototypeFrame classScope;

    public ClassPrototypeReferenceStatement(ClassPrototype classPrototype, ClassPrototypeFrame classScope) {
        this.classPrototype = classPrototype;
        this.classScope = classScope;
    }

    public ClassPrototypeFrame getClassScope() {
        return classScope;
    }

    public ClassPrototype getClassPrototype() {
        return classPrototype;
    }

    @Override
    public String toString() {
        return "'class-reference': '" + classPrototype.getName() + "': { " + classPrototype + " }";
    }

}
