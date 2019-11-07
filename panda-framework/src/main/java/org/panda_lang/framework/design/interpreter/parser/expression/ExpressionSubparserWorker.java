/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.framework.design.interpreter.parser.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;

/**
 * Subparser worker, created during the parse process
 */
public interface ExpressionSubparserWorker {

    /**
     * Handle a next token. Possible results:
     *
     * <ul>
     *     <li>null if current worker does not match the source</li>
     *     <li>empty result if worker matches the source, but still needs more data</li>
     *     <li>result if worker matched source</li>
     *     <li>error if worker matched source, but something happen</li>
     * </ul>
     *
     * @param context the current context
     * @param token the token to handle
     * @return the result of worker
     */
    @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token);

    /**
     * Get parent subparser
     *
     * @return the subparser
     */
    ExpressionSubparser getSubparser();

}
