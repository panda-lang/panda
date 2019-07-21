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

import org.panda_lang.panda.utilities.autodata.AutomatedDataException;
import org.panda_lang.panda.utilities.autodata.data.entity.EntityScheme;
import org.panda_lang.panda.utilities.commons.CamelCaseUtils;
import org.panda_lang.panda.utilities.commons.collection.Lists;
import org.panda_lang.panda.utilities.commons.text.ContentJoiner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

final class ProxyQueryParser {

    private static final List<ProxyQueryCategoryType> CATEGORIES = Arrays.asList(ProxyQueryCategoryType.values());

    protected DataQuery parse(EntityScheme scheme, Method method) {
        List<String> query = Lists.subList(CamelCaseUtils.split(method.getName(), String::toLowerCase), 1);

        Map<ProxyQueryCategoryType, List<String>> data = toCategories(query);
        Map<String, DataQueryCategory> queryData = toQueryData(scheme, data);

        return new ProxyQuery(queryData);
    }

    private Map<String, DataQueryCategory> toQueryData(EntityScheme scheme, Map<ProxyQueryCategoryType, List<String>> data) {
        Map<String, DataQueryCategory> queryData = new HashMap<>();

        data.forEach((key, value) -> {
            DataQueryCategory category = new ProxyQueryCategory(key, Arrays.stream(Lists.split(value, "or"))
                    .map(elementSource -> elementSource.stream()
                            .filter(property -> !property.equals("and"))
                            .map(scheme::getProperty)
                            .map(property -> {
                                if (!property.isPresent()) {
                                    throw new AutomatedDataException("Unknown property: " + property);
                                }

                                return property.get();
                            })
                            .collect(Collectors.toList()))
                    .map(ProxyQueryElement::new)
                    .collect(Collectors.toList()));

            queryData.put(key.getName(), category);
        });

        return queryData;
    }

    private Map<ProxyQueryCategoryType, List<String>> toCategories(List<String> query) {
        Map<ProxyQueryCategoryType, List<String>> values = new HashMap<>(CATEGORIES.size());
        ProxyQueryCategoryType currentCategory = CATEGORIES.get(0);
        int amount = 1;

        for (String element : query) {
            ProxyQueryCategoryType category = ProxyQueryCategoryType.of(element);
            int index = CATEGORIES.indexOf(category);

            if (index > 0 && index > CATEGORIES.indexOf(currentCategory) && amount > 0) {
                currentCategory = category;
                amount = 0;
                continue;
            }

            values.computeIfAbsent(currentCategory, key -> new ArrayList<>(4)).add(element);
            amount++;
        }

        return convertData(values);
    }

    private Map<ProxyQueryCategoryType, List<String>> convertData(Map<ProxyQueryCategoryType, List<String>> data) {
        Map<ProxyQueryCategoryType, List<String>> convertedData = new HashMap<>(data.size());

        data.forEach((key, value) -> {
            List<String> convertedValues = new ArrayList<>(value.size());

            List<List<String>> prepared = split(value, element -> element.equals("and") || element.equals("or"));
            prepared.forEach(list -> convertedValues.add(ContentJoiner.on("_").join(list).toString()));

            convertedData.put(key, convertedValues);
        });

        return convertedData;
    }

    private <T> List<List<T>> split(List<T> list, Predicate<T> by) {
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
