package panda.interpreter.token;

import panda.interpreter.token.Comment.CommentValue;

public final class Comment extends Token<CommentValue> {

    public enum CommentType {
        SINGLE_LINE,
        MULTILINE,
        DOCUMENTATION
    }

    public record CommentValue(CommentType type, String content) {
    }

    public Comment(CommentValue value, int line, int caret) {
        super(TokenType.COMMENT, value, line, caret);
    }

    @Override
    public String toSourceString() {
        return switch (value.type) {
            case SINGLE_LINE -> "//" + value.content;
            case MULTILINE -> "/*" + value.content + "*/";
            case DOCUMENTATION -> "/**" + value.content + "*/";
        };
    }

}
