/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.framework.language.architecture.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.Property;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;

import java.util.Optional;

public final class VisibilityComparator {

    public static final String NOTE_MESSAGE = "You may want to change the architecture of your application or you can just simply hack it";

    public static void requireAccess(Property requested, Context context, Snippetable source) {
        canAccess(requested, context).ifPresent(message -> {
            throw new PandaParserFailure(context, source, message, NOTE_MESSAGE);
        });
    }

    public static Optional<String> canAccess(Property requested, Context context) {
        return canAccess(requested, context.getComponent(Components.SCRIPT).getModule(), context.getComponent(PrototypeComponents.PROTOTYPE));
    }

    public static Optional<String> canAccess(Property requested, Module currentModule, @Nullable Property currentPrototype) {
        if (requested.getVisibility() == Visibility.PUBLIC) {
            return Optional.empty();
        }

        if (requested.getVisibility() == Visibility.SHARED) {
            Module requestedModule = requested.getPrototype().getModule();

            if (currentModule.equals(requestedModule) || requestedModule.isSubmodule(currentModule)) {
                return Optional.empty();
            }

            return Optional.of("Cannot access the property '" + requested + "' outside of the '" + requestedModule.getName() + "' module or its submodules");
        }

        if (requested.getPrototype().equals(currentPrototype)) {
            return Optional.empty();
        }

        return Optional.of("Cannot access the property '" + requested + "' outside of the " + requested.getPrototype().getName() + " prototype or its inheritors");
    }

}
