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

package org.panda_lang.panda.core.interpreter.parser.generation;

import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationUnit;

import java.util.ArrayList;
import java.util.List;

public class PandaCasualParserGenerationLayer implements CasualParserGenerationLayer {

    private final List<CasualParserGenerationUnit> immediately;
    private final List<CasualParserGenerationUnit> before;
    private final List<CasualParserGenerationUnit> delegates;
    private final List<CasualParserGenerationUnit> after;

    public PandaCasualParserGenerationLayer() {
        this.immediately = new ArrayList<>();
        this.before = new ArrayList<>(1);
        this.delegates = new ArrayList<>();
        this.after = new ArrayList<>(1);
    }

    @Override
    public void callImmediately(ParserInfo currentInfo, CasualParserGenerationLayer nextLayer) {
        call(immediately, currentInfo, nextLayer);
    }

    @Override
    public void call(ParserInfo currentInfo, CasualParserGenerationLayer nextLayer) {
        call(before, currentInfo, nextLayer);
        call(delegates, currentInfo, nextLayer);
        call(after, currentInfo, nextLayer);
    }

    private void call(List<CasualParserGenerationUnit> units, ParserInfo currentInfo, CasualParserGenerationLayer nextLayer) {
        for (CasualParserGenerationUnit unit : units) {
            CasualParserGenerationCallback callback = unit.getCallback();
            ParserInfo delegatedInfo = unit.getDelegated();

            delegatedInfo.setComponent(Components.CURRENT_PARSER_INFO, currentInfo);
            callback.call(delegatedInfo, nextLayer);
        }

        units.clear();
    }

    @Override
    public CasualParserGenerationLayer delegateImmediately(CasualParserGenerationCallback callback, ParserInfo delegated) {
        return delegate(immediately, callback, delegated);
    }

    @Override
    public CasualParserGenerationLayer delegateBefore(CasualParserGenerationCallback callback, ParserInfo delegated) {
        return delegate(before, callback, delegated);
    }

    @Override
    public CasualParserGenerationLayer delegate(CasualParserGenerationCallback callback, ParserInfo delegated) {
        return delegate(delegates, callback, delegated);
    }

    @Override
    public CasualParserGenerationLayer delegateAfter(CasualParserGenerationCallback callback, ParserInfo delegated) {
        return delegate(after, callback, delegated);
    }

    public CasualParserGenerationLayer delegate(List<CasualParserGenerationUnit> units, CasualParserGenerationCallback callback, ParserInfo delegated) {
        CasualParserGenerationUnit unit = new PandaCasualParserGenerationUnit(callback, delegated);
        units.add(unit);
        return this;
    }

    @Override
    public int countDelegates() {
        return immediately.size() + before.size() + delegates.size() + after.size();
    }

}
