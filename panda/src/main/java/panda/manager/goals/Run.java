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

package panda.manager.goals;

import panda.interpreter.architecture.packages.Package;
import panda.interpreter.PandaEnvironment;
import panda.manager.PackageInfo;
import panda.manager.PackageManager;
import panda.manager.PackageManagerConstants;
import panda.manager.PackageUtils;
import panda.std.Option;

import java.io.File;
import java.util.Objects;

public final class Run {

    public Option<Object> run(PackageManager manager, PackageInfo packageInfo) throws Exception {
        PandaEnvironment environment = new PandaEnvironment(manager.getFrameworkController(), manager.getWorkingDirectory());
        environment.initialize();

        File pandaModules = packageInfo.getPandaModules();
        File[] owners = pandaModules.listFiles();

        for (File ownerDirectory : owners) {
            for (File packageDirectory : Objects.requireNonNull(ownerDirectory.listFiles())) {
                File dependencyPackageInfo = new File(packageDirectory, PackageManagerConstants.PACKAGE_INFO);

                if (!dependencyPackageInfo.exists()) {
                    manager.getLogger().debug("Module located in " + packageDirectory + " does not contain package info");
                    continue;
                }

                try {
                    Package pkg = PackageUtils.directoryToPackage(packageDirectory);
                    environment.getPackages().registerPackage(pkg);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return Option.none();
                }
            }
        }

        Package packageToRun = PackageUtils.directoryToPackage(packageInfo.getFile().getAbsoluteFile().getParentFile());

        return environment.getInterpreter()
                .interpret(packageToRun)
                .orElseThrow(throwable -> new RuntimeException("Cannot launch application due to failures in interpretation process"))
                .launch()
                .get();
    }

}
