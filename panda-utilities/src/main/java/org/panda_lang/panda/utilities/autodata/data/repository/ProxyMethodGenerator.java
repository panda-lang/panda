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

import org.panda_lang.panda.utilities.autodata.AutomatedDataException;
import org.panda_lang.panda.utilities.autodata.data.collection.DataCollection;
import org.panda_lang.panda.utilities.autodata.data.entity.EntityModel;
import org.panda_lang.panda.utilities.autodata.data.query.DataQuery;
import org.panda_lang.panda.utilities.autodata.data.query.DataQueryFactory;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.CamelCaseUtils;
import org.panda_lang.panda.utilities.commons.function.ThrowingConsumer;
import org.panda_lang.panda.utilities.commons.function.ThrowingFunction;

import java.lang.reflect.Method;
import java.util.List;

final class ProxyMethodGenerator {

    private static final DataQueryFactory QUERY_FACTORY = new DataQueryFactory();

    protected ProxyMethod generateMethod(DataController controller, DataCollection collection, RepositoryModel repositoryModel, Method method) {
        List<String> elements = CamelCaseUtils.split(method.getName(), String::toLowerCase);
        RepositoryOperation operation = RepositoryOperation.of(elements.get(0));

        if (operation == null) {
            throw new AutomatedDataException("Unknown operation: '" + elements.get(0) + "' (source: " + method.toGenericString() + ")");
        }

        return new ProxyMethod(operation, generate(controller, collection, repositoryModel, method, operation));
    }

    private MethodFunction generate(DataController controller, DataCollection collection, RepositoryModel repositoryModel, Method method, RepositoryOperation operation) {
        DataHandler handler = controller.getHandler(collection.getName());
        EntityModel entityModel = repositoryModel.getCollectionScheme().getEntityModel();

        switch (operation) {
            case CREATE:
                return createFunction(handler);
            case DELETE:
                return deleteFunction(handler);
            case FIND:
                return findFunction(handler, entityModel, method);
            default:
                throw new AutomatedDataException("Unsupported operation: " + operation);
        }
    }

    private MethodFunction createFunction(DataHandler handler) {
        return handler::create;
    }

    @SuppressWarnings("unchecked")
    private MethodFunction deleteFunction(DataHandler handler) {
        return parameters -> {
            ArrayUtils.forEachThrowing(parameters, (ThrowingConsumer<Object, Exception>) handler::delete);
            return null;
        };
    }

    private MethodFunction findFunction(DataHandler handler, EntityModel scheme, Method method) {
        DataQuery query = QUERY_FACTORY.create(scheme, method);
        return parameters -> handler.find(query, parameters);
    }

    private interface MethodFunction extends ThrowingFunction<Object[], Object, Exception> { }

}
