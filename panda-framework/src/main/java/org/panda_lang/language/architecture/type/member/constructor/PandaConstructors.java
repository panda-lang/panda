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

import org.panda_lang.language.architecture.type.Adjustment;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.SignatureMatcher;
import org.panda_lang.language.architecture.type.member.AbstractProperties;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.type.TypedUtils;

public final class PandaConstructors extends AbstractProperties<TypeConstructor> implements Constructors {

    private static final SignatureMatcher<TypeConstructor> MATCHER = new SignatureMatcher<>();

    public PandaConstructors(Type type) {
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
