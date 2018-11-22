package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

abstract class AbstractElementExtractor<T> implements ElementExtractor<T> {

    protected final ExtractorWorker worker;

    protected AbstractElementExtractor(ExtractorWorker worker) {
        this.worker = worker;
    }

    protected ExtractorWorker getWorker() {
        return worker;
    }

}
