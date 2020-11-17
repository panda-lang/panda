package org.panda_lang.language.architecture.type;

import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.utilities.commons.function.CompletableOption;

public class Reference {

    private final CompletableOption<Type> type;
    private final String name;
    private final Module module;
    private final Visibility visibility;
    private final String kind;
    private final Location location;

    public Reference(CompletableOption<Type> type, Module module, String name, Visibility visibility, String kind, Location location) {
        this.type = type;
        this.name = name;
        this.module = module;
        this.visibility = visibility;
        this.kind = kind;
        this.location = location;
    }

    public Reference(Type type) {
        this(CompletableOption.completed(type), type.getModule(), type.getName(), type.getVisibility(), type.getKind(), type.getLocation());
    }

    public boolean isLoaded() {
        return type.isReady();
    }

    public Type fetchType() {
        return getType().get();
    }

    public CompletableOption<Type> getType() {
        return type;
    }

    public String getName() {
        return module.getName() + "::" + getSimpleName();
    }

    public Module getModule() {
        return module;
    }

    public String getSimpleName() {
        return name;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public String getKind() {
        return kind;
    }

    public Location getLocation() {
        return location;
    }

}
