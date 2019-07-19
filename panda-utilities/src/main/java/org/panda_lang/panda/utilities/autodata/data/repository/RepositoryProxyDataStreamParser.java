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

package org.panda_lang.panda.utilities.autodata.data.repository;

import org.panda_lang.panda.utilities.autodata.data.entity.EntityScheme;
import org.panda_lang.panda.utilities.autodata.data.stream.DataStream;
import org.panda_lang.panda.utilities.commons.text.ContentJoiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

final class RepositoryProxyDataStreamParser {

    public DataStream parse(EntityScheme entityScheme, List<String> query) {
        List<String> categories = Arrays.asList("what", "by", "order");

        Map<String, List<String>> data = mapQueryToCategories(categories, query);
        Map<String, List<String>> convertedData = convertData(entityScheme, data);

        System.out.println("converted:");
        convertedData.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });


        return null;
    }

    private Map<String, List<String>> convertData(EntityScheme entityScheme, Map<String, List<String>> data) {
        Map<String, List<String>> convertedData = new HashMap<>(data.size());
        Set<String> properties = entityScheme.getProperties().keySet();

        data.forEach((key, value) -> {
            List<String> convertedValues = new ArrayList<>(value.size());

            List<List<String>> prepared = split(value, element -> element.equals("and") || element.equals("or"));
            prepared.forEach(list -> convertedValues.add(ContentJoiner.on("_").join(list).toString()));

            convertedData.put(key, convertedValues);
        });

        return convertedData;
    }

    private Map<String, List<String>> mapQueryToCategories(List<String> categories, List<String> query) {
        Map<String, List<String>> values = new HashMap<>(categories.size());
        String currentCategory = categories.get(0);
        int amount = 1;

        for (String spec : query) {
            int index = categories.indexOf(spec);

            if (index > 0 && index > categories.indexOf(currentCategory) && amount > 0) {
                currentCategory = spec;
                amount = 0;
                continue;
            }

            values.computeIfAbsent(currentCategory, key -> new ArrayList<>(4)).add(spec);
            amount++;
        }

        System.out.println("--- query: " + query);

        values.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });

        System.out.println("all: " + values.get("what").contains("all"));
        return values;
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
