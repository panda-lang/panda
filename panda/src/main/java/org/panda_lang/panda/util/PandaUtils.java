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

package org.panda_lang.panda.util;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.utilities.annotations.AnnotationsScanner;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;

public class PandaUtils {

    public static final AnnotationsScannerProcess DEFAULT_PANDA_SCANNER = AnnotationsScanner.createScanner()
            .includeClassLoaders(false, Panda.class.getClassLoader())
            .build()
            .createWorker()
            .addDefaultProjectFilters("org.panda_lang")
            .fetch();

}
