package org.panda_lang.language.architecture.type.signature;

public enum Relation {

    DIRECT(false),
    ANY(true),
    ALSO(true);

    private final boolean wildcard;

    Relation(boolean wildcard) {
        this.wildcard = wildcard;
    }

    public boolean isWildcard() {
        return wildcard;
    }

}
