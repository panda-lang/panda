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

package org.panda_lang.framework.language.interpreter.pattern;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.BenchmarkRunner;
import org.panda_lang.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.Result;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.KeywordElement;
import org.panda_lang.framework.language.interpreter.pattern.linear.LinearPattern;
import org.panda_lang.framework.language.interpreter.pattern.linear.LinearPatternResult;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;

import java.util.concurrent.TimeUnit;

@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class PatternBenchmark {

    private static final Snippet SINGLE_KEYWORD = PandaLexerUtils.convert("PatternBenchmark", "while");
    private static final CustomPattern SINGLE_KEYWORD_CUSTOM = CustomPattern.of(KeywordElement.create(Keywords.WHILE));
    private static final LinearPattern SINGLE_KEYWORD_LINEAR = LinearPattern.compile("while");

    @Benchmark
    public Result singleKeywordCustom() {
        return SINGLE_KEYWORD_CUSTOM.match(SINGLE_KEYWORD);
    }

    @Benchmark
    public LinearPatternResult singleKeywordLinear() {
        return SINGLE_KEYWORD_LINEAR.match(SINGLE_KEYWORD);
    }

    public static void main(String[] args) throws Exception {
        BenchmarkRunner.run(PatternBenchmark.class);
    }

}
