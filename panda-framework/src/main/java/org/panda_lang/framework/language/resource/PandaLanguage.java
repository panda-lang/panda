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

package org.panda_lang.framework.language.resource;

import org.panda_lang.framework.design.resource.Language;
import org.panda_lang.framework.design.resource.Syntax;
import org.panda_lang.framework.language.resource.syntax.PandaSyntax;

public class PandaLanguage implements Language {

    private final PandaLanguageBuilder builder;

    private PandaLanguage(PandaLanguageBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Syntax getSyntax() {
        return builder.syntax;
    }

    public static PandaLanguageBuilder builder() {
        return new PandaLanguageBuilder();
    }

    public static final class PandaLanguageBuilder {

        public Syntax syntax;

        private PandaLanguageBuilder() { }

        public PandaLanguageBuilder withSyntax(Syntax syntax) {
            this.syntax = syntax;
            return this;
        }

        public PandaLanguage build() {
            if (syntax == null) {
                this.syntax = new PandaSyntax();
            }

            return new PandaLanguage(this);
        }

    }

}
