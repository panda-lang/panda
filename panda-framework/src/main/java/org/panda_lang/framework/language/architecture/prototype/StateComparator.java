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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.prototype.Type;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;

public final class StateComparator {

    private StateComparator() { }

    public static boolean requireInstantiation(Context context, Type type, Snippetable source) {
        if (type.getState().canBeInstantiated()) {
            return true;
        }

        throw new PandaParserFailure(context, source, "Cannot create instance of abstract prototype");
    }

    public static boolean requireInheritance(Context context, Type type, Snippetable source) {
        if (type.getState().canBeInherited()) {
            return true;
        }

        throw new PandaParserFailure(context, source, "Cannot inherit final prototype");
    }

}
