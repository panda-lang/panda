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

package org.panda_lang.panda.utilities.autodata.database;

import org.panda_lang.panda.utilities.autodata.AutomatedDataSpaceBuilder;

public class ADSDatabaseBuilder {

    private final AutomatedDataSpaceBuilder builder;
    protected String name;
    protected ADSDatabaseRepository repository;

    public ADSDatabaseBuilder(AutomatedDataSpaceBuilder builder) {
        this.builder = builder;
    }

    public ADSDatabaseBuilder name(String databaseName) {
        this.name = databaseName;
        return this;
    }

    public ADSDatabaseBuilder repository(ADSDatabaseRepository repository) {
        this.repository = repository;
        return this;
    }

    public AutomatedDataSpaceBuilder append() {
        return builder.addDatabase(new ADSDatabase(this));
    }

}
