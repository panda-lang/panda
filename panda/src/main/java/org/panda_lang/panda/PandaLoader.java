/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda;

import org.panda_lang.panda.framework.interpreter.source.Source;
import org.panda_lang.panda.framework.interpreter.source.SourceProvider;
import org.panda_lang.panda.implementation.interpreter.PandaInterpreter;
import org.panda_lang.panda.implementation.interpreter.source.PandaCodeSource;
import org.panda_lang.panda.implementation.interpreter.source.PandaSource;
import org.panda_lang.panda.implementation.interpreter.source.PandaSourceSet;
import org.panda_lang.panda.implementation.structure.PandaApplication;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

public class PandaLoader {

    private final Panda panda;

    public PandaLoader(Panda panda) {
        this.panda = panda;
    }

    public PandaApplication loadFiles(File... files) {
        return this.load(new FileSourceProvider(files));
    }

    public PandaApplication load(SourceProvider provider) {
        PandaSourceSet pandaSourceSet = new PandaSourceSet();
        for (Source source : provider) {
            pandaSourceSet.addSource(source);
        }
        if (pandaSourceSet.getSources().isEmpty()) {
            throw new LoaderException("no sources provided.");
        }
        PandaInterpreter interpreter = new PandaInterpreter(this.panda, pandaSourceSet);
        interpreter.interpret();

        return interpreter.getApplication();
    }

    class FileSourceProvider implements SourceProvider {
        private final File[] files;

        FileSourceProvider(File[] files) {
            this.files = files;
        }

        @Override
        public Iterator<Source> iterator() {
            Iterator<File> iterator = Arrays.asList(this.files).iterator();
            return new Iterator<Source>() {
                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public Source next() {
                    File next = iterator.next();
                    if (!next.exists()) {
                        throw new LoaderException("File '" + next.getName() + "' doesn't exist.");
                    }
                    if (next.isDirectory()) {
                        throw new LoaderException("File '" + next.getName() + "' ia a directory.");
                    }
                    return new PandaSource(PandaCodeSource.fromFile(next));
                }
            };
        }
    }
}
