/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.parser.stage;

public enum Layer {

    CURRENT_BEFORE(1),
    CURRENT_DEFAULT(2),
    CURRENT_AFTER(3),

    NEXT_BEFORE(4),
    NEXT_DEFAULT(5),
    NEXT_AFTER(6);

    private final int priority;

    Layer(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

}