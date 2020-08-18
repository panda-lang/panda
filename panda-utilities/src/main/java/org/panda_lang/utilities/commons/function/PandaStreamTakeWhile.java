/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.utilities.commons.function;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

final class PandaStreamTakeWhile<T> implements Spliterator<T> {

    private final Spliterator<T> source;
    private final Predicate<T> condition;
    private boolean conditionHolds;

    PandaStreamTakeWhile(Spliterator<T> source, Predicate<T> condition) {
        this.source = source;
        this.condition = condition;
        this.conditionHolds = true;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return conditionHolds && source.tryAdvance((e) -> {
            if (condition.test(e)) {
                action.accept(e);
             } else {
                conditionHolds = false;
            }
        });
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return 0;
    }

    @Override
    public int characteristics() {
        return 0;
    }

}
