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

package org.panda_lang.language.architecture.type.member.constructor;

import org.panda_lang.language.architecture.type.TypeInstance;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.architecture.statement.AbstractPropertyFramedScope;
import org.panda_lang.utilities.commons.collection.Lists;

import java.util.List;

public final class ConstructorScope extends AbstractPropertyFramedScope {

    public ConstructorScope(Location location, List<PropertyParameter> parameters) {
        super(location, parameters);
    }

    @Override
    public ConstructorFrame revive(ProcessStack stack, Object instance) {
        return new ConstructorFrame(this, (TypeInstance) instance);
    }

    public Option<BaseCall> getBaseCall() {
        return Option.of(this)
                .map(scope -> Lists.get(scope.getStatements(), 0))
                .filter(statement -> statement instanceof BaseCall)
                .map(statement -> ((BaseCall) statement));
    }

    public List<PropertyParameter> getParameters() {
        return parameters;
    }

}
