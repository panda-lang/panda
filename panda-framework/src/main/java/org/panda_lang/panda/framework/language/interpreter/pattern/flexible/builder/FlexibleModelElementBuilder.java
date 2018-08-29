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

package org.panda_lang.panda.framework.language.interpreter.pattern.flexible.builder;

import org.panda_lang.panda.framework.language.interpreter.pattern.flexible.FlexibleModel;
import org.panda_lang.panda.framework.language.interpreter.pattern.flexible.FlexibleModelElement;

public class FlexibleModelElementBuilder {

    protected final FlexibleModel.FlexibleModelBuilder builder;
    protected final FlexibleModelElementBuilder parent;
    protected final FlexibleModelElement element;

    public FlexibleModelElementBuilder(FlexibleModel.FlexibleModelBuilder builder, FlexibleModelElementBuilder parent, boolean optional) {
        this.builder = builder;
        this.parent = parent;
        this.element = new FlexibleModelElement(optional);
    }

    public FlexibleModelElementBuilder basic(String element) {
        this.element.add(new FlexibleModelElement(false, element));
        return this;
    }

    public FlexibleModelElementBuilder expression(Class<?> type) {
        this.element.add(new FlexibleModelElement(false, type));
        return this;
    }

    public FlexibleModelElementBuilder optional() {
        return new FlexibleModelElementBuilder(builder, this, true);
    }

    public FlexibleModelVariantBuilder variant() {
        return new FlexibleModelVariantBuilder(builder, this, true);
    }

    public FlexibleModelElementBuilder apply() {
        parent.getElement().add(this.element);
        return parent;
    }

    public FlexibleModel.FlexibleModelBuilder finish() {
        return builder;
    }

    public FlexibleModelElement getElement() {
        return element;
    }

}
