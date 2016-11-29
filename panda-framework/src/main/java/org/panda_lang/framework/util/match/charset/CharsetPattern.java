/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.framework.util.match.charset;

import org.panda_lang.framework.util.array.CharArrayDistributor;
import org.panda_lang.framework.util.match.Matcher;

public class CharsetPattern implements Matcher, Comparable<CharsetPattern> {

    private final String pattern;
    private char[] charset;
    private double priority;

    public CharsetPattern(String pattern, char[] charset, double priority) {
        this(pattern, charset);
        this.priority = priority;
    }

    public CharsetPattern(String pattern, char[] charset) {
        this(pattern);
        this.charset = charset;
    }

    public CharsetPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
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

    public char getNext(char[] array, int current) {
        return current + 1 < array.length ? array[current + 1] : array[current];
    }

    @Override
    public int compareTo(CharsetPattern pattern) {
        if (pattern == null) {
            return 1;
        }

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

    public void setCharset(char[] charset) {
        this.charset = charset;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public String getPattern() {
        return pattern;
    }

    public char[] getCharset() {
        return charset;
    }

    public double getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return pattern;
    }

}
