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

package org.panda_lang.panda.utilities.autodata.data.entity;

import org.panda_lang.panda.utilities.autodata.AutomatedDataException;
import org.panda_lang.panda.utilities.autodata.orm.Association;
import org.panda_lang.panda.utilities.commons.CamelCaseUtils;
import org.panda_lang.panda.utilities.commons.annotations.Annotations;
import org.panda_lang.panda.utilities.commons.text.ContentJoiner;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class MethodModelLoader {

    protected MethodModel load(Map<String, Property> properties, Method method) {
        List<String> elements = CamelCaseUtils.split(method.getName(), String::toLowerCase);

        String propertyName = ContentJoiner.on("_").join(elements.subList(1, elements.size())).toString();
        MethodType operationType = MethodType.of(elements.get(0));

        if (operationType == null) {
            throw new AutomatedDataException("Unknown operation '" + elements.get(0) + "'");
        }

        Class<?> propertyType = getType(operationType, method);
        boolean collection = false;

        if (Collection.class.isAssignableFrom(propertyType)) {
            propertyName = propertyName.substring(0, propertyName.length() - 1);
            collection = true;
        }

        if (!properties.containsKey(propertyName)) {
            Annotations annotations = new Annotations(method.getAnnotations());

            if (collection) {
                Optional<Association> associationValue = annotations.getAnnotation(Association.class);

                if (!associationValue.isPresent()) {
                    throw new AutomatedDataException("Collection based method requires @Association annotation");
                }

                Association association = associationValue.get();

                Property property = new Property(propertyName, association.type(), annotations);
                properties.put(propertyName, property);

                return new MethodModel(method, operationType, property);
            }

            Property property = new Property(propertyName, propertyType, annotations);
            properties.put(propertyName, property);

            return new MethodModel(method, operationType, property);
        }

        Property cachedProperty = properties.get(propertyName);
        cachedProperty.getAnnotations().addAnnotations(method.getAnnotations());

        if (!collection && !cachedProperty.getType().equals(propertyType)) {
            throw new AutomatedDataException("Methods associated with the same property cannot have different return type (" + method + " != type " + cachedProperty.getType() + ")");
        }

        return new MethodModel(method, operationType, cachedProperty);
    }

    private Class<?> getType(MethodType operationType, Method method) {
        switch (operationType) {
            case GET:
            case CREATE:
            case FIND:
                return method.getReturnType();
            case SET:
            case UPDATE:
            case DELETE:
                return method.getParameterTypes()[0];
            default:
                throw new AutomatedDataException("Unsupported operation '" + operationType + "'");
        }
    }

}
