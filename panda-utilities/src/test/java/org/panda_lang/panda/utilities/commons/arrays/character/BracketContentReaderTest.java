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

package org.panda_lang.panda.utilities.commons.arrays.character;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.utilities.commons.redact.BracketContentReader;

public class BracketContentReaderTest {

    private static final String CONTENT = "(Test (A[B{C\"D\"E}F]G) Test) EoT";

    @Test
    public void testBracketContentReader() {
        BracketContentReader bracketContentReader = new BracketContentReader(CONTENT);
        String content = bracketContentReader.read();

        Assertions.assertEquals("Test (A[B{C\"D\"E}F]G) Test", content);
    }

}
