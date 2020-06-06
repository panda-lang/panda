/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.framework.design.interpreter.parser.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Settings used by the single parse process
 */
public final class ExpressionParserSettings {

    /**
     * Represents selected mode
     */
    public enum SelectedMode {
        INCLUDE,
        EXCLUDE
    }

    /**
     * Default instance of expression settings
     */
    public static final ExpressionParserSettings DEFAULT = ExpressionParserSettings.create().build();

    private final Collection<String> selectedSubparsers;
    private final SelectedMode selectedMode;
    private final boolean standaloneOnly;

    ExpressionParserSettings(ExpressionParserSettingsBuilder builder) {
        this.selectedSubparsers = builder.selectedSubparsers;
        this.selectedMode = builder.selectedMode;
        this.standaloneOnly = builder.standaloneOnly;
    }

    /**
     * Get collection of selected subparsers
     *
     * @return the collection of selected subparsers
     */
    public Collection<? extends String> getSelectedSubparsers() {
        return selectedSubparsers;
    }

    /**
     * Get selection mode, possible results:
     *
     * <ul>
     *     <li>null - selection mode is not specified</li>
     *     <li>true - exclude</li>
     *     <li>false - include</li>
     * </ul>
     *
     * @return the selection mode
     */
    public SelectedMode getSelectedMode() {
        return selectedMode;
    }

    /**
     * Parse only standalone expressions
     *
     * @return true if parser can parse only standalone expressions
     */
    public boolean isStandaloneOnly() {
        return standaloneOnly;
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

        private Collection<String> selectedSubparsers;
        private SelectedMode selectedMode;
        private boolean standaloneOnly;

        ExpressionParserSettingsBuilder() { }

        /**
         * Include selected subparsers
         *
         * @return builder instance
         */
        public ExpressionParserSettingsBuilder includeSelected() {
            this.selectedMode = SelectedMode.INCLUDE;
            return this;
        }

        /**
         * Exclude selected subparsers
         *
         * @return builder instance
         */
        public ExpressionParserSettingsBuilder excludeSelected() {
            this.selectedMode = SelectedMode.EXCLUDE;
            return this;
        }

        /**
         * Set selected mode
         *
         * @param flag true to exclude, false to include selected subparsers
         * @return builder instance
         */
        public ExpressionParserSettingsBuilder selected(boolean flag) {
            if (flag) {
                includeSelected();
            }
            else {
                excludeSelected();
            }

            return this;
        }

        /**
         * Parse only standalone expressions
         *
         * @return builder instance
         */
        public ExpressionParserSettingsBuilder onlyStandalone() {
            this.standaloneOnly = true;
            return this;
        }

        /**
         * Select subparsers with the given name
         *
         * @param subparsers names of subparsers to select
         * @return builder instance
         */
        public ExpressionParserSettingsBuilder withSelectedSubparsers(String... subparsers) {
            return withSelectedSubparsers(Arrays.asList(subparsers));
        }

        /**
         * Select subparsers with the given name
         *
         * @param subparsers names of subparsers to select
         * @return builder instance
         */
        public ExpressionParserSettingsBuilder withSelectedSubparsers(Collection<String> subparsers) {
            if (selectedSubparsers == null) {
                this.selectedSubparsers = new ArrayList<>(subparsers.size());
            }

            this.selectedSubparsers.addAll(subparsers);
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
