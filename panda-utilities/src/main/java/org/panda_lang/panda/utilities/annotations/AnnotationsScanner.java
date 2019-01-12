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

package org.panda_lang.panda.utilities.annotations;

public class AnnotationsScanner {

    private final AnnotationsScannerConfiguration configuration;

    AnnotationsScanner(AnnotationsScannerConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * <p>Create new AnnotationScanner process based on current configuration.</p>
     * <p>
     * Steps:
     *
     * <ul>
     *     <li>filter resources by path</li>
     *     <li>filter resources by package</li>
     *     <li>filter resources by offline class content</li>
     *     <li>collect offline classes</li>
     * </ul>
     *
     * @return process builder
     */
    public AnnotationsScannerProcessBuilder createProcess() {
        return new AnnotationsScannerProcessBuilder(this, new AnnotationScannerStore());
    }

    /**
     * @return the configuration of current scanner
     */
    AnnotationsScannerConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * @return logger used by AnnotationScanner
     */
    public AnnotationsScannerLogger getLogger() {
        return configuration.logger;
    }

    /**
     * Create AnnotationScanner using configuration
     *
     * @return configuration used by scanner
     */
    public static AnnotationsScannerConfiguration configuration() {
        return new AnnotationsScannerConfiguration();
    }

}
