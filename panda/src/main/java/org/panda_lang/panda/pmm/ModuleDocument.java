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

package org.panda_lang.panda.pmm;

import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

final class ModuleDocument {

    private final File document;
    private final JsonObject content;

    ModuleDocument(File document, JsonObject content) {
        this.document = document;
        this.content = content;
    }

    private List<? extends String> getList(String name) {
        return content.get(name).asArray().values().stream()
                .map(JsonValue::asString)
                .collect(Collectors.toList());
    }

    protected List<? extends String> getTestsDependencies() {
        return getList("tests-dependencies");
    }

    protected List<? extends String> getDependencies() {
        return getList("dependencies");
    }

    protected String getMainScript() {
        JsonValue scripts = content.get("scripts");
        return scripts == null ? null : scripts.asObject().getString("main", null);
    }

    protected String getOwner() {
        return content.getString("owner", null);
    }

    protected String getVersion() {
        return content.getString("version", null);
    }

    protected String getName() {
        return content.getString("name", null);
    }

    protected File getDocument() {
        return document;
    }

}
