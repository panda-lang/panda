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

package org.panda_lang.panda.language.interpreter.messenger;

import org.panda_lang.framework.design.interpreter.messenger.MessengerFormatter;
import org.panda_lang.framework.design.interpreter.messenger.MessengerLevel;
import org.panda_lang.framework.design.interpreter.source.Source;

import java.util.Map;

public interface PandaTranslatorLayout<T extends Object> {

    default void onHandle(MessengerFormatter formatter, T element, Map<String, Object> context) { }

    boolean isInterrupting();

    String getPrefix();

    MessengerLevel getLevel();

    Source getTemplateSource();

    Class<T> getType();

}
