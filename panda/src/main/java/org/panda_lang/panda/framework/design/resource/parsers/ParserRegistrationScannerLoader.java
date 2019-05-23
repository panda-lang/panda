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

package org.panda_lang.panda.framework.design.resource.parsers;

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;

import java.util.Collection;
import java.util.stream.Collectors;

class ParserRegistrationScannerLoader {

    @SuppressWarnings("unchecked")
    protected Collection<Class<? extends Parser>> load(AnnotationsScannerProcess scannerProcess) {
        PandaFramework.getLogger().debug("");
        PandaFramework.getLogger().debug("--- Loading pipelines ");

        return scannerProcess.createSelector()
                .selectTypesAnnotatedWith(ParserRegistration.class)
                .stream()
                .filter(clazz -> {
                    if (Parser.class.isAssignableFrom(clazz)) {
                        return true;
                    }

                    PandaFramework.getLogger().error(clazz + " is annotated with ParserRegistration and does not implement Parser");
                    return true;
                })
                .map(clazz -> (Class<? extends Parser>) clazz)
                .collect(Collectors.toList());
    }

}
