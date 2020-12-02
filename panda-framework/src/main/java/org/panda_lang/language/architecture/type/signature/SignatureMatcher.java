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

package org.panda_lang.language.architecture.type.signature;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.type.Adjustment;
import org.panda_lang.language.architecture.type.member.Member;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.utilities.commons.function.Option;

import java.util.Arrays;
import java.util.Collection;

public final class SignatureMatcher<T extends Member> {

    public Option<Adjustment<T>> match(Collection<? extends T> collection, Signature[] requiredTypes, @Nullable Expression[] arguments) {
        Signature[] required = Arrays.stream(requiredTypes)
                .toArray(Signature[]::new);

        for (T executable : collection) {
            Adjustment<T> args = match(executable, required, arguments);

            if (args != null) {
                return Option.of(args);
            }
        }

        return Option.none();
    }

    private @Nullable Adjustment<T> match(T executable, Signature[] requiredTypes, @Nullable Expression[] arguments) {
        PropertyParameter[] parameters = executable.getParameters();

        // return result for parameterless executables
        if (parameters.length == 0) {
            return requiredTypes.length == 0 ? new Adjustment<>(executable, arguments) : null;
        }

        // map arguments into parameters
        int index = 0, required = 0;

        // loop as long parameters and types are available
        for (; (index < parameters.length) && (required < requiredTypes.length); index++) {
            PropertyParameter parameter = parameters[index];

            if (!parameter.getSignature().isAssignableFrom(requiredTypes[required++])) {
                return null;
            }
        }

        // return if does not match
        if (index != parameters.length || required != requiredTypes.length) {
            return null;
        }

        // return executable if only types was requested
        if (arguments == null) {
            return new Adjustment<>(executable, null);
        }

        return new Adjustment<>(executable, arguments);
    }

}
