package panda.interpreter.lexer;

import panda.interpreter.token.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TokenStream {

    private final List<Token<?>> source;
    private int index = 0;

    public TokenStream(List<Token<?>> source) {
        this.source = source;
    }

    public boolean hasNext() {
        return index < source.size();
    }

    public Token<?> preview() {
        return source.get(index);
    }

    public Token<?> read() {
        return source.get(index++);
    }

    @SuppressWarnings("unchecked")
    public <T extends Token<?>> T read(Class<T> type) {
        return (T) read();
    }

    public List<Token<?>> read(Predicate<Token<?>> condition) {
        var tokens = new ArrayList<Token<?>>();

        while (hasNext()) {
            var preview = preview();

            if (!condition.test(preview)) {
                break;
            }

            tokens.add(read());
        }

        return tokens;
    }

}
