/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.utilities.commons;

public final class ThrowableUtils {

    private ThrowableUtils() { }

    /**
     * Throw any throwable without required signatures
     *
     * @param e throwable to throw
     * @param <E> generics trick
     * @throws E specified throwable
     */
    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void throwAny(Throwable e) throws E {
        throw (E) e;
    }

}
