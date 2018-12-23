package org.panda_lang.panda.framework.design.resource.loader;

import org.panda_lang.panda.framework.design.resource.Autoload;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;

import java.util.Collection;

public class AutoloadLoader {

    public void load(AnnotationsScannerProcess scannerProcess) {
        Collection<Class<?>> classes = scannerProcess.createSelector().selectTypesAnnotatedWith(Autoload.class);

        for (Class<?> clazz : classes) {
            try {
                Class.forName(clazz.getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
