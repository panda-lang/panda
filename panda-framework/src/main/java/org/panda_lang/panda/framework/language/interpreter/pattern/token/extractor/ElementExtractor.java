package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor;

import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;

interface ElementExtractor<T> {

    ExtractorResult extract(T element, TokenDistributor distributor);

}
