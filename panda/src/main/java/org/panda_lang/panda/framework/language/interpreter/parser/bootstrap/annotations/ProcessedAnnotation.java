package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ProcessedAnnotation {

    protected final Class<? extends Annotation> annotationType;
    protected final Map<String, Object> values = new HashMap<>();

    public ProcessedAnnotation(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    public void load(Annotation annotation) throws Exception {
        for (Method declaredMethod : annotation.annotationType().getDeclaredMethods()) {
            values.put(declaredMethod.getName(), declaredMethod.invoke(annotation));
        }
    }

    public void load(String key, Object value) {
        values.put(key, value);
    }

    public <T> T getDefaultValue() {
        return getValue("value");
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return (T) values.get(key);
    }

    public Class<? extends Annotation> getAnnotationType() {
        return annotationType;
    }

}
