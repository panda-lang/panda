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

package org.panda_lang.language.architecture.type.member.method;

import org.panda_lang.language.architecture.type.Adjustment;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.architecture.type.State;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.signature.SignatureMatcher;
import org.panda_lang.language.architecture.type.member.AbstractMembers;
import org.panda_lang.language.architecture.type.member.parameter.ParameterUtils;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.language.architecture.expression.Expression;

import java.util.List;

public final class PandaMethods extends AbstractMembers<TypeMethod> implements Methods {

    private static final SignatureMatcher<TypeMethod> MATCHER = new SignatureMatcher<>();

    public PandaMethods(Type type) {
        super(TypeMethod.class, type);
    }

    @Override
    public Option<TypeMethod> getMethod(String name, Signature[] parameters) {
        return MATCHER.match(getPropertiesLike(name), parameters, null).map(Adjustment::getExecutable);
    }

    @Override
    public Option<Adjustment<TypeMethod>> getAdjustedArguments(String name, Expression[] arguments) {
        return MATCHER.match(getPropertiesLike(name), ParameterUtils.toTypes(arguments), arguments);
    }

    @Override
    public List<? extends TypeMethod> getPropertiesLike(String name) {
        return super.getPropertiesLike(name, method -> true);
    }

    @Override
    public List<? extends TypeMethod> getProperties() {
        return super.getProperties(method -> super.type.getState() == State.ABSTRACT || !method.isAbstract());
    }

    @Override
    public List<? extends TypeMethod> getDeclaredProperties() {
        return super.getDeclaredProperties(method -> super.type.getState() == State.ABSTRACT || !method.isAbstract());
    }

    @Override
    public String toString() {
        return "Methods[" + size() + "]";
    }

}
