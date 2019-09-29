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

package org.panda_lang.panda.language.resource.head;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.prototype.generator.ClassPrototypeGeneratorManager;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.utilities.commons.ClassUtils;

import java.util.Optional;

final class ConveyanceUtils {

    private ConveyanceUtils() { }

    protected static Reference fetchReference(Context context, Snippet classNameSource) {
        Optional<Class<?>> importedClass = ClassUtils.forName(classNameSource.asSource());

        if (!importedClass.isPresent()) {
            throw new PandaParserFailure(context, classNameSource, "Class " + classNameSource.asSource() + " does not exist");
        }

        Module module = context.getComponent(Components.SCRIPT).getModule();
        Class<?> clazz = importedClass.get();
        String className = clazz.getSimpleName();

        return ClassPrototypeGeneratorManager.getInstance().generate(module, clazz, className);
    }

}
