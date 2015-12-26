package org.panda_lang.panda.core.syntax;

import java.util.Arrays;

public class Namespace {

    private final String[] namespace;

    public Namespace(String namespace) {
        this.namespace = namespace.split(".");
    }

    public Namespace(String... namespace) {
        this.namespace = namespace;
    }

    public boolean isDefinedVial() {
        return !isDefinedNamespace();
    }

    public boolean isDefinedNamespace() {
        return namespace[namespace.length - 1].equals("*");
    }

    public Namespace getParent() {
        return new Namespace(Arrays.copyOf(namespace, namespace.length - 1));
    }

}
