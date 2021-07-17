/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.language.syntax.scope.branching;

import org.panda_lang.framework.architecture.dynamic.Controller;
import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.interpreter.source.Location;
import org.panda_lang.framework.runtime.ProcessStack;
import org.panda_lang.framework.runtime.Status;
import org.panda_lang.framework.architecture.dynamic.AbstractExecutableStatement;
import panda.utilities.UnsafeUtils;

final class Throw extends AbstractExecutableStatement implements Returnable, Controller {

    private final Expression value;

    public Throw(Location location, Expression value) {
        super(location);
        this.value = value;
    }

    @Override
    public Object execute(ProcessStack stack, Object instance) throws Exception {
        UnsafeUtils.throwException(value.evaluate(stack, instance));
        return null;
    }

    @Override
    public byte getStatusCode() {
        return Status.THROW;
    }

}
