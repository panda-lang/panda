package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.core.parser.essential.util.CharArrayDistributor;

public class Pattern implements Comparable<Pattern> {

    private final String pattern;
    private final double priority;
    private Parser parser;
    private char[] charset;
    private int id;

    public Pattern(String pattern) {
        this(pattern, 0, 0);
    }

    public Pattern(Parser parser, String pattern) {
        this(parser, pattern, 0);
    }

    public Pattern(String pattern, double priority, int id) {
        this(null, pattern, priority);
        this.id = id;
    }

    public Pattern(String pattern, double priority, int id, char[] charset) {
        this(pattern, priority, id);
        this.charset = charset;
    }

    public Pattern(Parser parser, String pattern, double priority) {
        this.parser = parser;
        this.pattern = pattern.replace(" ", "");
        this.priority = priority;
    }

    public boolean match(String s) {
        char[] string = s.toCharArray();
        int i = 0;

        if (string.length == 0) {
            return false;
        }

        CharArrayDistributor distributor = new CharArrayDistributor(getPattern().toCharArray());
        while (distributor.hasNext() && i < string.length) {
            char current = distributor.current();
            if (current == string[i]) {
                distributor.next();
                i++;
            }
            else if (current == '*') {
                if (distributor.further() == string[i]) {
                    distributor.next();
                    distributor.next();
                }
                i++;
            }
            else {
                break;
            }
        }
        return !distributor.hasNext();
    }

    public int count(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    public Pattern parser(Parser parser) {
        this.parser = parser;
        return this;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setCharset(char[] charset) {
        this.charset = charset;
    }

    public char getNext(char[] array, int current) {
        return current + 1 < array.length ? array[current + 1] : array[current];
    }

    public double getPriority() {
        return priority;
    }

    public String getPattern() {
        return pattern;
    }

    public Parser getParser() {
        return parser;
    }

    public char[] getCharset() {
        return charset;
    }

    public int getID() {
        return id;
    }

    @Override
    public int compareTo(Pattern pattern) {
        double priority = this.priority;
        double priorityTo = pattern.getPriority();

        if (priorityTo == 0 && priority == 0) {
            priority = count(getPattern(), '*');
            priorityTo = count(pattern.getPattern(), '*');
        }
        else if (priorityTo == priority) {
            return 0;
        }
        if (priorityTo == priority) {
            priority = getPattern().length();
            priorityTo = pattern.getPattern().length();
        }

        if (priorityTo < priority) {
            return 1;
        }
        else if (priorityTo > priority) {
            return -1;
        }
        return 0;
    }

}
