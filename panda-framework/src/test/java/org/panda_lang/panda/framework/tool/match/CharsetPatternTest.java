/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.tool.match;

import org.junit.Assert;
import org.junit.Test;
import org.panda_lang.panda.framework.implementation.PandaFramework;
import org.panda_lang.panda.utilities.redact.match.charset.CharsetPattern;

public class CharsetPatternTest {

    private static final String EXPRESSION = "instance.extractToken().method(parameter.extractToken())";

    @Test
    public void testCharsetPattern() {
        CharsetPattern charsetPattern = new CharsetPattern("*.*(*)");
        charsetPattern.setCharset(new char[]{ '.', '(', ')' });

        boolean matched = charsetPattern.match(EXPRESSION);
        PandaFramework.getLogger().info("[CharsetPattern] Matched: " + matched);

        Assert.assertEquals(true, matched);
    }
}
