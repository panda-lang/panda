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

package panda.interpreter.parser;

import panda.interpreter.source.Localizable;
import panda.interpreter.source.Location;
import panda.interpreter.token.PandaSnippet;
import panda.interpreter.token.Snippet;
import panda.interpreter.token.Snippetable;
import panda.interpreter.token.SourceStream;
import panda.interpreter.token.Streamable;
import panda.interpreter.token.Token;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.token.TokenType;
import panda.interpreter.token.TokenUtils;
import panda.interpreter.resource.syntax.TokenTypes;
import panda.interpreter.resource.syntax.auxiliary.Section;
import panda.std.Pair;
import panda.utilities.collection.FixedStack;
import panda.std.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SourceReader implements Localizable, Snippetable, Streamable {

    protected final SourceStream stream;

    public SourceReader(SourceStream stream) {
        this.stream = stream;
    }

    public boolean hasUnreadSource() {
        return stream.hasUnreadSource();
    }

    public <R> Option<R> optionalRead(Supplier<Option<R>> read) {
        int unreadLength = stream.getUnreadLength();

        return read.get()
                .onEmpty(() -> stream.unread(unreadLength - stream.getUnreadLength()));
    }

    public Option<TokenInfo> read() {
        return stream.hasUnreadSource()
                ? Option.of(stream.read())
                : Option.none();
    }

    public Option<TokenInfo> read(Predicate<TokenInfo> condition) {
        return read().filter(condition);
    }

    public Option<TokenInfo> read(Token token) {
        return read(read -> read.contentEquals(token));
    }

    public Option<TokenInfo> read(TokenType type) {
        return read(token -> token.getType() == type);
    }

    public Option<TokenInfo> readVariant(Token... variants) {
        return read(token -> TokenUtils.contentEquals(token, variants));
    }

    public Option<TokenInfo> readSection(Token openingToken) {
        return read(token -> token.getType() == TokenTypes.SECTION && token.toToken(Section.class).getOpeningSeparator().contentEquals(openingToken));
    }

    public Option<Section> readBetween(Token opening, Token closing) {
        Option<TokenInfo> probeToken = read(opening);

        if (probeToken.isEmpty()) {
            return Option.none();
        }

        stream.unread(1);
        int maxLength = stream.getUnreadLength();

        FixedStack<Pair<TokenInfo, List<TokenInfo>>> stack = new FixedStack<>(16);
        Section section = null;

        for (int index = 0; index < maxLength; index++) {
            TokenInfo token = stream.read();

            if (token.contentEquals(opening)) {
                stack.push(new Pair<>(token, new ArrayList<>()));
            }
            else if (token.contentEquals(closing)) {
                Pair<TokenInfo, List<TokenInfo>> sectionData = stack.pop();
                Section currentSection = new Section(sectionData.getFirst(), PandaSnippet.ofImmutable(sectionData.getSecond()), token);

                if (stack.isEmpty()) {
                    section = currentSection;
                    break;
                }
            }
            else {
                stack.peek().getSecond().add(token);
            }
        }

        if (section == null) {
            stream.unread(maxLength);
            return Option.none();
        }

        return Option.of(section);
    }

    @Override
    public Snippet toSnippet() {
        return stream.toSnippet();
    }

    @Override
    public Location toLocation() {
        return stream.hasUnreadSource()
                ? stream.toLocation()
                : stream.getOriginalSource().toLocation();
    }

    @Override
    public SourceStream toStream() {
        return stream;
    }

    public SourceStream getStream() {
        return stream;
    }

}
