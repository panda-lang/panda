package org.panda_lang.panda.language.interpreter.parser;

import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.SourceReader;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.token.PandaSnippet;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.auxiliary.Section;
import org.panda_lang.language.resource.syntax.operator.Operators;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.resource.syntax.type.SignatureParser;
import org.panda_lang.panda.language.resource.syntax.type.SignatureSource;
import org.panda_lang.utilities.commons.function.Option;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PandaSourceReader extends SourceReader {

    private static final SignatureParser SIGNATURE_PARSER = new SignatureParser();

    public PandaSourceReader(SourceStream stream) {
        super(stream);
    }

    public Option<Snippet> readBody() {
        return readSection(Separators.BRACE_LEFT)
                .map(token -> token.toToken(Section.class).getContent());
    }

    public Option<Snippet> readArguments() {
        return readSection(Separators.PARENTHESIS_LEFT)
                .map(token -> token.toToken(Section.class).getContent());
    }

    public Option<SignatureSource> readSignature() {
        return read(token -> token.getType() == TokenTypes.UNKNOWN && !Character.isDigit(token.getValue().charAt(0)))
                .flatMap(name -> optionalRead(this::readGenerics)
                    .map(section -> SIGNATURE_PARSER.readSignatures(section.getContent()))
                    .orElse(Collections.emptyList())
                    .map(generics -> new SignatureSource(name, generics))
                );
    }

    public Option<Section> readGenerics() {
        return readBetween(Operators.ANGLE_LEFT, Operators.ANGLE_RIGHT);
    }

    public Option<Snippet> readPandaQualifier() {
        List<TokenInfo> tokens = new ArrayList<>();
        boolean nextAny = false;

        while (super.stream.hasUnreadSource()) {
            boolean any = nextAny;
            nextAny = false;

            Option<TokenInfo> result = optionalRead(() -> read(read -> read.getType() == TokenTypes.UNKNOWN || (any && read.getType() == TokenTypes.KEYWORD)));

            if (result.isEmpty()) {
                result = optionalRead(() -> read(read -> read.contentEquals(Operators.SUBTRACTION)
                        || read.contentEquals(Separators.PERIOD)
                        || read.contentEquals(Operators.COLON)));
                nextAny = true;
            }

            result.peek(tokens::add);

            if (result.isEmpty()) {
                break;
            }
        }

        if (tokens.isEmpty()) {
            return Option.none();
        }

        return Option.of(PandaSnippet.ofImmutable(tokens));
    }

    public List<ExpressionTransaction> readExpressions(Context<?> context) {
        ExpressionParser parser = context.getExpressionParser();
        List<ExpressionTransaction> transactions = new ArrayList<>();

        while (super.stream.hasUnreadSource()) {
            Option<ExpressionTransaction> argument = optionalRead(() -> parser.parseSilently(context, super.stream));

            if (argument.isEmpty()) {
                break;
            }

            transactions.add(argument.get());

            if (super.stream.hasUnreadSource()) {
                if (!super.stream.getCurrent().equals(Separators.COMMA)) {
                    break;
                }

                super.stream.read();
            }
        }

        return transactions;
    }

}
