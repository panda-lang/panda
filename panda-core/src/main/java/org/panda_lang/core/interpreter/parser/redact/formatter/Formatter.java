package org.panda_lang.core.interpreter.parser.redact.formatter;

import org.panda_lang.core.element.Sequence;
import org.panda_lang.core.interpreter.parser.redact.Fragment;

import java.util.Stack;

public class Formatter {

    private final FormatterRules formatterRules;

    public Formatter(FormatterRules formatterRules) {
        this.formatterRules = formatterRules;
    }

    public Fragment format(Fragment unformatted) {
        String fragment = unformatted.getFragment();
        char[] fragmentChars = fragment.toCharArray();

        StringBuilder fragmentBuilder = new StringBuilder();
        StringBuilder sequenceBuilder = new StringBuilder();
        Stack<Sequence> sequenceStack = new Stack<>();

        for (int i = 0; i < fragmentChars.length; i++) {
            char c = fragmentChars[i];
            fragment = fragmentBuilder.toString();

            if (sequenceStack.size() > 0) {
                Sequence previousSequence = sequenceStack.peek();

                sequenceBuilder.append(c);
                String sequenceFragment = sequenceBuilder.toString();

                if (!sequenceFragment.endsWith(previousSequence.getSequenceEnd())) {
                    continue;
                }

                if (formatterRules.isSequenceOverlookEnabled()) {
                    if (!previousSequence.isOverlooked()) {
                        fragmentBuilder.append(sequenceBuilder.toString());
                    }
                }
                else {
                    fragmentBuilder.append(sequenceBuilder.toString());
                }

                sequenceBuilder.setLength(0);
                sequenceStack.pop();
                continue;
            }

            String currentFragment = fragment + c;
            for (Sequence sequence : formatterRules.getSequences()) {
                if (!currentFragment.endsWith(sequence.getSequenceStart())) {
                    continue;
                }

                fragmentBuilder.setLength(fragmentBuilder.length() + 1 - sequence.getSequenceStart().length());
                sequenceBuilder.append(sequence.getSequenceStart());
                sequenceStack.push(sequence);
                break;
            }

            if (sequenceStack.size() > 0) {
                continue;
            }

            if (formatterRules.isWhitespaceControlEnabled() && i > 0 && c != '\n') {
                char previousChar = fragmentChars[i - 1];

                if (Character.isWhitespace(c) && (Character.isWhitespace(previousChar) || !Character.isAlphabetic(previousChar))) {
                    continue;
                }
            }

            fragmentBuilder.append(c);

            if (currentFragment.endsWith(System.lineSeparator())) {
                currentFragment = currentFragment.substring(0, currentFragment.length() - System.lineSeparator().length());
                fragmentBuilder = new StringBuilder(currentFragment);
            }
        }

        fragment = fragmentBuilder.toString();

        if (formatterRules.isTrimEnabled()) {
            fragment = fragment.trim();
        }

        Fragment formatted = new Fragment(fragment);
        formatted.setIndention(unformatted.getIndention());
        formatted.setInline(unformatted.getInline());
        formatted.setLine(unformatted.getLine());
        return formatted;
    }

    public FormatterRules getFormatterRules() {
        return formatterRules;
    }

}
