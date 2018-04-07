/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.utilities.commons.ads;

import org.junit.Test;
import org.panda_lang.panda.utilities.commons.ads.collection.ADSCollectionHandler;
import org.panda_lang.panda.utilities.commons.ads.collection.ADSCollectionService;

import java.util.HashMap;
import java.util.Map;

public class AutomatedDataSpaceTest {

    @Test
    public void test() {
        AutomatedDataSpace pde = AutomatedDataSpace.builder()
                .addCollection()
                    .name("strings")
                    .type(String.class)
                    .service(new StringCollectionService())
                    .handler(new ADSCollectionHandler<StringCollectionService, String, Integer>() {
                        @Override
                        public void save(StringCollectionService service, String object) {
                            service.put(object);
                        }

                        @Override
                        public String get(StringCollectionService service, Integer query) {
                            return service.get(query);
                        }

                        @Override
                        public Class<Integer> getQueryType() {
                            return Integer.class;
                        }

                        @Override
                        public Class<String> getDataType() {
                            return String.class;
                        }
                    })
                    .append()
                .build();

        AutomatedDataInterface dataInterface = pde.createInterface();
        dataInterface.put("var");

        System.out.println(dataInterface.get(String.class, "var"));
        System.out.println(dataInterface.get(String.class, "var".hashCode()));
    }

    public static class StringCollectionService implements ADSCollectionService<String> {

        private final Map<Integer, String> strings = new HashMap<>();

        public void put(String element) {
            strings.put(element.hashCode(), element);
        }

        public String get(int hashCode) {
            return strings.get(hashCode);
        }

    }

}
