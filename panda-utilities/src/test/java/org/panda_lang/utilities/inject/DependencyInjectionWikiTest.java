/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.utilities.inject;

import org.junit.jupiter.api.Test;
import org.panda_lang.utilities.inject.annotations.Inject;
import org.panda_lang.utilities.inject.annotations.Injectable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;

final class DependencyInjectionWikiTest {

    @Test
    void testWikiExample() throws Exception {
        Injector injector = DependencyInjection.createInjector(resources -> {
            resources.annotatedWith(AwesomeRandom.class).assignHandler((expectedType, annotation) -> {
                if (expectedType.equals(String.class)) {
                    return UUID.randomUUID().toString();
                }

                if (expectedType.equals(UUID.class)) {
                    return UUID.randomUUID();
                }

                throw new IllegalArgumentException("Unsupported type " + expectedType);
            });
        });

        Entity entityA = injector.newInstance(Entity.class);
        Entity entityB = injector.newInstance(Entity.class);

        System.out.println(entityA.getId());
        System.out.println(entityB.getId());
    }

    @Injectable // mark annotation as DI ready annotation
    @Retention(RetentionPolicy.RUNTIME) // make sure that the annotation is visible at runtime
    @interface AwesomeRandom { }

    private static final class Entity {
        private final UUID id;

        @Inject //it's not required, but it might be useful to disable "unused method" warnings/scanning for annotations
        private Entity(@AwesomeRandom UUID random) {
            this.id = random;
        }

        public UUID getId() {
            return id;
        }
    }

}
