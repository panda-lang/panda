package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternUnit;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;
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
        Tokens[] dynamics = matchUnits(elements, distributor);

        if (dynamics == null) {
            return new TokenExtractorResult("Dynamics are null");
        }

        /*
        System.out.println("---");
        for (TokenizedSource dynamic : dynamics) {
            if (dynamic == null) {
                continue;
            }

            System.out.println(dynamic.asString());
        }
        */

        return matchDynamics(elements, dynamics);
    }

    private @Nullable Tokens[] matchUnits(List<LexicalPatternElement> elements, TokenDistributor distributor) {
        Tokens[] dynamics = new Tokens[elements.size()];
        Stack<Separator> separators = new Stack<>();

        int lockState = separators.size();
        int minIndex = 0;

        for (int i = 0; i < dynamics.length; i++) {
            StackUtils.popSilently(separators, separators.size() - lockState);
            LexicalPatternElement element = elements.get(i);

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
                    found = true;
                    break;
                }

                checkOpening(separators, representation);
                dynamic++;
            }

            if (!found && unit.isOptional()) {
                continue;
            }

            if (!found && (distributor.getIndex() + dynamic != distributor.length()) && !unit.isOptional()) {
                return null;
            }

            if (separators.size() > 1) {
                return null;
            }

            if (dynamic == 0) {
                checkOpening(separators, distributor.next());
                lockState = separators.size();
                continue;
            }

            if ((found) || (distributor.getIndex() + dynamic == distributor.length() && unit.isOptional())) {
                int index = getLastDynamicIndex(dynamics, minIndex);

                if (index == -1) {
                    return null;
                }

                dynamics[index] = new PandaTokens(distributor.next(dynamic));
                minIndex = i;

                TokenRepresentation representation = distributor.next();
                checkOpening(separators, representation);
                lockState = separators.size();

                continue;
            }

            return null;
        }

        return dynamics;
    }

    private boolean checkClosing(Stack<Separator> separators, TokenRepresentation representation) {
        if (!separators.isEmpty()) {
            if (!TokenUtils.isTypeOf(representation, TokenType.SEPARATOR)) {
                return false;
            }

            if (!TokenUtils.equals(representation, separators.peek().getOpposite())) {
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

    private TokenExtractorResult matchDynamics(List<LexicalPatternElement> elements, Tokens[] dynamics) {
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

            Tokens nodeContent = dynamics[i];
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

                dynamics[nextIndex] = new PandaTokens(content.next(content.length() - content.getIndex()));
            }

            result.addIdentifier(nodeElement.getIdentifier());
            result.merge(nodeElementResult);
        }

        for (Tokens dynamicContent : dynamics) {
            if (dynamicContent != null) {
                return new TokenExtractorResult();
            }
        }

        return result;
    }

    private int getLastDynamicIndex(Tokens[] dynamics, int minIndex) {
        for (int i = minIndex; i < dynamics.length; i++) {
            if (dynamics[i] != null) {
                continue;
            }

            return i;
        }

        return -1;
    }

}
