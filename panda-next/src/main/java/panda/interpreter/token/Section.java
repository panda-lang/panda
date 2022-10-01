package panda.interpreter.token;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.token.Separator.SeparatorDirection;
import panda.interpreter.token.Separator.SeparatorGroup;
import panda.interpreter.token.Separator.SeparatorType;
import panda.std.Option;
import panda.std.stream.PandaStream;
import panda.utilities.StringUtils;
import java.util.List;

public class Section extends Token<List<Token<?>>> {

    private final Option<SeparatorGroup> surroundingSeparatorsGroup;
    private final int level;

    public Section(@Nullable SeparatorGroup surroundingSeparatorsGroup, int level, List<Token<?>> tokens, int line, int caret) {
        super(TokenType.SECTION, tokens, line, caret);
        this.surroundingSeparatorsGroup = Option.of(surroundingSeparatorsGroup);
        this.level = level;
    }

    public void addToken(Token<?> token) {
        getValue().add(token);
    }

    public Option<SeparatorGroup> getSurroundingSeparatorsGroup() {
        return surroundingSeparatorsGroup;
    }

    public String toSourceString() {
        var stringBuilder = new StringBuilder();

        surroundingSeparatorsGroup
                .map((group) ->
                    PandaStream.of(SeparatorType.getValues())
                            .filter(type -> type.getGroup() == group && type.getDirection() == SeparatorDirection.LEFT)
                            .any()
                            .get()
                )
                .peek(separator -> stringBuilder.append(separator.getSymbol()).append(" "));

        getValue().forEach(token -> {
            if (token instanceof Section section) {
                stringBuilder.append(section.toSourceString()).append(" ");
                return;
            }

            if (token instanceof Indentation indentation) {
                stringBuilder.append("\n").append(indentation.toSourceString());
                return;
            }

            stringBuilder.append(token.toSourceString()).append(" ");
        });

        surroundingSeparatorsGroup
                .map((group) ->
                        PandaStream.of(SeparatorType.getValues())
                                .filter(type -> type.getGroup() == group && type.getDirection() == SeparatorDirection.RIGHT)
                                .any()
                                .get()
                )
                .peek(separator -> stringBuilder.append(separator.getSymbol()).append(" "));

        return stringBuilder.toString().trim();
    }

}
