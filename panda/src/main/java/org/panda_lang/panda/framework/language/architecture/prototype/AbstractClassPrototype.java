package org.panda_lang.panda.framework.language.architecture.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructors;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeFields;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethods;
import org.panda_lang.panda.framework.language.architecture.prototype.constructor.PandaConstructors;
import org.panda_lang.panda.framework.language.architecture.prototype.field.PandaFields;
import org.panda_lang.panda.framework.language.architecture.prototype.method.PandaMethods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class AbstractClassPrototype implements ClassPrototype {

    protected final String name;
    protected final Class<?> associated;
    protected final Collection<String> aliases;
    protected final Collection<ClassPrototype> extended = new ArrayList<>(1);
    protected final PrototypeConstructors constructors = new PandaConstructors();
    protected final PrototypeFields fields = new PandaFields();
    protected final PrototypeMethods methods = new PandaMethods();

    public AbstractClassPrototype(String name, Class<?> associated, Collection<String> aliases) {
        this.name = name;
        this.associated = associated;
        this.aliases = new ArrayList<>(aliases);
    }

    @Override
    public boolean isClassOf(String className) {
        if (this.getClassName().equals(className)) {
            return true;
        }

        if (this.associated != null && this.associated.getSimpleName().equals(className)) {
            return true;
        }

        for (String alias : this.getAliases()) {
            if (alias.equals(className)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isAssociatedWith(ClassPrototype prototype) { // this (Panda Class | Java Class) isAssociatedWith
        return prototype != null && (prototype.equals(this)
                || PandaClassPrototypeUtils.isAssociatedWith(associated, prototype.getAssociated())
                || PandaClassPrototypeUtils.hasCommonPrototypes(extended, prototype.getExtended()));
    }

    @Override
    public Collection<ClassPrototype> getExtended() {
        return extended;
    }

    @Override
    public Class<?> getAssociated() {
        return associated;
    }

    @Override
    public Collection<String> getAliases() {
        return aliases;
    }

    @Override
    public PrototypeMethods getMethods() {
        return methods;
    }

    @Override
    public PrototypeFields getFields() {
        return fields;
    }

    @Override
    public PrototypeConstructors getConstructors() {
        return constructors;
    }

    @Override
    public String getClassName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        return this == o;
    }

    @Override
    public String toString() {
        return "ClassPrototype::" + name;
    }

}
