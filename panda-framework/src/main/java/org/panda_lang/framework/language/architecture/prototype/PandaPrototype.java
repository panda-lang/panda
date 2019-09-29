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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.source.Source;

public class PandaPrototype extends AbstractPrototype {

    private boolean initialized;

    protected PandaPrototype(Module module, String className, Source source, Class<?> associated, Visibility visibility) {
        super(module, className, source, associated, visibility);
    }

    protected PandaPrototype(PandaPrototypeBuilder<?, ?> builder) {
        this(builder.module, builder.name, builder.source, builder.associated, builder.visibility);
    }

    public synchronized void initialize() throws Exception {
        if (initialized) {
            return;
        }

        this.initialized = true;

        for (PrototypeField field : fields.getProperties()) {
            if (!field.hasDefaultValue() || !field.isStatic()) {
                continue;
            }

            Expression expression = field.getDefaultValue();
            field.setStaticValue(expression.evaluate(null, null));
        }
    }

    public static <T> PandaPrototypeBuilder<?, ?> builder() {
        return new PandaPrototypeBuilder<>();
    }

    public static class PandaPrototypeBuilder<BUILDER extends PandaPrototypeBuilder<BUILDER, ?>, TYPE extends PandaPrototype> {

        protected String name;
        protected Module module;
        protected Source source;
        protected Class<?> associated;
        protected Visibility visibility;

        protected PandaPrototypeBuilder() {
            this.associated = Object.class;
        }

        public BUILDER name(String name) {
            this.name = name;
            return getThis();
        }

        public BUILDER module(Module module) {
            this.module = module;
            return getThis();
        }

        public BUILDER source(Source source) {
            this.source = source;
            return getThis();
        }

        public BUILDER associated(Class associated) {
            this.associated = associated;

            if (name == null) {
                this.name = associated.getCanonicalName();
            }

            return getThis();
        }

        public BUILDER visibility(Visibility visibility) {
            this.visibility = visibility;
            return getThis();
        }

        @SuppressWarnings("unchecked")
        public TYPE build() {
            if (name == null) {
                throw new IllegalArgumentException("ClassPrototype name is not defined");
            }

            return (TYPE) new PandaPrototype(this);
        }

        @SuppressWarnings("unchecked")
        protected BUILDER getThis() {
            return (BUILDER) this;
        }

    }

}
