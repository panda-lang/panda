/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.scope.branching;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.dynamic.Controller;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.runtime.Status;
import org.panda_lang.language.architecture.dynamic.AbstractExecutableStatement;

final class Break extends AbstractExecutableStatement implements Controller {

    Break(Location location) {
        super(location);
    }

    @Override
    public @Nullable Object execute(ProcessStack stack, Object instance) {
        return null;
    }

    @Override
    public byte getStatusCode() {
        return Status.BREAK;
    }

}
