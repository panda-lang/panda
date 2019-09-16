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

package org.panda_lang.panda.language.resource.scope.branching;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.Controller;
import org.panda_lang.panda.framework.design.runtime.ProcessStack;
import org.panda_lang.panda.framework.design.runtime.Status;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.architecture.dynamic.AbstractExecutableStatement;
import org.panda_lang.panda.utilities.commons.UnsafeUtils;

final class Throw extends AbstractExecutableStatement implements Controller {

    private final Expression value;

    public Throw(Expression value) {
        this.value = value;
    }

    @Override
    public @Nullable Object execute(ProcessStack stack, Object instance) {
        UnsafeUtils.getUnsafe().throwException(value.evaluate(stack, instance));
        return null;
    }

    @Override
    public byte getStatus() {
        return Status.THROW;
    }

}
