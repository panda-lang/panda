package panda.interpreter.compiler;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompiledScript {

    private final Map<String, String> javaImports = new HashMap<>();
    private final List<byte[]> classes = new ArrayList<>();

    public void addImport(String javaType) {
        javaImports.put(javaType.substring(javaType.lastIndexOf("/")), javaType);
    }

    public void addClass(byte[] source) {
        classes.add(source);
    }

    public @Nullable String findJavaImportByTypeName(String name) {
        return javaImports.get(name);
    }

    public List<byte[]> getClasses() {
        return classes;
    }

}
