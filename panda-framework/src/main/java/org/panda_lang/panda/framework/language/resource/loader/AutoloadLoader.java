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

package org.panda_lang.panda.framework.language.resource.loader;

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.resource.Autoload;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;

import java.util.Collection;

public class AutoloadLoader {

    public void load(AnnotationsScannerProcess scannerProcess) {
        PandaFramework.getLogger().debug("");
        PandaFramework.getLogger().debug("--- Loading autoloaded classes");

        Collection<Class<?>> classes = scannerProcess.createSelector().selectTypesAnnotatedWith(Autoload.class);

        for (Class<?> clazz : classes) {
            load(clazz);
        }
    }

    private void load(Class<?> clazz) {
        try {
            Class.forName(clazz.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
