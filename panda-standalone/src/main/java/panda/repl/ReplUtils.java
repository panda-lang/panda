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

package panda.repl;

import panda.interpreter.logging.LoggerHolder;

import java.util.Collection;

public final class ReplUtils {

    private ReplUtils() { }

    /**
     * Print results using {@link panda.interpreter.PandaFramework#getLogger()}
     *
     * @param results the results to print
     */
    public static void print(LoggerHolder logger, Collection<ReplResult> results) {
        for (ReplResult result : results) {
            String[] lines = result.toString().split(System.lineSeparator());

            for (String line : lines) {
                logger.getLogger().info(line);
            }
        }
    }

}
