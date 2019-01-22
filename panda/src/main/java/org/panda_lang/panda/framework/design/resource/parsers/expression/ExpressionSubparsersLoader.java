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

package org.panda_lang.panda.framework.design.resource.parsers.expression;

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.util.PandaUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExpressionSubparsersLoader {

    public ExpressionSubparsers load(ParserData data) throws Exception {
        PandaFramework.getLogger().debug("");
        PandaFramework.getLogger().debug("--- Loading expressions");

        Collection<Class<? extends ExpressionSubparser>> subparserClasses = PandaUtils.DEFAULT_PANDA_SCANNER
                .createSelector()
                .selectSubtypesOf(ExpressionSubparser.class);

        return new ExpressionSubparsers(load(subparserClasses, data));
    }

    private List<ExpressionSubparser> load(Collection<Class<? extends ExpressionSubparser>> subparserClasses, ParserData data) throws Exception {
        List<ExpressionSubparser> subparsers = new ArrayList<>(subparserClasses.size());

        for (Class<? extends ExpressionSubparser> subparserClass : subparserClasses) {
            subparsers.add(subparserClass.newInstance());
        }

        for (ExpressionSubparser subparser : subparsers) {
            subparser.initialize(data);
        }

        return subparsers;
    }

}
