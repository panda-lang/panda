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

package org.panda_lang.panda.language.resource.syntax.type;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.type.Signature;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameterImpl;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SnippetUtils;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ParameterParser implements Parser {

    private static final SignatureParser SIGNATURE_PARSER = new SignatureParser();

    @Override
    public String name() {
        return "parameter";
    }

    public List<PropertyParameter> parse(Context<?> context, @Nullable Snippet snippet) {
        if (SnippetUtils.isEmpty(snippet)) {
            return Collections.emptyList();
        }

        List<PropertyParameter> parameters = new ArrayList<>();
        PandaSourceReader sourceReader = new PandaSourceReader(snippet.toStream());

        while (sourceReader.hasUnreadSource()) {
            boolean mutable = sourceReader.optionalRead(() -> sourceReader.read(Keywords.MUT)).isDefined();
            boolean nillable = sourceReader.optionalRead(() -> sourceReader.read(Keywords.NIL)).isDefined();

            Signature signature = sourceReader.readSignature()
                    .map(signatureSource -> SIGNATURE_PARSER.parse(context, signatureSource))
                    .orThrow(() -> new PandaParserFailure(context, snippet, "Missing parameter signature"));

            String name = sourceReader.read(TokenTypes.UNKNOWN)
                    .map(TokenInfo::getValue)
                    .orThrow(() -> new PandaParserFailure(context, snippet, "Missing parameter name"));

            parameters.add(new PropertyParameterImpl(parameters.size(), signature, name, mutable, nillable));

            if (sourceReader.optionalRead(() -> sourceReader.read(Separators.COMMA)).isEmpty()) {
                break;
            }
        }

        if (sourceReader.hasUnreadSource()) {
            throw new PandaParserFailure(context, sourceReader.getStream().readLineResidue(), "Unknown source provided as parameter");
        }

        return parameters;
    }

}
