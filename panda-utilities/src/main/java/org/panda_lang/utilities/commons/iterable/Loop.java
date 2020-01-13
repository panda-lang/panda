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

package org.panda_lang.utilities.commons.iterable;

import java.util.function.BiConsumer;

public final class Loop<T> {

    private final Iterable<T> source;
    private BiConsumer<LoopResult, T> loopSource;

    private Loop(Iterable<T> source) {
        this.source = source;
    }

    public Loop<T> loop(BiConsumer<LoopResult, T> loopSource) {
        this.loopSource = loopSource;
        return this;
    }

    public LoopResult run() {
        LoopResult result = new LoopResult();

        for (T element : source) {
            loopSource.accept(result, element);

            if (result.isBroke()) {
                break;
            }
        }

        return result;
    }

    public static <T> Loop<T> of(Iterable<T> source) {
        return new Loop<>(source);
    }

    public static final class LoopResult {

        private boolean broke;

        public void breakLoop() {
            this.broke = true;
        }

        public void breakLoop(boolean flag) {
            this.broke = flag;
        }

        public boolean isBroke() {
            return broke;
        }

    }

}
