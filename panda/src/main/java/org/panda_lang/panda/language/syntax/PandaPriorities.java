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

package org.panda_lang.panda.language.syntax;

public final class PandaPriorities {

    /**
     * Used by {@link org.panda_lang.panda.language.syntax.type.MethodParser}
     */
    public static final int PROTOTYPE_METHOD = 1;

    /**
     * Used by {@link org.panda_lang.panda.language.syntax.type.FieldParser}
     */
    public static final int PROTOTYPE_FIELD = 2;

    /**
     * Used by {@link org.panda_lang.panda.language.interpreter.parser.block.BlockParser}
     */
    public static final int SCOPE_BLOCK = 1;

    /**
     * Used by {@link org.panda_lang.panda.language.syntax.scope.LateDeclarationParser}
     */
    public static final int SCOPE_LATE_DECLARATION = 4;

    /**
     * Used by {@link org.panda_lang.panda.language.syntax.scope.StandaloneExpressionParser}
     */
    public static final int SCOPE_EXPRESSION = 5;

    private PandaPriorities() { }

}
