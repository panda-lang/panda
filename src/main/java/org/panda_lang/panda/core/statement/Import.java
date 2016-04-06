package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.essential.GroupCenter;
import org.panda_lang.panda.core.statement.util.NamedExecutable;

public class Import implements NamedExecutable {

    private final String declaredImport;
    private final Group group;
    private String specific;
    private String as;

    public Import(String declaredImport) {
        this(declaredImport, null, null);
    }

    public Import(Group group) {
        this.declaredImport = group.getName();
        this.group = group;
    }

    public Import(Group group, Vial vial) {
        this.declaredImport = group.getName();
        this.group = group;
        this.specific = vial.getName();
    }

    public Import(String declaredImport, String specific, String as) {
        this.declaredImport = declaredImport;
        this.group = !isDefinedFile() ? GroupCenter.getGroup(declaredImport) : null;
        this.specific = specific;
        this.as = as;
    }

    @Override
    public Essence run(Alice alice) {
        return null;
    }

    public boolean containsCustomName() {
        return as != null;
    }

    public boolean isDefinedGroup() {
        return !isDefinedScript() && !isDefinedFile();
    }

    public boolean isDefinedScript() {
        return specific != null;
    }

    public boolean isDefinedFile() {
        return declaredImport.charAt(0) == '\'';
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public String getFile() {
        return declaredImport.substring(1, declaredImport.length() - 1);
    }

    public String getSpecific() {
        return specific;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }

    public Group getGroup() {
        return group;
    }

    public String getDeclaredImport() {
        return declaredImport;
    }

    @Override
    public String getName() {
        return declaredImport;
    }

}
