package org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor;

abstract class AbstractElementExtractor<T> implements ElementExtractor<T> {

    protected final ExtractorWorker worker;

    protected AbstractElementExtractor(ExtractorWorker worker) {
        this.worker = worker;
    }

    protected ExtractorWorker getWorker() {
        return worker;
    }

}
