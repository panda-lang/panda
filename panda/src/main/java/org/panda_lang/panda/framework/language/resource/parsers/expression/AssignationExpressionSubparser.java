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

package org.panda_lang.panda.framework.language.resource.parsers.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

public class AssignationExpressionSubparser implements ExpressionSubparser {

    @Override
    public @Nullable Tokens read(ExpressionParser parent, Tokens source) {
        // read var/field/array/etc. from subparsers
        // =
        // expression

        /*
        ExtractorResult result = pattern.extract(source);

        if (!result.isMatched()) {
            return null;
        }

        Tokens declaration = result.getWildcard("*declaration");
        SourceStream stream = new PandaSourceStream(declaration);

        AssignationSubparser subparser = data
                .getComponent(UniversalComponents.PIPELINE)
                .getPipeline(PandaPipelines.ASSIGNER)
                .handleWithUpdatedSource(data, stream);

        if (subparser == null) {
            return null;
        }
        */

        return null;
        //return !stream.hasUnreadSource();
    }

    @Override
    public @Nullable Expression parse(ExpressionParser parent, ParserData data, Tokens source) {
        return null;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.ASSIGNATION;
    }

}
