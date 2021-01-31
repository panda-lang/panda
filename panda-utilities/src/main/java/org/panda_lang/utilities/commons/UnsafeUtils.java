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

package org.panda_lang.utilities.commons;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class UnsafeUtils {

    private UnsafeUtils() { }

    @SuppressWarnings("unchecked")
    public static <R, E extends Throwable> R throwException(Throwable throwable) throws E {
        throw (E) throwable;
    }

    /**
     * Disable message about illegal reflective access operation on Java 9+
     */
    public static void disableIllegalAccessMessage() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");

            Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
            Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);

            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Object unsafe = field.get(null);

            Class<?> loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field loggerField = loggerClass.getDeclaredField("logger");

            Long offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
            putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
        } catch (Exception ignored) {
            // Just tried
        }
    }

}
