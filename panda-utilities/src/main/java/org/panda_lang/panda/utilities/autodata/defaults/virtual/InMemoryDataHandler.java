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

package org.panda_lang.panda.utilities.autodata.defaults.virtual;

import org.panda_lang.panda.utilities.autodata.AutomatedDataException;
import org.panda_lang.panda.utilities.autodata.data.collection.DataCollection;
import org.panda_lang.panda.utilities.autodata.data.entity.Property;
import org.panda_lang.panda.utilities.autodata.data.query.DataQuery;
import org.panda_lang.panda.utilities.autodata.data.query.DataQueryCategoryType;
import org.panda_lang.panda.utilities.autodata.data.query.DataQueryRule;
import org.panda_lang.panda.utilities.autodata.data.query.DataQueryRuleScheme;
import org.panda_lang.panda.utilities.autodata.data.query.DataRuleProperty;
import org.panda_lang.panda.utilities.autodata.data.repository.DataHandler;
import org.panda_lang.panda.utilities.autodata.data.transaction.DataTransactionResult;
import org.panda_lang.panda.utilities.autodata.orm.GenerationStrategy;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.ClassUtils;
import org.panda_lang.panda.utilities.commons.collection.Lists;
import org.panda_lang.panda.utilities.commons.collection.Pair;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

final class InMemoryDataHandler<T> implements DataHandler<T> {

    private static final AtomicInteger ID = new AtomicInteger();

    private final int id;
    private final InMemoryDataController<T> controller;
    private DataCollection collection;

    InMemoryDataHandler(InMemoryDataController<T> controller) {
        this.id = ID.incrementAndGet();
        this.controller = controller;
    }

    @Override
    public T create(Object[] constructorArguments) throws Exception {
        @SuppressWarnings("unchecked")
        T value = (T) collection.getEntityClass()
                .getConstructor(ArrayUtils.mergeArrays(ArrayUtils.of(DataHandler.class), ClassUtils.getClasses(constructorArguments)))
                .newInstance(ArrayUtils.mergeArrays(new Object[] { this }, constructorArguments));

        controller.getValues().add(value);
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object generate(Class requestedType, GenerationStrategy strategy) {
        return UUID.randomUUID();
    }

    @Override
    public void save(DataTransactionResult<T> transaction) throws Exception {
        transaction.getSuccessAction().ifPresent(action -> action.accept(0, 0));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object find(DataQuery query, Object[] values) throws Exception {
        List<T> data = null;

        for (DataQueryRuleScheme scheme : query.getCategory(DataQueryCategoryType.BY).getElements()) {
            DataQueryRule rule = scheme.toRule(values);

            data = controller.getValues().stream()
                    .filter(value -> {
                        for (Pair<? extends DataRuleProperty, Object> property : rule.getProperties()) {
                            if (!property.getKey().isEntityProperty()) {
                                continue;
                            }

                            Property schemeProperty = property.getKey().getValue();

                            try {
                                Field field = value.getClass().getDeclaredField(schemeProperty.getName());

                                if (!Objects.equals(property.getValue(), field.get(value))) {
                                    return false;
                                }
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                throw new AutomatedDataException("Cannot invoke", e);
                            }
                        }

                        return true;
                    })
                    .collect(Collectors.toList());

            if (!data.isEmpty()) {
                break;
            }
        }

        if (data == null) {
            return null;
        }

        if (query.getExpectedReturnType().isAssignableFrom(data.getClass())) {
            return data;
        }

        if (Optional.class.isAssignableFrom(query.getExpectedReturnType())) {
            return Optional.ofNullable(Lists.get(data, 0));
        }

        return data.get(0);
    }

    @Override
    public void delete(T entity) {
        controller.getValues().remove(entity);
    }

    @Override
    public void handleException(Exception e) {
        System.out.println("HANDLED EXCEPTION");
        e.printStackTrace();
    }

    public void setCollection(DataCollection collection) {
        this.collection = collection;
    }

    @Override
    public String getIdentifier() {
        return controller.getIdentifier() + "::" + getClass().getSimpleName() + "-" + id;
    }

}
