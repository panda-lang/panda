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

import org.panda_lang.panda.utilities.autodata.data.collection.CollectionModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

final class ProxyInvocationHandler implements InvocationHandler {

    private final CollectionModel collectionModel;
    private final Map<String, ProxyMethod> generatedFunctions = new HashMap<>();

    ProxyInvocationHandler(CollectionModel collectionModel) {
        this.collectionModel = collectionModel;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        if (method.getName().equals("toString")) {
            return collectionModel.getRepositoryClass() + "::" + collectionModel.getName();
        }

        if (method.getName().equals("equals") && args.length == 1){
            return proxy == args[0];
        }

        ProxyMethod function = generatedFunctions.get(method.getName());

        if (function == null) {
            return null; // or throw?
        }

        return function.apply(args);
    }

    protected void addFunctions(Map<String, ProxyMethod> functions) {
        generatedFunctions.putAll(functions);
    }

}
