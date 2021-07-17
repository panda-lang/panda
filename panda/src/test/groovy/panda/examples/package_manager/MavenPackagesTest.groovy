/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package panda.examples.package_manager

import groovy.transform.CompileStatic
import panda.interpreter.utils.PandaUtils
import panda.manager.PackageManager

@CompileStatic
final class MavenPackagesTest {

    private static final File PROJECT = new File("../examples/package_manager/maven-packages/panda.cdn")
    private static final PackageManager MANAGER = new PackageManager(PandaUtils.defaultInstance(), PROJECT.getParentFile())

    // @Test TODO: Add arrays
    void test() throws Exception {
        MANAGER.install(PROJECT)
        MANAGER.run(PROJECT)
    }

}