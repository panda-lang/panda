/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.panda.util.configuration.spcl;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.util.configuration.spcl.value.SPCLValue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SPCLConfiguration {

    private static final Panda spclPanda = new Panda();
    private final Map<String, SPCLValue> values;
    private final SPCLLoader loader;
    private final SPCLSaver saver;
    private File cachedFile;

    public SPCLConfiguration() {
        this.values = new HashMap<>();
        this.loader = new SPCLLoader(this);
        this.saver = new SPCLSaver(this);
    }

    public void put(String key, Object value) {

    }

    public void load(File file) {
        this.cachedFile = file;
        loader.load(file);
    }

    public void save() {
        save(cachedFile);
    }

    public void save(File file) {
        this.cachedFile = file;
    }

    protected Map<String, SPCLValue> getValues() {
        return this.values;
    }

}
