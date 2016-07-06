package org.panda_lang.panda.util.configuration;

public class ConfigurationObject {

    private final ConfigurationType type;
    private Object object;
    private int position;

    protected ConfigurationObject(ConfigurationType type) {
        this.type = type;
    }

    protected String getString() {
        if (type == ConfigurationType.STRING) {
            return (String) object;
        }
        return null;
    }

    protected int getPosition() {
        return this.position;
    }

    protected void setPosition(int i) {
        this.position = i;
    }

    protected Object getObject() {
        return this.object;
    }

    protected void setObject(Object o) {
        this.object = o;
    }

    protected ConfigurationType getType() {
        return this.type;
    }

}
