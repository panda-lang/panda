package org.panda_lang.panda.core.parser.util;

import java.util.ArrayList;
import java.util.Collection;

public class InjectionCenter {

    private final Collection<Injection> injections;

    public InjectionCenter() {
        this.injections = new ArrayList<>();
    }

    public void registerInjection(Injection injection) {
        injections.add(injection);
    }

    public Collection<Injection> getInjections() {
        return injections;
    }

}
