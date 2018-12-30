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

package org.panda_lang.panda.utilities.commons;

import org.jetbrains.annotations.Nullable;

public class PackageUtils {

    public static String getShortenPackage(Class<?> clazz) {
        return getShortenPackage(clazz.getName());
    }

    public static String getShortenPackage(String pack) {
        int separator = StringUtils.lastIndexOfBefore(pack, ".", 1);

        String className = pack.substring(separator + 1);
        String packagePath = pack.substring(0, separator - 1);

        String[] packages = packagePath.split("\\.");
        StringBuilder builder = new StringBuilder();

        for (String element : packages) {
            builder.append(element, 0, 1).append(".");
        }

        return builder.append(className).toString();
    }

    public static @Nullable String getPackageName(Class<?> clazz) {
        return clazz.getPackage() == null ? null : clazz.getPackage().getName();
    }

    public static String toString(Package pack, String defaultValue) {
        return pack != null ? pack.getName() : defaultValue;
    }

    public static @Nullable String toString(Package pack) {
        return pack != null ? pack.getName() : null;
    }

}
