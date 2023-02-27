package panda.interpreter.lexer;

import panda.interpreter.token.Comment;
import panda.interpreter.token.Comment.CommentType;
import panda.interpreter.token.Comment.CommentValue;
import panda.interpreter.token.Identifier;
import panda.interpreter.token.Indentation;
import panda.interpreter.token.Keyword;
import panda.interpreter.token.Keyword.KeywordType;
import panda.interpreter.token.Literal;
import panda.interpreter.token.Literal.LiteralValue;
import panda.interpreter.token.Literal.LiteralType;
import panda.interpreter.token.Operator;
import panda.interpreter.token.Operator.OperatorType;
import panda.interpreter.token.Separator;
import panda.interpreter.token.Separator.SeparatorType;
import panda.interpreter.token.Token;

public class LexicalStream extends StringStream {

    private static final int INDENTATION_SIZE = 4;

    public LexicalStream(String source) {
        super(source.toCharArray());
    }

    public Token<?> read() {
        var line = super.getLine();
        var caret = super.getCaret();

        if (caret == -1) {
            return new Indentation(readIndentation(), line, caret);
        }

        var symbol = peek();

        if (Character.isWhitespace(symbol)) {
            next();
            return read();
        }

        if (Character.isDigit(symbol)) {
            return new Literal(readDigit(), line, caret);
        }

        for (SeparatorType type : SeparatorType.getValues()) {
            if (type.getSymbol() == symbol) {
                next();
                return new Separator(type, line, caret);
            }
        }

        switch (symbol) {
            case '"', '\'', '`': return new Literal(readString(), line, caret);
        }

        var figure = hasContent(1)
            ? Character.toString(symbol) + peek(1)
            : Character.toString(symbol);

        switch (figure) {
            case "/*", "//": return new Comment(readComment(), line, caret);
        }

        for (OperatorType type : OperatorType.getValues()) {
            if (type.getOperator().equals(figure)) {
                next(figure.length());
                return new Operator(type, line, caret);
            }
        }

        for (OperatorType type : OperatorType.getValues()) {
            if (type.getOperator().equals(String.valueOf(symbol))) {
                next();
                return new Operator(type, line, caret);
            }
        }

        var identifier = readIdentifier();

        for (KeywordType type : KeywordType.getValues()) {
            if (type.getName().equals(identifier)) {
                return new Keyword(type, line, caret);
            }
        }

        return new Identifier(identifier, line, caret);
    }

    private LiteralValue readString() {
        var stringBuilder = new StringBuilder();
        var operator = next();
        var closed = false;

        while (hasContent()) {
            var next = next();

            if (next == operator) {
                closed = true;
                break;
            }

            stringBuilder.append(next);
        }

        if (!closed) {
            throw new IllegalStateException("Cannot read string, closing operator " + operator + " was not found at the end of literal. " + getLocation());
        }

        var type = switch (operator) {
            case '`' -> LiteralType.RAW_STRING;
            case '"' -> LiteralType.QUOTED_STRING;
            case '\'' -> LiteralType.STRING;
            default -> throw new IllegalStateException("Unknown string operator" + operator);
        };

        return new LiteralValue(type, stringBuilder.toString());
    }

    private Integer readIndentation() {
        setCaret(0);
        var level = 0;

        monke: while (hasContent()) {
            switch (peek()) {
                case ' ' -> level++;
                case '\t' -> level += INDENTATION_SIZE;
                default -> { break monke; }
            }
            next();
        }

        return level / INDENTATION_SIZE;
    }

    private String readIdentifier() {
        var identifierBuilder = new StringBuilder();

        while (hasContent()) {
            var symbol = peek();
            if (Character.isAlphabetic(symbol) || symbol == '_' || (identifierBuilder.length() > 0 && Character.isDigit(symbol))) {
                identifierBuilder.append(next());
            }
            else break;
        }

        if (identifierBuilder.isEmpty()) {
            throw new IllegalStateException("Cannot read current source as identifier: " + readRemainingLine() + " " + getLocation());
        }

        return identifierBuilder.toString();
    }

    private LiteralValue readDigit() {
        var digit = new StringBuilder();

        while (hasContent()) {
            var symbol = peek();
            if (Character.isDigit(symbol) || (digit.length() > 0 && (symbol == '.' || symbol == '_' || symbol == 'x'))) {
                digit.append(next());
            }
            else break;
        }

        if (digit.isEmpty()) {
            throw new IllegalStateException("Cannot read current source as digit: " + readRemainingLine() + " " + getLocation());
        }

        return new LiteralValue(LiteralType.DECIMAL, digit.toString());
    }

    private CommentValue readComment() {
        String operator = next() + "" + next();

        return switch (operator) {
            case "//" -> new CommentValue(CommentType.SINGLE_LINE, readRemainingLine());
            case "/*" -> new CommentValue(CommentType.MULTILINE, readTo("*/"));
            default -> throw new IllegalStateException("Unknown comment type: " + operator + " " + getLocation());
        };
    }

}
