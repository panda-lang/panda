package org.panda_lang.panda.util.documentation;

public class Documentation {

    private String name;
    private String usage;
    private String description;
    private String example;

    public Documentation name(String name) {
        this.name = name;
        return this;
    }

    public Documentation usage(String usage) {
        this.usage = usage;
        return this;
    }

    public Documentation description(String description) {
        this.description = description;
        return this;
    }

    public Documentation example(String example) {
        this.example = example;
        return this;
    }

    public String getExample() {
        return example;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getName() {
        return name;
    }

}
