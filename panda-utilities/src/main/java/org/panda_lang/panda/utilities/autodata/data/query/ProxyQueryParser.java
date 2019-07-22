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

import org.panda_lang.panda.utilities.autodata.data.entity.EntityModel;
import org.panda_lang.panda.utilities.autodata.data.entity.Property;
import org.panda_lang.panda.utilities.commons.CamelCaseUtils;
import org.panda_lang.panda.utilities.commons.collection.Lists;
import org.panda_lang.panda.utilities.commons.collection.Pair;
import org.panda_lang.panda.utilities.commons.text.ContentJoiner;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

final class ProxyQueryParser {

    private static final List<DataQueryCategoryType> CATEGORIES = Arrays.asList(DataQueryCategoryType.values());

    private static final String AND = "and";
    private static final String OR = "or";

    protected DataQuery parse(EntityModel scheme, Method method) {
        List<String> query = Lists.subList(CamelCaseUtils.split(method.getName(), String::toLowerCase), 1);
        Parameter[] parameters = method.getParameters();

        Map<DataQueryCategoryType, List<String>> data = toCategories(query);
        Map<String, DataQueryCategory> queryData = toQueryData(scheme, data);

        return new ProxyQuery(method.getReturnType(), queryData);
    }

    private Map<String, DataQueryCategory> toQueryData(EntityModel scheme, Map<DataQueryCategoryType, List<String>> data) {
        Map<String, DataQueryCategory> queryData = new HashMap<>();
        AtomicInteger index = new AtomicInteger();

        data.forEach((key, value) -> {
            List<DataQueryRuleScheme> rules = Arrays.stream(Lists.split(value, OR))
                    .map(elementSource -> toRule(scheme, elementSource, index))
                    .collect(Collectors.toList());

            queryData.put(key.getName(), new ProxyQueryCategory(key, rules));
        });

        return queryData;
    }

    private ProxyQueryRuleScheme toRule(EntityModel scheme, List<String> rules, AtomicInteger index) {
        return new ProxyQueryRuleScheme(rules.stream()
                .filter(property -> !property.equals(AND))
                .map(property -> {
                    Optional<Property> propertyValue = scheme.getProperty(property);
                    return new ProxyQueryRuleProperty(propertyValue.isPresent() ? propertyValue.get() : property);
                })
                .map(property -> new Pair<>(property, property.isEntityProperty() ? index.getAndIncrement() : -1))
                .collect(Collectors.toList()));
    }

    private Map<DataQueryCategoryType, List<String>> toCategories(List<String> query) {
        Map<DataQueryCategoryType, List<String>> data = new TreeMap<>(Comparator.comparingInt(Enum::ordinal));
        DataQueryCategoryType currentCategory = CATEGORIES.get(0);
        int amount = 1;

        for (String element : query) {
            DataQueryCategoryType category = DataQueryCategoryType.of(element);
            int index = CATEGORIES.indexOf(category);

            if (index > 0 && index > CATEGORIES.indexOf(currentCategory) && amount > 0) {
                currentCategory = category;
                amount = 0;
                continue;
            }

            data.computeIfAbsent(currentCategory, key -> new ArrayList<>(4)).add(element);
            amount++;
        }

        Map<DataQueryCategoryType, List<String>> convertedData = new HashMap<>(data.size());

        data.forEach((key, value) -> {
            List<String> convertedValues = new ArrayList<>(value.size());

            List<List<String>> prepared = ProxyQueryParserUtils.split(value, element -> element.equals(AND) || element.equals(OR));
            prepared.forEach(list -> convertedValues.add(ContentJoiner.on("_").join(list).toString()));

            convertedData.put(key, convertedValues);
        });

        return convertedData;
    }

}
