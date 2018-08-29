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

package org.panda_lang.panda.framework.language.interpreter.pattern.flexible;

import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.language.interpreter.pattern.flexible.builder.FlexibleModelElementBuilder;

public class FlexibleModel {

    private final FlexibleModelElement structure;
    private final Executable executable;

    protected FlexibleModel(FlexibleModelElement element, Executable executable) {
        this.structure = element;
        this.executable = executable;
    }

    public Executable getExecutable() {
        return executable;
    }

    public FlexibleModelElement getStructure() {
        return structure;
    }

    public static FlexibleModelBuilder builder() {
        return new FlexibleModelBuilder();
    }

    public static class FlexibleModelBuilder {

        private final FlexibleModelElementBuilder primaryBuilder = new FlexibleModelElementBuilder(this, null, false);
        private Executable executable;

        protected FlexibleModelBuilder() {
        }

        public FlexibleModelElementBuilder compose() {
            return primaryBuilder;
        }

        public FlexibleModelBuilder statement(Executable executable) {
            this.executable = executable;
            return this;
        }

        public FlexibleModel createModel() {
            return new FlexibleModel(primaryBuilder.getElement(), executable);
        }

    }

}
