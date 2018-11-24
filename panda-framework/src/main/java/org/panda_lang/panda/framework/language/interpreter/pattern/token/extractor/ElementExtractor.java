package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor;

interface ElementExtractor<T> {

    ExtractorResult extract(T element, TokenDistributor distributor);

}
