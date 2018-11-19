package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.popsuted;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternWildcard;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.WildcardCondition;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.WildcardConditionFactory;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.ArrayList;
import java.util.List;

class WorkerWildcardContent {

    private final TokenExtractorWorker worker;

    WorkerWildcardContent(TokenExtractorWorker worker) {
        this.worker = worker;
    }

    protected TokenExtractorResult matchWildcard(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        Tokens wildcardContent = null;

        if (wildcard.getDetails() != null) {
            wildcardContent = matchWildcardWithCondition(wildcard, distributor);
        }

        if (wildcardContent == null) {
            wildcardContent = new PandaTokens(distributor.next());
        }

        return new TokenExtractorResult(true).addWildcard(wildcard.getName(), wildcardContent);
    }

    private @Nullable Tokens matchWildcardWithCondition(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        String details = wildcard.getOriginalDetails();

        if (details.startsWith("*")) {
            return new PandaTokens(distributor.next(distributor.length() - distributor.getIndex()));
        }

        if (!details.contains(":")) {
            return null;
        }

        String[] elements = StringUtils.splitFirst(details, ":");
        wildcard.setName(elements[0]);
        wildcard.setDetails(elements[1]);

        String[] conditions = wildcard.getDetails().split(",");
        List<WildcardCondition> wildcardConditions = new ArrayList<>(conditions.length);

        for (String condition : conditions) {
            WildcardCondition wildcardCondition = createWildcardCondition(condition);

            if (wildcardCondition == null) {
                throw new PandaFrameworkException("Unknown wildcard condition: " + condition);
            }

            wildcardConditions.add(wildcardCondition);
        }

        List<TokenRepresentation> tokens = new ArrayList<>(distributor.length() - distributor.getIndex());

        while (distributor.hasNext()) {
            TokenRepresentation next = distributor.getNext();

            if (!checkWildcard(wildcardConditions, next)) {
                break;
            }

            tokens.add(distributor.next());
        }

        return new PandaTokens(tokens);
    }

    private @Nullable WildcardCondition createWildcardCondition(String condition) {
        condition = condition.trim();

        for (WildcardConditionFactory factory : worker.pattern.getWildcardConditionFactories()) {
            if (factory.handle(condition)) {
                return factory.create(condition);
            }
        }

        return null;
    }

    private boolean checkWildcard(List<WildcardCondition> wildcardConditions, TokenRepresentation next) {
        for (WildcardCondition wildcardCondition : wildcardConditions) {
            if (wildcardCondition.accept(next)) {
                return true;
            }
        }

        return false;
    }

}
