/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.architecture.type.utils;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.architecture.type.Property;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.source.Source;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;

import org.panda_lang.utilities.commons.function.Option;

public final class VisibilityComparator {

    public static final String NOTE_MESSAGE = "You may want to change the architecture of your application or you can just simply hack it";

    public static boolean requireAccess(Property requested, Context context, Snippetable source) {
        canAccess(requested, context).peek(message -> {
            throw new PandaParserFailure(context, source, message, NOTE_MESSAGE);
        });
        return true;
    }

    public static Option<String> canAccess(Property requested, Context context) {
        return canAccess(requested, context.getComponent(Components.SCRIPT).getModule(), context.getComponent(Components.CURRENT_SOURCE).getLocation().getSource());
    }

    public static Option<String> canAccess(Property requested, Module currentModule, @Nullable Source currentSource) {
        if (requested.getVisibility() == Visibility.OPEN) {
            return Option.none();
        }

        if (requested.getVisibility() == Visibility.SHARED) {
            Module requestedModule = requested.getType().getModule();

            if (currentModule.equals(requestedModule) || requestedModule.isSubmodule(currentModule)) {
                return Option.none();
            }

            return Option.of("Cannot access the property '" + requested + "' outside of the '" + requestedModule.getName() + "' module or its submodules");
        }

        if (requested.getType().getLocation().getSource().equals(currentSource)) {
            return Option.none();
        }

        return Option.of("Cannot access the property '" + requested + "' outside of the " + requested.getType().getLocation().getSource().getId() + " source");
    }

}
