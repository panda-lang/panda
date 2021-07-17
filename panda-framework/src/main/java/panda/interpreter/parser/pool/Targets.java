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

package panda.interpreter.parser.pool;

import panda.interpreter.architecture.type.TypeContext;
import panda.interpreter.parser.Component;

/**
 * Default pipelines used by the framework
 */
public final class Targets {

    /**
     * All pipelines
     */
    public static final Component<Object> ALL = new Component<>("all", Object.class);

    /**
     * Head pipeline
     */
    public static final Component<Object> HEAD = new Component<>("head", Object.class);

    /**
     * Class type parsers, used by type parser
     */
    public static final Component<TypeContext> TYPE = new Component<>("type", TypeContext.class);

    /**
     * Scope parsers
     */
    public static final Component<Object> SCOPE = new Component<>("scope", Object.class);

}
