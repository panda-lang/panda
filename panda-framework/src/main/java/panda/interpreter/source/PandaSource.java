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

package panda.interpreter.source;

import panda.interpreter.architecture.module.Module;

import java.util.Objects;

public class PandaSource implements Source {

    private final String id;
    private final String content;
    private final boolean virtual;
    private final Module module;

    public PandaSource(Module module, URLSource codeSource) {
        this(module, codeSource.getLocation().getPath(), codeSource.getContent(), false);
    }

    public PandaSource(Module module, Object id, String content) {
        this(module, id, content, true);
    }

    public PandaSource(Module module, Object id, String content, boolean virtual) {
        this.id = id.toString();
        this.content = content;
        this.virtual = virtual;
        this.module = module;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PandaSource that = (PandaSource) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public boolean isVirtual() {
        return virtual;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public String getId() {
        return this.id;
    }

}
