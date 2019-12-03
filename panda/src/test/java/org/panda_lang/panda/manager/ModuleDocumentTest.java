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

package org.panda_lang.panda.manager;

import org.hjson.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

class ModuleDocumentTest {

    private static ModuleDocument document;

    @BeforeAll
    static void prepare() {
        String value =
                "name: test-module\n" +
                        "version: 1.0.0\n" +
                        "owner: dzikoysk\n" +
                        "scripts: {\n" +
                        "main: app.panda\n" +
                        "}\n" +
                        "dependencies: [\n" +
                        "github:owner-one/dependency-one@1.0.0\n" +
                        "github:owner-three/dependency-three@1.0.0\n" +
                        "]\n" +
                        "tests-dependencies: [\n" +
                        "github:owner-two/dependency-two@1.0.0\n" +
                        "]\n";

        document = new ModuleDocument(new File("."), JsonValue.readHjson(value).asObject());
    }

    @Test
    void getTestsDependencies() {
        Assertions.assertEquals("[owner-two/dependency-two@1.0.0]", document.getTestsDependencies().toString());
    }

    @Test
    void getDependencies() {
        Assertions.assertEquals("[owner-one/dependency-one@1.0.0, owner-three/dependency-three@1.0.0]", document.getDependencies().toString());
    }

    @Test
    void getMainScript() {
        Assertions.assertEquals("app.panda", document.getMainScript());
    }

    @Test
    void getOwner() {
        Assertions.assertEquals("dzikoysk", document.getOwner());
    }

    @Test
    void getVersion() {
        Assertions.assertEquals("1.0.0", document.getVersion());
    }

    @Test
    void getName() {
        Assertions.assertEquals("test-module", document.getName());
    }

}