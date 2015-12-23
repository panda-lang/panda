package org.panda_lang.panda.core.scheme;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.lang.PObject;

import java.util.ArrayList;
import java.util.Collection;

public class ObjectScheme {

    private final String name;
    private final Class<? extends PObject> clazz;
    private final Collection<MethodScheme> methods;
    private Constructor constructor;

    public ObjectScheme(Class<? extends PObject> clazz, String name) {
        this.clazz = clazz;
        this.name = name;
        this.methods = new ArrayList<>();
        ElementsBucket.registerObject(this);
    }

    public void registerConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public void registerMethod(MethodScheme method) {
        this.methods.add(method);
    }

    public Collection<MethodScheme> getMethods() {
        return methods;
    }

    public Class<? extends PObject> getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }

}
