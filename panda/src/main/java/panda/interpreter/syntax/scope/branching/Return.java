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

package panda.interpreter.syntax.scope.branching;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.dynamic.Controller;
import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.source.Location;
import panda.interpreter.runtime.ProcessStack;
import panda.interpreter.runtime.Status;
import panda.interpreter.architecture.dynamic.AbstractExecutableStatement;

public final class Return extends AbstractExecutableStatement implements Returnable, Controller {

    private final Expression value;

    public Return(Location location, Expression value) {
        super(location);
        this.value = value;
    }

    @Override
    public @Nullable Object execute(ProcessStack stack, Object instance) throws Exception {
        return hasReturnValue() ? value.evaluate(stack, instance) : null;
    }

    public boolean hasReturnValue() {
        return value != null;
    }

    @Override
    public byte getStatusCode() {
        return Status.RETURN;
    }

}
