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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CollectionUtils {

    private CollectionUtils() { }

    /**
     * Join collections into the list
     *
     * @param collections collections to merge
     * @param <T>         generic type of the collections
     * @return the list with the specified collections
     */
    @SafeVarargs
    public static <T> List<T> listOf(Collection<? extends T>... collections) {
        List<T> list = new ArrayList<>();

        for (Collection<? extends T> collection : collections) {
            list.addAll(collection);
        }

        return list;
    }

}
