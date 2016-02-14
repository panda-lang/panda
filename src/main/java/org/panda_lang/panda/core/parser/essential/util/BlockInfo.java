package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.parser.util.ParserUtils;

import java.util.List;

public class BlockInfo {

    private final String type;
    private final List<String> specifiers;
    private final List<String> parameters;

    public BlockInfo(String type, List<String> specifiers, List<String> parameters) {
        this.type = type;
        this.specifiers = specifiers;
        this.parameters = parameters;
    }

    public String getType() {
        return type;
    }

    public String getSpecifiersAsPhrase() {
        return ParserUtils.toString(specifiers, " ", 0, specifiers.size());
    }

    public List<String> getSpecifiers() {
        return specifiers;
    }

    public String[] getParameters() {
        String[] params = new String[parameters.size()];
        return parameters.toArray(params);
    }

    public List<String> getParametersList() {
        return parameters;
    }

}
