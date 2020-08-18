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

package org.panda_lang.language.interpreter.pattern.linear;

import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.language.interpreter.token.SynchronizedSource;
import org.panda_lang.language.resource.syntax.PandaSyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Fast pattern based on simple linear matching, uses only simple elements:
 *
 * <ul>
 *     <li>unit</li>
 *     <li>wildcards:
 *         <ul>
 *             <li>simple (only one token)</li>
 *             <li>expression</li>
 *         </ul>
 *     </li>
 *     <li>section</li>
 * </ul>
 *
 * Supports identifiers and optional elements. <br>
 * Identifier is determined by the content before the <code>:</code> operator, <br>
 * optional is determined by the <code>&</code> before the identifier, example: <br><br>
 *
 *  <code>&identifier:token</code><br><br>
 *
 *  Sections are based on {@link org.panda_lang.language.resource.syntax.PandaSyntax}, <br>
 *  its definition is determined by the <code>~</code> operator between separators. Usage: <br><br>
 *
 *  <code>class Test {~}</code><br><br>
 *
 * Wildcards are determined by the <code>*</code> operator. <br>
 * Expression wildcards contains extra data defined by the <code>=expression</code> suffix, example: <br><br>
 *
 * <code>identifier:*=expression</code><br><br>
 *
 */
public final class LinearPattern {

    private static final LinearPatternCompiler COMPILER = new LinearPatternCompiler(new PandaSyntax(), new ArrayList<LinearPatternElementCompiler>(2) {{
        add(new WildcardElementCompiler());
        add(new SectionElementCompiler());
    }});

    static {
        COMPILER.initialize();
    }

    private final String patternSource;
    private final List<LinearPatternElement> element;

    LinearPattern(String patternSource, List<LinearPatternElement> element) {
        this.patternSource = patternSource;
        this.element = element;
    }

    public LinearPatternResult match(Snippet source) {
        SourceStream stream = new PandaSourceStream(source);
        LinearPatternResult result = match(stream);
        return stream.hasUnreadSource() ? LinearPatternResult.NOT_MATCHED : result;
    }

    public LinearPatternResult match(SourceStream source) {
        return match(source, null);
    }

    public LinearPatternResult match(SourceStream source, Function<SynchronizedSource, Object> expressionMatcher) {
        return new LinearPatternMatcher(this, source).match(expressionMatcher);
    }

    protected List<LinearPatternElement> getElements() {
        return element;
    }

    public String getPatternSource() {
        return patternSource;
    }

    public static LinearPattern compile(String pattern) {
        return COMPILER.compile(pattern);
    }

}
