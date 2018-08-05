/*
 * Copyright (c) 2015-2018 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.utilities.commons.redact.divider;

import org.panda_lang.panda.utilities.commons.objects.CharacterUtils;

import java.util.Iterator;
import java.util.Stack;

/**
 * Divider deals with the separation of code into fragments
 */
public class Divider implements Iterable<Fragment>, Iterator<Fragment> {

    private final char[] source;
    private final DividerRules dividerRules;
    private final Stack<DividerSequence> sequenceStack;
    private final StringBuilder indentionBuilder;
    private final StringBuilder nodeBuilder;
    private DividerSeparator latestSeparator;
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
        this.sequenceStack = new Stack<>();
        this.caret = -1;
        this.line = 1;
    }

    /**
     * @return the next piece of code
     */
    @Override
    public Fragment next() {
        inline = 0;
        caret++;

        divider:
        for (; caret < source.length; caret++, inline++) {
            char currentChar = source[caret];

            if (nodeBuilder.length() == 0 && CharacterUtils.isWhitespace(currentChar)) {
                indentionBuilder.append(currentChar);
                continue;
            }

            nodeBuilder.append(currentChar);
            node = nodeBuilder.toString();

            if (node.endsWith(System.lineSeparator())) {
                line++;
            }

            for (DividerSequence sequence : dividerRules.getSequences()) {
                String sequenceStart = sequence.getSequenceStart();

                if (sequenceStack.size() != 0) {
                    DividerSequence previousSequence = sequenceStack.peek();

                    if (!node.endsWith(previousSequence.getSequenceEnd())) {
                        continue;
                    }

                    sequenceStack.pop();
                    break;
                }

                if (!node.endsWith(sequenceStart)) {
                    continue;
                }

                sequenceStack.push(sequence);
                break;
            }

            if (sequenceStack.size() != 0) {
                continue;
            }

            for (DividerSeparator separator : dividerRules.getSeparators()) {
                if (!node.endsWith(separator.getTokenValue())) {
                    continue;
                }

                node = node.substring(0, node.length() - separator.getTokenValue().length());
                latestSeparator = separator;

                break divider;
            }
        }

        Fragment fragment = new Fragment(node);
        fragment.setIndention(indentionBuilder.toString());
        fragment.setSeparator(latestSeparator);
        fragment.setInline(inline);
        fragment.setLine(line);

        previousFragment = currentFragment;
        currentFragment = fragment;

        indentionBuilder.setLength(0);
        nodeBuilder.setLength(0);
        nodes++;

        return currentFragment;
    }

    /**
     * @return Divider iterator
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

            if (CharacterUtils.isWhitespace(character)) {
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
     * @return position of caret in the current line
     */
    public int getInlineCaretPosition() {
        return inline;
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
