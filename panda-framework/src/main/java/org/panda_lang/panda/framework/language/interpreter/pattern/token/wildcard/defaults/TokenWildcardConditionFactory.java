package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.defaults;

import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.WildcardCondition;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.WildcardConditionFactory;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.WildcardConditionResult;
import org.panda_lang.panda.utilities.commons.StringUtils;

class TokenWildcardConditionFactory implements WildcardConditionFactory {

    private static final String TOKEN = "token";
    private static final String NOT_TOKEN = "not token";

    @Override
    public boolean handle(String condition) {
        return condition.startsWith(TOKEN) || condition.startsWith(NOT_TOKEN);
    }

    @Override
    public WildcardCondition create(String condition) {
        boolean renamed = false;

        if (condition.startsWith(NOT_TOKEN)) {
            condition = StringUtils.replaceFirst(condition, NOT_TOKEN, TOKEN);
            renamed = true;
        }

        String[] elements =  StringUtils.splitFirst(condition, " ");
        boolean not = renamed;

        if (elements.length < 2) {
            throw new PandaFrameworkException("Token condition does renamed contain specification");
        }

        String source = elements[1]
                .replace("{", "{" + System.lineSeparator())
                .replace("}", System.lineSeparator() + "}");

        JsonObject conditions = JsonValue
                .readHjson(source)
                .asObject();

        return representation -> check(conditions, representation).negate(not);
    }

    private WildcardConditionResult check(JsonObject conditions, TokenRepresentation representation) {
        if (!equals("type", conditions, representation.getTokenType())) {
            return WildcardConditionResult.NEUTRAL;
        }

        if (!equals("value", conditions, representation.getTokenValue())) {
            return WildcardConditionResult.NEUTRAL;
        }

        return WildcardConditionResult.ALLOWED;
    }

    private boolean equals(String key, JsonObject conditions, String value) {
        return !conditions.names().contains(key) || value.equalsIgnoreCase(conditions.get(key).asString());
    }

}
