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

package org.panda_lang.panda.utilities.redact.match.charset;

import org.panda_lang.panda.utilities.commons.arrays.CharArrayDistributor;
import org.panda_lang.panda.utilities.redact.match.Matcher;

import java.security.InvalidParameterException;
import java.util.Objects;

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
        this.charset = charset.clone();
    }

    public CharsetPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean match(String s) {
        char[] string = s.toCharArray();
        int i = 0;

        if (string.length == 0 && getPattern().length() == 0) {
            return false;
        }

        CharArrayDistributor distributor = new CharArrayDistributor(getPattern().toCharArray());
        distributor.next();

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
            throw new InvalidParameterException("Pattern is null");
        }

        if (Objects.equals(this, pattern)) {
            return 0;
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

        if (Double.compare(priority, priorityTo) == 0) {
            priority = getPattern().length();
            priorityTo = pattern.getPattern().length();
        }

        return Double.compare(priority, priorityTo);
    }

    public void setCharset(char[] charset) {
        this.charset = charset.clone();
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public String getPattern() {
        return pattern;
    }

    public char[] getCharset() {
        return charset.clone();
    }

    public double getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return pattern;
    }

}
