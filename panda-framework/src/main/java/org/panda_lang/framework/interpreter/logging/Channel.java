/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.interpreter.logging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import static panda.utilities.collection.Lists.add;

public class Channel implements Comparable<Channel> {

    private static final Collection<Channel> VALUES = new ArrayList<>();

    public static final Channel FATAL = add(VALUES, new Channel("fatal", 6.0));
    public static final Channel ERROR = add(VALUES, new Channel("error", 5.0));
    public static final Channel WARN = add(VALUES, new Channel("warn", 4.0));
    public static final Channel INFO = add(VALUES, new Channel("info", 3.0));
    public static final Channel DEBUG = add(VALUES, new Channel("debug", 2.0));
    public static final Channel TRACE = add(VALUES, new Channel("trace", 1.0));

    private final String channel;
    private final double priority;

    public Channel(String channel, double priority) {
        this.channel = channel;
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull Channel o) {
        return Double.compare(priority, o.priority);
    }

    public double getPriority() {
        return priority;
    }

    public String getChannel() {
        return channel;
    }

    public static Channel of(String channel) {
        for (Channel value : VALUES) {
            if (value.getChannel().equalsIgnoreCase(channel)) {
                return value;
            }
        }

        throw new UnsupportedOperationException(channel + " is not supported by default");
    }

}
