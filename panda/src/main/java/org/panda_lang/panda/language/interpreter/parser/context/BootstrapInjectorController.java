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

package org.panda_lang.panda.language.interpreter.parser.context;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.LocalChannel;
import org.panda_lang.framework.language.interpreter.pattern.Mappings;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.inject.InjectorController;
import org.panda_lang.utilities.inject.InjectorResources;

import java.lang.reflect.Parameter;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

final class BootstrapInjectorController implements InjectorController {

    @Override
    public void initialize(InjectorResources resources) {
        resources.on(Context.class).assignHandler((parameter, annotation, injectorArgs) -> context(injectorArgs));
        resources.on(LocalChannel.class).assignHandler((parameter, annotation, injectorArgs) -> channel(context(injectorArgs)));

        resources.annotatedWith(Ctx.class).assignHandler((required, annotation, injectorArgs) -> {
            return findComponent(context(injectorArgs), annotation, required);
        });

        resources.annotatedWith(Channel.class).assignHandler((required, annotation, injectorArgs) -> {
            return findInChannel(context(injectorArgs), annotation, required);
        });

        resources.annotatedWith(Src.class).assignHandler((required, annotation, injectorArgs) -> {
            return findSource(context(injectorArgs), annotation, required);
        });
    }

    private @Nullable Object findComponent(Context context, Ctx ctx, Parameter required) {
        return context.getComponents().entrySet().stream()
                .filter(entry -> {
                    String value = ctx.value();

                    if (!StringUtils.isEmpty(value) && Objects.equals(entry.getKey().getName(), value)) {
                        return true;
                    }

                    return required.getType() == entry.getKey().getType();
                })
                .sorted(Comparator.comparingDouble(entry -> entry.getKey().getPriority()))
                .map(Map.Entry::getValue)
                // .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private @Nullable Object findSource(Context context, Src src, Parameter required) {
        LocalChannel channel = channel(context);

        if (!channel.contains(Mappings.class)) {
            throw new BootstrapException("Pattern mappings are not defined for @Redactor");
        }

        Mappings redactor = channel.get(Mappings.class);
        Object value = redactor.get(src.value()).getOrNull();
        Class<?> requiredType = required.getType();

        if (value != null && requiredType == String.class) {
            return value.toString();
        }

        if (value != null && !requiredType.isAssignableFrom(value.getClass())) {
            throw new BootstrapException("Cannot match types: " + requiredType + " != " + value.getClass());
        }

        return value;
    }

    private @Nullable Object findInChannel(Context context, Channel channelAnnotation, Parameter required) {
        String name = channelAnnotation.value();
        LocalChannel channel = channel(context);

        if (!StringUtils.isEmpty(name)) {
            return channel.get(name);
        }

        return channel.get(required.getType());
    }

    private Context context(Object[] injectorArgs) {
        return Objects.requireNonNull(ObjectUtils.cast(injectorArgs[0]));
    }

    private LocalChannel channel(Context context) {
        return context.getComponent(Components.CHANNEL);
    }

}
