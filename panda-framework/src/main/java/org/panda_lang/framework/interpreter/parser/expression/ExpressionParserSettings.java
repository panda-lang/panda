/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.interpreter.parser.expression;

/**
 * Settings used by the single parse process
 */
public final class ExpressionParserSettings {

    /**
     * Default instance of expression settings
     */
    public static final ExpressionParserSettings DEFAULT = ExpressionParserSettings.create().build();

    private final boolean standaloneOnly;
    private final boolean mutualDisabled;

    ExpressionParserSettings(ExpressionParserSettingsBuilder builder) {
        this.standaloneOnly = builder.standaloneOnly;
        this.mutualDisabled = builder.mutualDisabled;
    }

    /**
     * Parse only standalone expressions
     *
     * @return true if parser can parse only standalone expressions
     */
    public boolean isStandaloneOnly() {
        return standaloneOnly;
    }

    public boolean isMutualDisabled() {
        return mutualDisabled;
    }

    /**
     * Create settings builder
     *
     * @return a new builder instance
     */
    public static ExpressionParserSettingsBuilder create() {
        return new ExpressionParserSettingsBuilder();
    }

    /**
     * Settings builder
     */
    public static final class ExpressionParserSettingsBuilder {

        private boolean standaloneOnly;
        private boolean mutualDisabled;

        ExpressionParserSettingsBuilder() { }

        /**
         * Parse only standalone expressions
         *
         * @return builder instance
         */
        public ExpressionParserSettingsBuilder onlyStandalone() {
            this.standaloneOnly = true;
            return this;
        }

        public ExpressionParserSettingsBuilder mutualDisabled() {
            this.mutualDisabled = true;
            return this;
        }

        /**
         * Create settings based on the collected data
         *
         * @return a new settings instance
         */
        public ExpressionParserSettings build() {
            return new ExpressionParserSettings(this);
        }

    }

}
