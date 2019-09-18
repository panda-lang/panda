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

package org.panda_lang.framework.language.resource.syntax.sequence;

import org.panda_lang.utilities.commons.ReflectionUtils;

import java.util.Collection;

/**
 * Default sequences
 */
public final class Sequences {

    public static final Sequence STRING = new Sequence("String", '"');

    public static final Sequence RAW_STRING = new Sequence("String", "'");

    public static final Sequence LINE_ORIENTED_COMMENT = new Sequence("Comment", "//", "\n");

    public static final Sequence BLOCK_ORIENTED_COMMENT = new Sequence("Comment", "/*", "*/");

    public static final Sequence DOCUMENTATION_ORIENTED_COMMENT = new Sequence("Documentation", "/**", "*/");

    private static final Collection<Sequence> VALUES;

    static {
        VALUES = ReflectionUtils.getStaticFieldValues(Sequences.class, Sequence.class);
    }

    private Sequences() { }

    public static Sequence[] values() {
        return VALUES.toArray(new Sequence[0]);
    }

}
