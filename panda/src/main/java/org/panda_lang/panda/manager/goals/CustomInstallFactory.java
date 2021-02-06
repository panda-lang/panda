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

package org.panda_lang.panda.manager.goals;

import org.panda_lang.panda.manager.Dependency;
import org.panda_lang.panda.manager.PackageInfo;
import org.panda_lang.panda.manager.PackageManager;

final class CustomInstallFactory {
    
    public CustomInstall createCustomInstall(PackageManager packageManager, PackageInfo document, Dependency dependency) {
        switch (dependency.getType()) {
            case "github":
                return new GitHubInstall(packageManager, dependency);
            case "maven":
                return new MavenInstall(packageManager, document, dependency);
            default:
                throw new IllegalArgumentException("Unknown dependency type: " + dependency.getType());
        }
    }
    
}
