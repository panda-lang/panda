package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

import java.util.Arrays;

public class Group implements NamedExecutable {

    private final String[] namespace;

    public Group(String namespace) {
        this.namespace = namespace.split(".");
    }

    public Group(String... namespace) {
        this.namespace = namespace;
    }

    @Override
    public Essence run(Particle particle) {
        return null;
    }

    public boolean isDefinedVial() {
        return !isDefinedNamespace();
    }

    public boolean isDefinedNamespace() {
        return namespace[namespace.length - 1].equals("*");
    }

    public Group getParent() {
        return new Group(Arrays.copyOf(namespace, namespace.length - 1));
    }

    @Override
    public String getName() {
        StringBuilder namespaceBuilder = new StringBuilder();
        for (String part : namespace) {
            if (namespaceBuilder.length() != 0) {
                namespaceBuilder.append('.');
            }
            namespaceBuilder.append(part);
        }
        return namespaceBuilder.toString();
    }

}
