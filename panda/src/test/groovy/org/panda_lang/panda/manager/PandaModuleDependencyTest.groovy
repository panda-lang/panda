/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.manager

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import org.panda_lang.panda.utils.PandaUtils

@CompileStatic
final class PandaModuleDependencyTest {

    private static final File DIRECTORY = new File("../examples/package_manager/panda-module-dependency")
    private static final PackageManager MANAGER = new PackageManager(PandaUtils.defaultInstance(), DIRECTORY)

    @Test
    void test() throws Exception {
        PandaUtils.printJVMUptime(MANAGER)
        File document = new File(DIRECTORY, "panda.cdn")

        MANAGER.install(document)
        MANAGER.run(document)
    }

}