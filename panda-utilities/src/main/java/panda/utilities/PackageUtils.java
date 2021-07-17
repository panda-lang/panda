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

package panda.utilities;

import org.jetbrains.annotations.Nullable;

public final class PackageUtils {

    private PackageUtils() { }

    /**
     * Display package using only first letters
     *
     * @param clazz the class to use
     * @return the formatted package
     *
     * @see #getShortenPackage(String)
     */
    public static String getShortenPackage(Class<?> clazz) {
        return getShortenPackage(clazz.getName());
    }

    /**
     * Display package using only first letters
     *
     * @param pack the package represented by string (with class name at the end)
     * @return the formatted package
     */
    public static String getShortenPackage(String pack) {
        int separator = StringUtils.lastIndexOfBefore(pack, ".", 1);

        String className = pack.substring(separator + 1);
        String packagePath = pack.substring(0, separator - 1);

        String[] packages = StringUtils.split(packagePath, ".");
        StringBuilder builder = new StringBuilder();

        for (String element : packages) {
            builder.append(element, 0, 1).append(".");
        }

        return builder.append(className).toString();
    }

    /**
     * Get package name (supports primitives)
     *
     * @param clazz the class to get the package
     * @return the package name or null if class does not have package
     */
    public static @Nullable String getPackageName(Class<?> clazz) {
        return clazz.getPackage() == null ? null : clazz.getPackage().getName();
    }

    /**
     * Package to string
     *
     * @param pack         the package to stringify
     * @param defaultValue the value returned when the package is null
     * @return the package name or the default value if package is null
     */
    public static String toString(@Nullable Package pack, @Nullable String defaultValue) {
        return pack != null ? pack.getName() : defaultValue;
    }

    /**
     * Package to string
     *
     * @param pkg the package to stringify
     * @return the package name or null if the package is null
     */
    public static @Nullable String toString(@Nullable Package pkg) {
        return toString(pkg, null);
    }

}
