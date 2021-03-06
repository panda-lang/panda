/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.syntax.expressions.subparsers.operation.pattern;

import panda.interpreter.token.Snippet;
import panda.interpreter.token.Token;
import panda.interpreter.resource.syntax.separator.Separator;

public final class OperationPattern {

    private final Separator[] separators;
    private final Token[] dividers;

    public OperationPattern(Separator[] separators, Token[] dividers) {
        this.separators = separators;
        this.dividers = dividers;
    }

    public OperationPatternResult extract(Snippet source) {
        OperationPatternWorker worker = new OperationPatternWorker(this, source);
        return worker.extract();
    }

    protected Token[] getDividers() {
        return dividers;
    }

    public Token[] getSeparators() {
        return separators;
    }

}
