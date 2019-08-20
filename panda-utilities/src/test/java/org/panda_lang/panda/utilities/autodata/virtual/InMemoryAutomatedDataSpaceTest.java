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

package org.panda_lang.panda.utilities.autodata.virtual;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.utilities.autodata.AutomatedDataSpace;
import org.panda_lang.panda.utilities.autodata.data.collection.DataCollection;
import org.panda_lang.panda.utilities.autodata.data.entity.DataEntity;
import org.panda_lang.panda.utilities.autodata.data.transaction.DataTransaction;
import org.panda_lang.panda.utilities.autodata.defaults.virtual.InMemoryDataController;
import org.panda_lang.panda.utilities.autodata.defaults.virtual.InMemoryDataRepository;
import org.panda_lang.panda.utilities.autodata.orm.As;
import org.panda_lang.panda.utilities.autodata.orm.Berry;
import org.panda_lang.panda.utilities.autodata.orm.Generated;
import org.panda_lang.panda.utilities.autodata.orm.Id;
import org.panda_lang.panda.utilities.autodata.stereotype.Entity;
import org.panda_lang.panda.utilities.autodata.stereotype.Repository;
import org.panda_lang.panda.utilities.autodata.stereotype.Service;
import org.panda_lang.panda.utilities.inject.annotations.Autowired;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

final class InMemoryAutomatedDataSpaceTest {

    @Test
    void test() {
        AutomatedDataSpace space = AutomatedDataSpace.initialize(new InMemoryDataController())
                .createCollection()
                    .name("users")
                    .entity(User.class)
                    .service(UserService.class)
                    .repository(UserRepository.class)
                    .append()
                .createCollection()
                    .name("special-users")
                    .entity(User.class)
                    .service(SpecialUserService.class)
                    .repository(UserRepository.class)
                    .append()
                .collect();

        DataCollection collection = space.getCollection("users");
        UserService service = collection.getService(UserService.class);

        User user = service.createUser("onlypanda");
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals("onlypanda", user.getName());

        user.setName("updated panda");
        Assertions.assertEquals("updated panda", user.getName());

        Optional<User> foundByUser = service.findUserByName("updated panda");
        Assertions.assertTrue(foundByUser.isPresent());
        Assertions.assertEquals("updated panda", foundByUser.get().getName());
        Assertions.assertEquals(user.getId(), foundByUser.get().getId());

        User foundById = service.findUserByNameOrId("fake username", user.getId());
        Assertions.assertEquals(user.getId(), foundById.getId());

        AtomicBoolean succeed = new AtomicBoolean(false);
        DataTransaction transaction = user.transaction(() -> {
                    user.setName("variant panda");
                    user.setName("transactional panda");
                })
                .success((attempt, time) -> {
                    succeed.set(true);
                });
        Assertions.assertEquals("updated panda", user.getName());

        transaction.commit();
        Assertions.assertEquals("transactional panda", user.getName());
        Assertions.assertTrue(succeed.get());
    }

    @Service
    static class SpecialUserService {

        @Autowired
        public SpecialUserService(UserService service, @Berry("special-users") UserRepository repository) {
            Assertions.assertNotEquals(repository, service.repository);
        }

    }

    @Service
    static class UserService {

        private final UserRepository repository;

        @Autowired
        public UserService(@Berry("users") UserRepository repository) {
            this.repository = repository;
        }

        public User createUser(String name) {
            return repository.createUser(name);
        }

        public Optional<User> findUserByName(String name) {
            return repository.findUserByName(name);
        }

        public User findUserByNameOrId(String name, UUID id) {
            return repository.findByNameOrId(name, id);
        }

    }

    @Repository
    interface UserRepository extends InMemoryDataRepository<User> {

        User createUser(@As("name") String name);

        Optional<User> findUserByName(String name);

        User findByNameOrId(String name, UUID id);

    }

    @Entity
    public interface User extends DataEntity {

        void setName(String name);

        String getName();

        @Id
        @Generated
        UUID getId();

    }

}
