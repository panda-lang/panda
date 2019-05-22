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

package org.panda_lang.panda.framework.language.interpreter.token.distributors;

import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.lexer.Lexer;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.language.interpreter.source.PandaSource;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

public class MatchableDistributorTest {

    @Test
    public void testMatchable() {
        Lexer lexer = PandaLexer.of(new PandaSyntax(), new PandaSource("MatchableDistributorTest", "test [ a { b } c ] element")).build();
        MatchableDistributor distributor = new MatchableDistributor(new TokenDistributor(lexer.convert()));

        while (distributor.hasNext()) {
            System.out.println(distributor.nextVerified() + " isMatchable=" + distributor.isMatchable());
        }
    }

}
