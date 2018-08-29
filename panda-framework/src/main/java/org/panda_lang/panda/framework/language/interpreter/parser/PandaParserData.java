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

package org.panda_lang.panda.framework.language.interpreter.parser;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;

import java.util.HashMap;
import java.util.Map;

public class PandaParserData implements ParserData {

    private final Map<Component<?>, Object> components;

    public PandaParserData() {
        this(new HashMap<>());
    }

    private PandaParserData(Map<Component<?>, Object> components) {
        this.components = components;
    }

    @Override
    public PandaParserData fork() {
        PandaParserData parserInfo = new PandaParserData(new HashMap<>(components));
        parserInfo.setComponent(UniversalComponents.PARENT_DATA, this);
        return parserInfo;
    }

    @Override
    public <T> PandaParserData setComponent(Component<T> component, T value) {
        this.components.put(component, value);
        return this;
    }

    /**
     * @param componentName a name of the specified component
     * @return selected component
     */
    @Override
    @SuppressWarnings({ "unchecked" })
    public <T> T getComponent(Component<T> componentName) {
        return (T) components.get(componentName);
    }

    @Override
    public Map<? extends Component<?>, ? extends Object> getComponents() {
        return new HashMap<>(components);
    }

}
