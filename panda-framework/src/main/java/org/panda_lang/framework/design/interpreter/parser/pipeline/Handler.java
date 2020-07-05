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

package org.panda_lang.framework.design.interpreter.parser.pipeline;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.LocalChannel;
import org.panda_lang.framework.design.interpreter.token.Snippet;

/**
 * Checkout the requested source
 */
public interface Handler {

    /**
     * Handle source, possible results:
     *
     * <ul>
     *     <li>InterpreterFailure - that will be stored and printed if there is no matching handler</li>
     *     <li>Exception - that will interrupt pipeline</li>
     *     <li>boolean - true/false if matched or not</li>
     *     <li>other/null - illegal return value</li>
     * </ul>
     *
     * @param source source
     * @return the result object
     */
    @Nullable Object handle(Context context, LocalChannel channel, Snippet source);

}
