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

package org.panda_lang.framework.interpreter.parser.generation;

import org.panda_lang.framework.interpreter.parser.ParserInfo;

public interface ParserGenerationLayer {

    void callImmediately(ParserInfo currentInfo, ParserGenerationLayer nextLayer);

    void call(ParserInfo currentInfo, ParserGenerationLayer nextLayer);

    ParserGenerationLayer delegateImmediately(ParserGenerationCallback callback, ParserInfo delegated);

    ParserGenerationLayer delegateBefore(ParserGenerationCallback callback, ParserInfo delegated);

    ParserGenerationLayer delegate(ParserGenerationCallback callback, ParserInfo delegated);

    ParserGenerationLayer delegateAfter(ParserGenerationCallback callback, ParserInfo delegated);

    int countDelegates();

}
