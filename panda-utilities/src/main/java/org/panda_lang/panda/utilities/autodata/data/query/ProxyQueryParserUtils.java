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

package org.panda_lang.panda.utilities.autodata.data.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

final class ProxyQueryParserUtils {

    protected static <T> List<List<T>> split(List<T> list, Predicate<T> by) {
        List<List<T>> result = new ArrayList<>(list.size() / 2);
        int previousIndex = -1;

        for (int index = 0; index < list.size(); index++) {
            T element = list.get(index);

            if (!by.test(element)) {
                continue;
            }

            result.add(list.subList(previousIndex + 1, index));
            result.add(Collections.singletonList(element));
            previousIndex = index;
        }

        if (previousIndex > 0) {
            result.add(list.subList(previousIndex + 1, list.size()));
        }

        if (result.isEmpty()) {
            result.add(list);
        }

        return result;
    }


}
