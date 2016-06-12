package org.panda_lang.panda.core.parser.redact.divider;

import org.panda_lang.panda.core.element.Literal;
import org.panda_lang.panda.core.element.Separator;

import java.util.Iterator;
import java.util.Stack;

public class Divider implements Iterable<Fragment>, Iterator<Fragment> {

    private final char[] source;
    private final DividerRules dividerRules;
    private final Stack<Literal> literalStack;
    private final StringBuilder indentionBuilder;
    private final StringBuilder nodeBuilder;
    private Fragment previousFragment;
    private Fragment currentFragment;
    private String node;
    private int inline;
    private int nodes;
    private int line;
    private int caret;

    /**
     * Divider deals with the separation of code into fragments.
     *
     * @param source       source code, which will be divided
     * @param dividerRules rules for Divider
     */
    public Divider(String source, DividerRules dividerRules) {
        this.source = source.toCharArray();
        this.dividerRules = dividerRules;
        this.nodeBuilder = new StringBuilder();
        this.indentionBuilder = new StringBuilder();
        this.literalStack = new Stack<>();
        this.caret = -1;
    }

    /**
     * @return the next piece of code
     */
    @Override
    public Fragment next() {
        caret++;

        divider:
        for (; caret < source.length; caret++, inline++) {
            char currentChar = source[caret];

            if (nodeBuilder.length() == 0 && Character.isWhitespace(currentChar)) {
                indentionBuilder.append(currentChar);
                continue;
            }

            nodeBuilder.append(currentChar);
            node = nodeBuilder.toString();

            if (node.endsWith(System.lineSeparator())) {
                line++;
            }

            for (Literal literal : dividerRules.getLiterals()) {
                String literalStart = literal.getLiteralStart();

                if (literalStack.size() != 0) {
                    Literal previousLiteral = literalStack.peek();

                    if (!node.endsWith(previousLiteral.getLiteralEnd())) {
                        continue;
                    }

                    literalStack.pop();
                    break;
                }

                if (!node.endsWith(literalStart)) {
                    continue;
                }

                literalStack.push(literal);
                break;
            }

            if (literalStack.size() != 0) {
                continue;
            }

            for (Separator separator : dividerRules.getSeparators()) {
                if (currentChar != separator.getSeparator()) {
                    continue;
                }

                break divider;
            }
        }

        Fragment fragment = new Fragment(nodeBuilder.toString());
        fragment.setIndention(indentionBuilder.toString());
        fragment.setInline(inline);
        fragment.setLine(line);

        previousFragment = currentFragment;
        currentFragment = fragment;

        indentionBuilder.setLength(0);
        nodeBuilder.setLength(0);
        inline = 0;
        nodes++;

        return currentFragment;
    }

    /**
     * @return SourceDivider iterator
     */
    @Override
    public Iterator<Fragment> iterator() {
        return this;
    }

    /**
     * Checks if there are further fragments
     */
    @Override
    public boolean hasNext() {
        if (caret + 1 >= source.length) {
            return false;
        }

        for (int i = caret + 1; i < source.length; i++) {
            char character = source[i];

            if (Character.isWhitespace(character)) {
                continue;
            }

            return true;
        }

        return false;
    }

    /**
     * @return the current node
     */
    public String getNode() {
        return node;
    }

    /**
     * @return set of rules for Divider
     */
    public DividerRules getDividerRules() {
        return dividerRules;
    }

    /**
     * @return previous fragment
     */
    public Fragment getPreviousFragment() {
        return previousFragment;
    }

    /**
     * @return the current number of code fragments
     */
    public int getNodes() {
        return nodes;
    }

    /**
     * @return the current line of code
     */
    public int getLine() {
        return line;
    }

    /**
     * @return position of caret
     */
    public int getCaretPosition() {
        return caret;
    }

    /**
     * @return source code
     */
    public char[] getSource() {
        return source;
    }

}
