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

package org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers;

import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.util.PandaUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DefaultSubparsers {

    public static class Names {

        public static final String FIELD = "field";

        public static final String INSTANCE = "instance";

        public static final String LITERAL = "literal";

        public static final String MATH = "math";

        public static final String METHOD = "method";

        public static final String NEGATE = "negate";

        public static final String NUMBER = "number";

        public static final String SECTION = "section";

        public static final String SEQUENCE = "sequence";

        public static final String SINGLE = "single";

    }

    public static class Priorities {

        private static final double GROUND_ZERO = 0;
        private static final double GROUP_DIFF = 0.01D;

        public static final double ADVANCED_DYNAMIC = GROUND_ZERO + 1;
        public static final double DYNAMIC = ADVANCED_DYNAMIC + 1;
        public static final double SIMPLE_DYNAMIC = DYNAMIC + 1;
        public static final double SINGLE = SIMPLE_DYNAMIC + 1;

        public static class Dynamic {

            public static final double CONSTRUCTOR_CALL = DYNAMIC + GROUP_DIFF;

        }

    }

    public static class Instances {

        private static final List<ExpressionSubparser> SUBPARSERS = new ArrayList<>();

        static {
            Collection<Class<? extends ExpressionSubparser>> subparserClasses = PandaUtils.DEFAULT_PANDA_SCANNER
                    .createSelector()
                    .selectSubtypesOf(ExpressionSubparser.class);

            try {
                load(subparserClasses);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Collections.sort(SUBPARSERS);
        }

        private static void load(Collection<Class<? extends ExpressionSubparser>> subparserClasses) throws Exception {
            for (Class<? extends ExpressionSubparser> subparserClass : subparserClasses) {
                SUBPARSERS.add(subparserClass.newInstance());
            }
        }

        public static List<ExpressionSubparser> getDefaultSubparsers(Collection<String> names) {
            List<ExpressionSubparser> subparsers = new ArrayList<>();

            for (ExpressionSubparser subparser : SUBPARSERS) {
                if (names.contains(subparser.getName().trim())) {
                    subparsers.add(subparser);
                }
            }

            return subparsers;
        }

        public static List<ExpressionSubparser> getDefaultSubparsers() {
            return new ArrayList<>(SUBPARSERS);
        }

    }

}
