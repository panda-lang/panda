package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.popsuted;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternUnit;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;
import org.panda_lang.panda.utilities.commons.StackUtils;

import java.util.List;
import java.util.Stack;

class WorkerDynamicContent {

    private final TokenExtractorWorker worker;

    WorkerDynamicContent(TokenExtractorWorker worker) {
        this.worker = worker;
    }

    protected TokenExtractorResult matchDynamicContent(LexicalPatternNode node, TokenDistributor distributor) {
        List<LexicalPatternElement> elements = node.getElements();

        TokenExtractorResult result = new TokenExtractorResult(true);
        Stack<DynamicContent> dynamics = matchUnits(result, elements, distributor);

        if (dynamics == null || dynamics.size() == 0) {
            return new TokenExtractorResult("Dynamics are null");
        }

        result = matchDynamics(result, elements, dynamics);

        if (!dynamics.isEmpty()) {
            distributor.setIndex(distributor.getIndex() - dynamics.pop().getTokens().size());
        }

        return result;
    }

    private @Nullable Stack<DynamicContent> matchUnits(TokenExtractorResult result, List<LexicalPatternElement> elements, TokenDistributor distributor) {
        Stack<DynamicContent> dynamics = new Stack<>();
        Stack<Separator> separators = new Stack<>();

        boolean lastWildcard = false;
        int lastWildcardIndex = -1;

        int lockState = separators.size();
        int minIndex = 0;

        for (int i = 0; i < elements.size(); i++) {
            StackUtils.popSilently(separators, separators.size() - lockState);
            LexicalPatternElement element = elements.get(i);

            boolean wasWildcard = lastWildcard;
            lastWildcard = element.isWildcard();

            if (element.isWildcard()) {
                lastWildcardIndex = i;
            }

            if (!element.isUnit()) {
                continue;
            }

            LexicalPatternUnit unit = element.toUnit();
            boolean found = false;
            int dynamic = 0;

            for (TokenRepresentation representation : distributor) {
                if (!separators.isEmpty()) {
                    dynamic++;

                    checkOpening(separators, representation);
                    checkClosing(separators, representation);

                    if (!separators.isEmpty()) {
                        continue;
                    }

                    dynamic--;
                }

                if (representation.getTokenValue().equals(unit.getValue())) {
                    result.identified(unit.getIdentifier());
                    found = true;
                    break;
                }

                checkOpening(separators, representation);
                dynamic++;
            }

            if (!found) {
                if (wasWildcard) {
                    lastWildcard = true;
                }

                if (unit.isOptional()) {
                    continue;
                }

                if (distributor.getIndex() + dynamic != distributor.length() && !unit.isOptional()) {
                    return null;
                }

                continue;
            }

            int previousMinIndex = minIndex;
            minIndex = i;

            if (separators.size() > 1) {
                return null;
            }

            if (dynamic == 0) {
                checkOpening(separators, distributor.next());
                lockState = separators.size();
                continue;
            }

            Tokens tokens = new PandaTokens(distributor.next(dynamic));
            dynamics.push(new DynamicContent(previousMinIndex, tokens));

            TokenRepresentation representation = distributor.next();
            checkOpening(separators, representation);
            lockState = separators.size();
        }

        if (lastWildcard && distributor.hasNext()) {
            dynamics.push(new DynamicContent(lastWildcardIndex, new PandaTokens(distributor.next(distributor.length() - distributor.getIndex()))));
        }

        return dynamics;
    }

    private boolean checkClosing(Stack<Separator> separators, TokenRepresentation representation) {
        if (!separators.isEmpty()) {
            if (!TokenUtils.isTypeOf(representation, TokenType.SEPARATOR)) {
                return false;
            }

            if (!representation.contentEquals(separators.peek().getOpposite())) {
                return false;
            }

            separators.pop();
            return true;
        }

        return false;
    }

    private boolean checkOpening(Stack<Separator> separators, TokenRepresentation representation) {
        if (TokenUtils.isTypeOf(representation, TokenType.SEPARATOR)) {
            Separator separator = (Separator) representation.getToken();

            if (separator.hasOpposite()) {
                separators.push(separator);
                return true;
            }
        }

        return false;
    }

    private TokenExtractorResult matchDynamics(TokenExtractorResult result, List<LexicalPatternElement> elements, Stack<DynamicContent> dynamicsStack) {
        Stack<DynamicContent> dynamics = StackUtils.reverse(dynamicsStack);

        for (int i = 0; i < elements.size(); i++) {
            LexicalPatternElement nodeElement = elements.get(i);

            if (dynamics.isEmpty()) {
                // return new TokenExtractorResult("Out of content");
                continue;
            }

            if (nodeElement.isUnit()) {
                continue;
            }

            DynamicContent nodeContent = dynamics.peek();

            if (nodeContent == null) {
                if (nodeElement.isOptional()) {
                    continue;
                }

                return new TokenExtractorResult("Node content is null");
            }

            if (nodeContent.getIndex() > i) {
                continue;
            }

            TokenDistributor content = new TokenDistributor(nodeContent.getTokens());
            TokenExtractorResult nodeElementResult = worker.extract(nodeElement, content);

            if (!nodeElementResult.isMatched()) {
                if (nodeElement.isOptional()) {
                    continue;
                }

                return nodeElementResult;
            }

            dynamics.pop();

            if (content.hasNext()) {
                /*
                int nextIndex = i + 1;

                if (nextIndex >= dynamics.length || dynamics[nextIndex] != null) {
                    return new TokenExtractorResult("Index out of dynamics' range or dynamics' content still exists");
                }
                */

                dynamics.push(new DynamicContent(i, new PandaTokens(content.next(content.length() - content.getIndex()))));
            }

            result.identified(nodeElement.getIdentifier());
            result.merge(nodeElementResult);
        }

        /*
        for (int i = 0; i < dynamics.size() - 1; i++) {
            if (dynamics[i] != null) {
                return new TokenExtractorResult("Not moved dynamic content left in the array");
            }
        }
        */

        return result;
    }

}
