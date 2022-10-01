package panda.interpreter.runtime;

public class PandaClassLoader extends ClassLoader {

    public Class<?> defineClass(String className, byte[] data) {
        return defineClass(className, data, 0, data.length);
    }

}
