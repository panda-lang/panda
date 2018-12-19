package org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.popsuted;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternWildcard;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.condition.WildcardCondition;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.condition.WildcardConditionFactory;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;

import java.util.ArrayList;
import java.util.List;

class WorkerWildcardContent {

    private final TokenExtractorWorker worker;

    WorkerWildcardContent(TokenExtractorWorker worker) {
        this.worker = worker;
    }

    protected TokenExtractorResult matchWildcard(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        Tokens wildcardContent = null;

        if (wildcard.getCondition() != null) {
            wildcardContent = matchWildcardWithCondition(wildcard, distributor);
        }

        if (wildcardContent == null) {
            wildcardContent = new PandaTokens(distributor.next());
        }

        return new TokenExtractorResult(true).addWildcard(wildcard.getName(), wildcardContent);
    }

    private @Nullable Tokens matchWildcardWithCondition(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        if (wildcard.getName().startsWith("*")) {
            return new PandaTokens(distributor.next(distributor.size() - distributor.getIndex()));
        }

        if (!wildcard.hasCondition()) {
            return null;
        }

        String[] conditions = wildcard.getCondition().split(",");
        List<WildcardCondition> wildcardConditions = new ArrayList<>(conditions.length);

        for (String condition : conditions) {
            WildcardCondition wildcardCondition = createWildcardCondition(condition);

            if (wildcardCondition == null) {
                throw new PandaFrameworkException("Unknown wildcard condition: " + condition);
            }

            wildcardConditions.add(wildcardCondition);
        }

        List<TokenRepresentation> tokens = new ArrayList<>(distributor.size() - distributor.getIndex());

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
            /*
            if (wildcardCondition.accept(next)) {
                return true;
            }
            */
        }

        return false;
    }

}
