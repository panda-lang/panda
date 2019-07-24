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

package org.panda_lang.panda.utilities.autodata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.utilities.autodata.data.entity.DataEntity;
import org.panda_lang.panda.utilities.autodata.defaults.sql.SQLDataController;
import org.panda_lang.panda.utilities.autodata.defaults.sql.SQLRepository;
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

final class SQLAutomatedDataSpaceTest {

    @Test
    void testSQL() {
        SQLDataController sqlController = new SQLDataController();

        AutomatedDataSpace space = AutomatedDataSpace.initialize(sqlController)
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
    interface UserRepository extends SQLRepository<User> {

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
