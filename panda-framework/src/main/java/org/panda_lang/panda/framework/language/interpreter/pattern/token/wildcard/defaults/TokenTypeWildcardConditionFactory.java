package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.defaults;

import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.WildcardCondition;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.WildcardConditionFactory;
import org.panda_lang.panda.utilities.commons.StringUtils;

class TokenTypeWildcardConditionFactory implements WildcardConditionFactory {

    @Override
    public boolean handle(String condition) {
        return condition.startsWith("token");
    }

    @Override
    public WildcardCondition create(String condition) {
        String[] elements = StringUtils.splitFirst(condition, " ");

        if (elements.length < 2) {
            throw new PandaFrameworkException("Token condition does not contain specification");
        }

        String source = elements[1]
                .replace("{", "{" + System.lineSeparator())
                .replace("}", System.lineSeparator() + "}");

        JsonObject conditions = JsonValue.readHjson(source).asObject();
        return representation -> check(conditions, representation);
    }

    private boolean check(JsonObject conditions, TokenRepresentation representation) {
        if (!equals("type", conditions, representation)) {
            return false;
        }

        return equals("value", conditions, representation);
    }

    private boolean equals(String value, JsonObject conditions, TokenRepresentation representation) {
        return !conditions.names().contains(value) || representation.getTokenType().equalsIgnoreCase(conditions.get(value).asString());
    }

}
