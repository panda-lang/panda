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

import java.util.ArrayList;
import java.util.List;

public class FlexibleModelVariantBuilder extends FlexibleModelElementBuilder {

    private final List<FlexibleModelElement> variants = new ArrayList<>(3);

    protected FlexibleModelVariantBuilder(FlexibleModel.FlexibleModelBuilder builder, FlexibleModelElementBuilder parent, boolean optional) {
        super(builder, parent, optional);
    }

    public FlexibleModelVariantElementBuilder option() {
        return new FlexibleModelVariantElementBuilder(super.builder, this, false);
    }

    public static class FlexibleModelVariantElementBuilder extends FlexibleModelElementBuilder {

        private FlexibleModelVariantElementBuilder(FlexibleModel.FlexibleModelBuilder builder, FlexibleModelVariantBuilder parent, boolean optional) {
            super(builder, parent, optional);
        }

        @Override
        public FlexibleModelVariantBuilder apply() {
            return (FlexibleModelVariantBuilder) super.apply();
        }

    }

}
