package org.panda_lang.panda.utilities.annotations.monads.selectors;

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import org.panda_lang.panda.utilities.annotations.AnnotationScannerStore;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerUtils;
import org.panda_lang.panda.utilities.annotations.adapter.MetadataAdapter;
import org.panda_lang.panda.utilities.annotations.monads.AnnotationsSelector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MethodAnnotationSelector implements AnnotationsSelector<Method> {

    private final Class<? extends Annotation> annotationType;

    public MethodAnnotationSelector(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    @Override
    public Collection<Method> select(AnnotationsScannerProcess process, AnnotationScannerStore store) {
        Set<String> selectedClasses = new HashSet<>();
        MetadataAdapter<ClassFile, FieldInfo, MethodInfo> adapter = process.getMetadataAdapter();

        for (ClassFile cachedClassFile : store.getCachedClassFiles()) {
            for (MethodInfo method : adapter.getMethods(cachedClassFile)) {
                for (String annotationName : adapter.getMethodAnnotationNames(method)) {
                    if (annotationType.getName().equals(annotationName)) {
                        selectedClasses.add(adapter.getMethodFullKey(cachedClassFile, method));
                    }
                }
            }
        }

        return AnnotationsScannerUtils.forMethods(process, selectedClasses);
    }

}
