/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.pattern.custom;

import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.KeywordElement;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;

import java.util.ArrayList;
import java.util.List;

final class SimpleCustomPatternProfiler {

    private static final Snippet SINGLE_KEYWORD = PandaLexerUtils.convert("PatternBenchmark", "while");
    private static final CustomPattern SINGLE_KEYWORD_CUSTOM = CustomPattern.of(KeywordElement.create(Keywords.WHILE));

    public static void main(String... args) {
        List<Result> results = new ArrayList<>(100_000_000);

        for (int i = 0; i < 100_000_000; i++) {
            Result result = SINGLE_KEYWORD_CUSTOM.match(SINGLE_KEYWORD);
            results.add(result);
        }

        System.out.println(results);
    }

}
