/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.util.spcl;

import org.panda_lang.panda.util.spcl.storage.SPCLLoader;
import org.panda_lang.panda.util.spcl.storage.SPCLSaver;
import org.panda_lang.panda.util.spcl.value.SPCLSection;

import java.io.File;

public class SPCLConfiguration extends SPCLSection {

    private final SPCLVariables variables;
    private final SPCLLoader loader;
    private final SPCLSaver saver;

    public SPCLConfiguration() {
        super();

        this.variables = new SPCLVariables();
        this.loader = new SPCLLoader(this);
        this.saver = new SPCLSaver(this);
    }

    public void load(File file) {
        loader.load(file);
    }

    public void save(File file) {
        saver.save(file);
    }

    public SPCLVariables getVariables() {
        return variables;
    }

}
