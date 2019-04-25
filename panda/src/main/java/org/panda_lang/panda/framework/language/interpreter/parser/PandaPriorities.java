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

package org.panda_lang.panda.framework.language.interpreter.parser;

public class PandaPriorities {

    /**
     * Used by {@link org.panda_lang.panda.framework.language.resource.parsers.prototype.method.MethodParser}
     */
    public static final int PROTOTYPE_METHOD = 1;

    /**
     * Used by {@link org.panda_lang.panda.framework.language.resource.parsers.prototype.field.FieldParser}
     */
    public static final int PROTOTYPE_FIELD = 2;



    /**
     * Used by {@link org.panda_lang.panda.framework.language.resource.parsers.scope.block.BlockParser}
     */
    public static final int SCOPE_BLOCK = 1;

    /**
     * Used by {@link org.panda_lang.panda.framework.language.resource.parsers.expression.invoker.MethodInvokerParser}
     */
    public static final int SCOPE_METHOD_INVOKER = 2;

    /**
     * Used by {@link org.panda_lang.panda.framework.language.resource.parsers.expression.assignation.AssignationParser}
     */
    public static final int SCOPE_ASSIGNATION = 3;

    /**
     * Used by {@link org.panda_lang.panda.framework.language.resource.parsers.scope.DeclarationParser}
     */
    public static final int SCOPE_DECLARATION = 4;

    /**
     * Used by {@link org.panda_lang.panda.framework.language.resource.parsers.scope.StandaloneExpressionParser}
     */
    public static final int SCOPE_EXPRESSION = 5;

}
