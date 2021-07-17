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

package panda.interpreter.architecture.type.signature;

public enum Relation {

    DIRECT(false),
    ANY(true),
    ALSO(true);

    private final boolean wildcard;

    Relation(boolean wildcard) {
        this.wildcard = wildcard;
    }

    public boolean isWildcard() {
        return wildcard;
    }

}
