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

package org.panda_lang.panda.implementation.interpreter.parser.generation;

import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationUnit;
import org.panda_lang.panda.implementation.interpreter.parser.util.Components;

import java.util.ArrayList;
import java.util.List;

public class PandaParserGenerationLayer implements ParserGenerationLayer {

    private final List<ParserGenerationUnit> immediately;
    private final List<ParserGenerationUnit> before;
    private final List<ParserGenerationUnit> delegates;
    private final List<ParserGenerationUnit> after;

    public PandaParserGenerationLayer() {
        this.immediately = new ArrayList<>();
        this.before = new ArrayList<>(1);
        this.delegates = new ArrayList<>();
        this.after = new ArrayList<>(1);
    }

    @Override
    public void callImmediately(ParserInfo currentInfo, ParserGenerationLayer nextLayer) {
        call(immediately, currentInfo, nextLayer);
    }

    @Override
    public void call(ParserInfo currentInfo, ParserGenerationLayer nextLayer) {
        call(before, currentInfo, nextLayer);
        call(delegates, currentInfo, nextLayer);
        call(after, currentInfo, nextLayer);
    }

    private void call(List<ParserGenerationUnit> units, ParserInfo currentInfo, ParserGenerationLayer nextLayer) {
        for (ParserGenerationUnit unit : units) {
            ParserGenerationCallback callback = unit.getCallback();
            ParserInfo delegatedInfo = unit.getDelegated();

            delegatedInfo.setComponent(Components.CURRENT_PARSER_INFO, currentInfo);
            callback.call(delegatedInfo, nextLayer);
        }

        units.clear();
    }

    @Override
    public ParserGenerationLayer delegateImmediately(ParserGenerationCallback callback, ParserInfo delegated) {
        return delegate(immediately, callback, delegated);
    }

    @Override
    public ParserGenerationLayer delegateBefore(ParserGenerationCallback callback, ParserInfo delegated) {
        return delegate(before, callback, delegated);
    }

    @Override
    public ParserGenerationLayer delegate(ParserGenerationCallback callback, ParserInfo delegated) {
        return delegate(delegates, callback, delegated);
    }

    @Override
    public ParserGenerationLayer delegateAfter(ParserGenerationCallback callback, ParserInfo delegated) {
        return delegate(after, callback, delegated);
    }

    public ParserGenerationLayer delegate(List<ParserGenerationUnit> units, ParserGenerationCallback callback, ParserInfo delegated) {
        ParserGenerationUnit unit = new PandaParserGenerationUnit(callback, delegated);
        units.add(unit);
        return this;
    }

    @Override
    public int countDelegates() {
        return immediately.size() + before.size() + delegates.size() + after.size();
    }

}
