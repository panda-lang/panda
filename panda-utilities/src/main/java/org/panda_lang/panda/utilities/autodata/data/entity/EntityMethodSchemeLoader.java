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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.autodata.AutomatedDataException;
import org.panda_lang.panda.utilities.commons.CamelCaseUtils;
import org.panda_lang.panda.utilities.commons.annotations.Annotations;
import org.panda_lang.panda.utilities.commons.text.ContentJoiner;

import java.lang.reflect.Method;
import java.util.List;

final class EntityMethodSchemeLoader {

    protected EntityMethodScheme load(Method method) {
        List<String> elements = CamelCaseUtils.split(method.getName(), String::toLowerCase);

        String propertyName = ContentJoiner.on("_").join(elements.subList(1, elements.size())).toString();
        EntityMethodType operationType = EntityMethodType.of(elements.get(0));

        if (operationType == null) {
            throw new AutomatedDataException("Unknown operation '" + elements.get(0) + "'");
        }

        EntityProperty property = new EntityProperty(propertyName, getType(operationType, method), new Annotations(method.getAnnotations()), method);
        return new EntityMethodScheme(method, property, operationType);
    }

    private @Nullable Class<?> getType(EntityMethodType operationType, Method method) {
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
