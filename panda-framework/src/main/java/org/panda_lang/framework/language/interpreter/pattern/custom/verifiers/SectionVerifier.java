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

package org.panda_lang.framework.language.interpreter.pattern.custom.verifiers;

import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomVerify;
import org.panda_lang.framework.language.interpreter.token.SynchronizedSource;
import org.panda_lang.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.framework.language.resource.syntax.separator.Separator;
import org.panda_lang.utilities.commons.ObjectUtils;

import java.util.Map;

public final class SectionVerifier implements CustomVerify<TokenInfo> {

    private final Separator separator;

    public SectionVerifier(Separator separator) {
        this.separator = separator;
    }

    @Override
    public boolean verify(Map<String, Object> results, SynchronizedSource source, TokenInfo token) {
        Section section = ObjectUtils.cast(Section.class, token.getToken());
        return section != null && section.getSeparator().equals(separator);
    }

}
