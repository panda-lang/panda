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

package org.panda_lang.panda.framework.language.interpreter.source.providers;

import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.language.interpreter.source.PandaSource;
import org.panda_lang.panda.framework.language.interpreter.source.PandaURLSource;

import java.io.File;
import java.util.Iterator;

class FileToSourceIterator implements Iterator<Source> {

    private final Iterator<File> iterator;

    public FileToSourceIterator(Iterator<File> iterator) {
        this.iterator = iterator;
    }

    @Override
    public Source next() {
        File next = iterator.next();

        if (!next.exists()) {
            throw new PandaFrameworkException("File '" + next.getName() + "' doesn't exist.");
        }

        if (next.isDirectory()) {
            throw new PandaFrameworkException("File '" + next.getName() + "' ia a directory.");
        }

        return new PandaSource(PandaURLSource.fromFile(next));
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

}
