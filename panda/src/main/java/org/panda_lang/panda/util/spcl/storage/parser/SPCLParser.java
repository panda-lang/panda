/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.util.spcl.storage.parser;

import org.panda_lang.framework.interpreter.parser.Parser;
import org.panda_lang.panda.util.spcl.SPCLConfiguration;
import org.panda_lang.panda.util.spcl.SPCLEntry;
import org.panda_lang.panda.util.spcl.storage.parser.specific.SimpleValueParser;
import org.panda_lang.panda.util.spcl.value.SPCLSection;

public class SPCLParser implements Parser {

    private static final SimpleValueParser simpleValueParser = new SimpleValueParser();

    private final SPCLConfiguration configuration;
    private final SPCLSection section;

    public SPCLParser(SPCLConfiguration configuration, SPCLSection section) {
        this.configuration = configuration;
        this.section = section;
    }

    public void parse(String source) {
        String[] lines = source.split(System.lineSeparator());

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if (SimpleValueParser.PATTERN.match(line)) {
                SPCLEntry entry = simpleValueParser.getSimpleValue(configuration);
                section.put(entry);
            }

            throw new RuntimeException("Unknown source at line " + (i + 1));
        }
    }

}
