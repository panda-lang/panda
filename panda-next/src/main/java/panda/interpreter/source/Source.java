package panda.interpreter.source;

public class Source {

    private final String location;
    private final String content;

    public Source(String location, String content) {
        this.location = location;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getLocation() {
        return location;
    }

}
