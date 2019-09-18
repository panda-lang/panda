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

package org.panda_lang.panda.language.architecture.statement;

import org.panda_lang.framework.design.interpreter.pattern.descriptive.DescriptivePattern;

public final class VariableConstants {

    public static final String DECLARATION = "mut:[mut] nil:[nil] <type:reader type> <name:condition token {type:unknown}>";

    public static final DescriptivePattern DECLARATION_PATTERN = DescriptivePattern.builder()
            .compile(DECLARATION)
            .build();

}
