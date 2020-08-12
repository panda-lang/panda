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

package org.panda_lang.language.architecture.type;

import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.type.utils.TypedUtils;

final class PandaConstructors extends AbstractProperties<TypeConstructor> implements Constructors {

    private static final TypeExecutablePropertiesMatcher<TypeConstructor> MATCHER = new TypeExecutablePropertiesMatcher<>();

    PandaConstructors(Type type) {
        super(TypeConstructor.class, type);
    }

    @Override
    public Option<TypeConstructor> getConstructor(Type[] types) {
        return MATCHER.match(getDeclaredProperties(), types, null).map(Adjustment::getExecutable);
    }

    @Override
    public Option<Adjustment<TypeConstructor>> getAdjustedConstructor(Expression[] arguments) {
        return MATCHER.match(getDeclaredProperties(), TypedUtils.toTypes(arguments), arguments);
    }

}
