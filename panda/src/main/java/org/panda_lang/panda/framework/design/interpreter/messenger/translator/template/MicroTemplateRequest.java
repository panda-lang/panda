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

package org.panda_lang.panda.framework.design.interpreter.messenger.translator.template;

import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerFormatter;
import org.panda_lang.panda.framework.design.interpreter.source.Source;

import java.util.Map;

public final class MicroTemplateRequest {

    private final MicroTemplateRequestBuilder builder;

    private MicroTemplateRequest(MicroTemplateRequestBuilder builder) {
        this.builder = builder;
    }

    protected String getPrefix() {
        return builder.prefix;
    }

    protected Map<String, Object> getData() {
        return builder.context;
    }

    protected MessengerFormatter getFormatter() {
        return builder.formatter;
    }

    protected Source getSource() {
        return builder.source;
    }

    public static MicroTemplateRequestBuilder builder() {
        return new MicroTemplateRequestBuilder();
    }

    public static final class MicroTemplateRequestBuilder {

        private Source source;
        private MessengerFormatter formatter;
        private Map<String, Object> context;
        private String prefix;

        public MicroTemplateRequestBuilder withSource(Source source) {
            this.source = source;
            return this;
        }

        public MicroTemplateRequestBuilder withFormatter(MessengerFormatter formatter) {
            this.formatter = formatter;
            return this;
        }

        public MicroTemplateRequestBuilder withData(Map<String, Object> context) {
            this.context = context;
            return this;
        }

        public MicroTemplateRequestBuilder withPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public MicroTemplateRequest build() {
            return new MicroTemplateRequest(this);
        }

    }

}
