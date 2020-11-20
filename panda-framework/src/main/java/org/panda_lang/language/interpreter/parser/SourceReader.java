package org.panda_lang.language.interpreter.parser;

import org.panda_lang.language.interpreter.source.Localizable;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.PandaSnippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.Token;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.interpreter.token.TokenType;
import org.panda_lang.language.interpreter.token.TokenUtils;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.auxiliary.Section;
import org.panda_lang.utilities.commons.collection.FixedStack;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.function.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SourceReader implements Localizable {

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
                Section currentSection = new Section(sectionData.getKey(), PandaSnippet.ofImmutable(sectionData.getValue()), token);

                if (stack.isEmpty()) {
                    section = currentSection;
                    break;
                }
            }
            else {
                stack.peek().getValue().add(token);
            }
        }

        if (section == null) {
            stream.unread(maxLength);
            return Option.none();
        }

        return Option.of(section);
    }

    @Override
    public Location toLocation() {
        return stream.toLocation();
    }

    public SourceStream getStream() {
        return stream;
    }

}
