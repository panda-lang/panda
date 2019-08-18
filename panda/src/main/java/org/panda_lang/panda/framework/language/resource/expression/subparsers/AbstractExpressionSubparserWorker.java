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

package org.panda_lang.panda.framework.language.resource.expression.subparsers;

import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;

abstract class AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

    private ExpressionSubparserRepresentation subparser;

    public ExpressionSubparserWorker withSubparser(ExpressionSubparserRepresentation subparser) {
        this.subparser = subparser;
        return this;
    }

    @Override
    public ExpressionSubparserRepresentation getSubparserRepresentation() {
        return subparser;
    }

}
