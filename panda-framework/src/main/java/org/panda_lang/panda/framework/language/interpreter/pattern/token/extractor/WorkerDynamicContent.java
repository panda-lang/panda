package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternUnit;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;

import java.util.List;
import java.util.Stack;

class WorkerDynamicContent {

    private final TokenExtractorWorker worker;

    WorkerDynamicContent(TokenExtractorWorker worker) {
        this.worker = worker;
    }

    protected TokenExtractorResult matchDynamicContent(LexicalPatternNode node, TokenDistributor distributor) {
        List<LexicalPatternElement> elements = node.getElements();
        TokenizedSource[] dynamics = matchUnits(elements, distributor);

        if (dynamics == null) {
            return new TokenExtractorResult("Dynamics are null");
        }

        return matchDynamics(elements, dynamics);
    }

    private @Nullable TokenizedSource[] matchUnits(List<LexicalPatternElement> elements, TokenDistributor distributor) {
        TokenizedSource[] dynamics = new TokenizedSource[elements.size()];
        int minIndex = 0;

        for (int i = 0; i < elements.size(); i++) {
            LexicalPatternElement element = elements.get(i);

            if (!element.isUnit()) {
                continue;
            }

            LexicalPatternUnit unit = element.toUnit();
            Stack<Separator> separators = new Stack<>();
            boolean found = false;
            int dynamic = 0;

            for (TokenRepresentation representation : distributor) {
                if (!separators.isEmpty()) {
                    dynamic++;

                    if (!TokenUtils.isTypeOf(representation, TokenType.SEPARATOR)) {
                        continue;
                    }

                    if (!TokenUtils.equals(representation, separators.peek().getOpposite())) {
                        continue;
                    }

                    dynamic--;
                    separators.pop();
                }

                if (representation.getTokenValue().equals(unit.getValue())) {
                    found = true;
                    break;
                }

                if (TokenUtils.isTypeOf(representation, TokenType.SEPARATOR)) {
                    Separator separator = (Separator) representation.getToken();

                    if (separator.hasOpposite()) {
                        separators.push(separator);
                    }
                }

                dynamic++;
            }

            if (!found && unit.isOptional()) {
                continue;
            }

            if (!found && (distributor.getIndex() + dynamic != distributor.length()) && !unit.isOptional()) {
                return null;
            }

            if (!separators.isEmpty()) {
                return null;
            }

            if (dynamic == 0) {
                distributor.next();
                continue;
            }

            if ((found) || (distributor.getIndex() + dynamic == distributor.length() && unit.isOptional())) {
                int index = getLastDynamicIndex(dynamics, minIndex);

                if (index == -1) {
                    return null;
                }

                dynamics[index] = new PandaTokenizedSource(distributor.next(dynamic));
                distributor.next();
                minIndex = i;
            }
        }

        return dynamics;
    }

    private TokenExtractorResult matchDynamics(List<LexicalPatternElement> elements, TokenizedSource[] dynamics) {
        TokenExtractorResult result = new TokenExtractorResult(true);

        for (int i = 0; i < elements.size(); i++) {
            LexicalPatternElement nodeElement = elements.get(i);


            if (nodeElement.isUnit() || (dynamics.length == 0 && nodeElement.isOptional())) {
                if (nodeElement.isOptional() && dynamics[i] == null) {
                    continue;
                }

                int nextIndex = i + 1;

                if (nextIndex >= dynamics.length) {
                    continue;
                }

                dynamics[nextIndex] = dynamics[i];
                dynamics[i] = null;
                continue;
            }

            TokenizedSource nodeContent = dynamics[i];
            dynamics[i] = null;

            if (nodeContent == null) {
                return new TokenExtractorResult("Node content is null");
            }

            TokenDistributor content = new TokenDistributor(nodeContent);
            TokenExtractorResult nodeElementResult = worker.extract(nodeElement, content);

            if (!nodeElementResult.isMatched()) {
                return new TokenExtractorResult();
            }

            if (content.hasNext()) {
                int nextIndex = i + 1;

                if (nextIndex >= dynamics.length || dynamics[nextIndex] != null) {
                    return new TokenExtractorResult();
                }

                dynamics[nextIndex] = new PandaTokenizedSource(content.next(content.length() - content.getIndex()));
            }

            result.addIdentifier(nodeElement.getIdentifier());
            result.merge(nodeElementResult);
        }

        for (TokenizedSource dynamicContent : dynamics) {
            if (dynamicContent != null) {
                return new TokenExtractorResult();
            }
        }

        return result;
    }

    private int getLastDynamicIndex(TokenizedSource[] dynamics, int minIndex) {
        for (int i = minIndex; i < dynamics.length; i++) {
            if (dynamics[i] != null) {
                continue;
            }

            return i;
        }

        return -1;
    }

}
