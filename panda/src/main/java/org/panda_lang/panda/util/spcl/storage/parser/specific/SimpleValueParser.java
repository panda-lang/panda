/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.panda.util.spcl.storage.parser.specific;

import org.panda_lang.framework.interpreter.parser.Parser;
import org.panda_lang.framework.interpreter.token.suggestion.Indentation;
import org.panda_lang.framework.tool.match.hollow.HollowPattern;
import org.panda_lang.framework.util.StringUtils;
import org.panda_lang.panda.util.spcl.SPCLConfiguration;
import org.panda_lang.panda.util.spcl.SPCLEntry;
import org.panda_lang.panda.util.spcl.util.VariableUtils;
import org.panda_lang.panda.util.spcl.value.SPCLSimpleValue;
import org.panda_lang.panda.util.spcl.value.SPCLValue;
import org.panda_lang.panda.util.spcl.value.SPCLVar;

import java.util.List;

public class SimpleValueParser implements Parser {

    public static HollowPattern PATTERN = HollowPattern.builder().compile("*:*").build();

    public SPCLEntry getSimpleValue(SPCLConfiguration configuration) {
        List<String> hollows = PATTERN.cloneHollows();
        SPCLEntry entry = new SPCLEntry();

        String beginning = hollows.get(0);
        Indentation indentation = Indentation.valueOf(StringUtils.extractParagraph(beginning));

        String key = beginning.trim();
        String value = hollows.get(1).trim();

        SPCLValue spclValue;

        if (value.startsWith("@")) {
            spclValue = new SPCLVar(configuration.getVariables(), VariableUtils.getName(value));
        }
        else {
            spclValue = new SPCLSimpleValue(value);
        }

        if (key.startsWith("@")) {
            configuration.getVariables().set(VariableUtils.getName(key), spclValue);
        }

        entry.setKey(key);
        entry.setValue(spclValue);
        return entry;
    }

}
