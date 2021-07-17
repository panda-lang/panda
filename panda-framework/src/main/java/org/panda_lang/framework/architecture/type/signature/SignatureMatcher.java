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

package org.panda_lang.framework.architecture.type.signature;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.architecture.type.member.ParametrizedMember;
import org.panda_lang.framework.architecture.type.member.parameter.PropertyParameter;
import panda.std.Option;
import panda.std.Result;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class SignatureMatcher<T extends ParametrizedMember> {

    public Option<AdjustedMember<T>> match(@Nullable Signature parent, Collection<? extends T> collection, Signature[] requiredTypes, @Nullable List<Expression> arguments) {
        Signature[] required = Arrays.stream(requiredTypes)
                .toArray(Signature[]::new);

        for (T executable : collection) {
            Result<AdjustedMember<T>, String> result = match(parent, executable, required, arguments);

            if (result.isOk()) {
                return Option.of(result.get());
            }
        }

        return Option.none();
    }

    private Result<AdjustedMember<T>, String> match(@Nullable Signature parent, T executable, Signature[] requiredTypes, @Nullable List<Expression> arguments) {
        List<? extends PropertyParameter> parameters = executable.getParameters();
        //Result<? extends Signature, String> resultSignature = parent.merge(executable.getReturnType());
        Result<? extends Signature, String> resultSignature = Result.error("Not implemented");

        if (resultSignature.isErr()) {
            return Result.error(resultSignature.getError());
        }

        // return result for parameterless executables
        if (parameters.size() == 0) {
            return requiredTypes.length == 0
                    ? Result.ok(new AdjustedMember<>(resultSignature.get(), executable, arguments))
                    : Result.error("Invalid amount of parameters");
        }

        // map arguments into parameters
        int index = 0, required = 0;

        // loop as long parameters and types are available
        for (; (index < parameters.size()) && (required < requiredTypes.length); index++) {
            PropertyParameter parameter = parameters.get(index);
            Signature requiredType = requiredTypes[required++];

            if (!parameter.getSignature().isAssignableFrom(requiredType)) {
                return Result.error(parameter + " is not assignable from " + requiredType);
            }
        }

        // return if does not match
        // TODO: Probably deprecated due to lack of varargs
        if (index != parameters.size() || required != requiredTypes.length) {
            return Result.error("Unmatched amount of parameters");
        }

        return Result.ok(new AdjustedMember<>(resultSignature.get(), executable, arguments));
    }

}
