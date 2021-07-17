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

package panda.interpreter.syntax.type;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.architecture.type.member.parameter.PropertyParameter;
import panda.interpreter.architecture.type.member.parameter.PropertyParameterImpl;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.Parser;
import panda.interpreter.token.Snippet;
import panda.interpreter.token.SnippetUtils;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.resource.syntax.TokenTypes;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.resource.syntax.separator.Separators;
import panda.interpreter.syntax.PandaSourceReader;

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
                    // TODO: parent signature
                    .map(signatureSource -> SIGNATURE_PARSER.parse(context, signatureSource, false, null))
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
