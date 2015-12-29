package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

public class Import implements NamedExecutable {

    private final String declaredImport;
    private String specific;
    private String as;

    public Import(String declaredImport) {
        this(declaredImport, null);
    }

    public Import(String declaredImport, String as) {
        this.declaredImport = declaredImport;
        this.as = as;
    }

    @Override
    public Essence run(Particle particle) {
        return null;
    }

    public boolean isDefinedGroup() {
        return !isDefinedScript();
    }

    public boolean isDefinedScript() {
        return declaredImport.contains(">");
    }

    public boolean isDefinedFile() {
        return declaredImport.charAt(0) == '\'';
    }

    public boolean containsCustomName() {
        return as != null;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public String getSpecific() {
        return specific;
    }

    public String getAs() {
        return as;
    }

    @Override
    public String getName() {
        return declaredImport;
    }

}
