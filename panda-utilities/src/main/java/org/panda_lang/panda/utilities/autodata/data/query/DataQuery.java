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

import java.util.Collection;

public interface DataQuery<TYPE> {

    default DataQueryCategory getCategory(DataQueryCategoryType category) {
        return getCategory(category.getName());
    }

    DataQueryCategory getCategory(String category);

    Collection<? extends DataQueryCategory> getCategories();

    Class<TYPE> getExpectedReturnType();

}
