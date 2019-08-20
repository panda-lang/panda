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

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class UnsafeUtils {

    private static Unsafe unsafe;

    private UnsafeUtils() { }

    /**
     * Get access to the Unsafe instance
     *
     * @return the unsafe
     */
    public static Unsafe getUnsafe() {
        if (unsafe == null) {
            try {
                Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafeField.setAccessible(true);
                unsafe = (Unsafe) theUnsafeField.get(null);
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return unsafe;
    }

}
