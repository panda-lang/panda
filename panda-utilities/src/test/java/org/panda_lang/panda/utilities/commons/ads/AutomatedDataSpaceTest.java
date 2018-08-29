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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.utilities.autodata.AutomatedDataInterface;
import org.panda_lang.panda.utilities.autodata.AutomatedDataSpace;
import org.panda_lang.panda.utilities.autodata.collection.ADSCollectionHandler;
import org.panda_lang.panda.utilities.autodata.collection.ADSCollectionService;
import org.panda_lang.panda.utilities.autodata.database.ADSDatabaseRepository;

import java.util.HashMap;
import java.util.Map;

public class AutomatedDataSpaceTest {

    @Test
    public void test() {
        AutomatedDataSpace pde = AutomatedDataSpace.builder()
                .createCollection()
                .name("strings")
                .type(String.class)
                .service(new StringCollectionService())
                .handler(new ADSCollectionHandler<StringCollectionService, String, Integer>() {
                    @Override
                    public void save(StringCollectionService service, String element) {
                        service.put(element);
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
                .createDatabase()
                .name("strings")
                .repository(new StringDatabaseRepository())
                .append()
                .build();

        AutomatedDataInterface dataInterface = pde.createInterface();
        dataInterface.post("strings", "var");
        dataInterface.loadAll();

        Assertions.assertNull(dataInterface.get("strings", String.class, "var"));
        Assertions.assertEquals(dataInterface.get("strings", String.class, "var".hashCode()), "var");
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

    public static class StringDatabaseRepository implements ADSDatabaseRepository<String> {

    }

}
