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

package org.panda_lang.panda.utilities.autodata.sql;

import org.panda_lang.panda.utilities.autodata.orm.Berry;
import org.panda_lang.panda.utilities.autodata.stereotype.Service;
import org.panda_lang.panda.utilities.inject.annotations.Autowired;

import java.util.Optional;
import java.util.UUID;

@Service
class UserService {

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