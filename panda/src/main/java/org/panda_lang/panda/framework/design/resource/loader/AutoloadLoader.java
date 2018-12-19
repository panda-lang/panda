package org.panda_lang.panda.framework.design.resource.loader;

import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;

public class AutoloadLoader {

    public void load(AnnotationsScannerProcess scannerProcess) {
        try {
            loadClasses(scannerProcess);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadClasses(AnnotationsScannerProcess scannerProcess) throws Exception {

    }

}
