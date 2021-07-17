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

package panda.interpreter.architecture.type.signature;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.type.member.ParametrizedMember;

import java.util.List;

/**
 * Container for arguments adjusted to the property signature
 *
 * @param <T> generic type of property
 */
public class AdjustedMember<T extends ParametrizedMember> implements Signed {

    private final Signature signature;
    private final T executable;
    private final List<Expression> arguments;

    public AdjustedMember(Signature signature, T executable, List<Expression> arguments) {
        this.signature = signature;
        this.executable = executable;
        this.arguments = arguments;
    }

    /**
     * Get adjusted arguments
     *
     * @return the array of arguments
     */
    public List<Expression> getArguments() {
        return arguments;
    }

    /**
     * Get associated parametrized executable
     *
     * @return the executable
     */
    public T getExecutable() {
        return executable;
    }

    @Override
    public Signature getSignature() {
        return signature;
    }

}
