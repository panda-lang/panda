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

package org.panda_lang.panda.language.interpreter.parser.bootstraps.context;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.data.InterceptorData;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.data.LocalData;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.generation.Generation;
import org.panda_lang.framework.design.interpreter.parser.generation.GenerationCycle;
import org.panda_lang.framework.design.interpreter.parser.generation.GenerationPhase;
import org.panda_lang.framework.design.interpreter.parser.generation.GenerationTask;
import org.panda_lang.utilities.inject.DependencyInjection;
import org.panda_lang.utilities.inject.Injector;
import org.panda_lang.utilities.inject.InjectorController;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

final class BootstrapTaskScheduler<T> {

    protected final BootstrapContent content;
    protected final Stack<? extends BootstrapMethod> methods;

    BootstrapTaskScheduler(BootstrapContent content, Stack<? extends BootstrapMethod> methods) {
        if (methods.isEmpty()) {
            throw new IllegalArgumentException("Methods stack cannot be empty");
        }

        this.content = content;
        this.methods = methods;
    }

    protected T schedule(Context context, InterceptorData interceptorData, LocalData localData) throws Exception {
        return schedule(context, new BootstrapInjectorController(context, interceptorData, localData));
    }

    private @Nullable T schedule(Context context, InjectorController controller) throws Exception {
        Injector injector = DependencyInjection.createInjector(controller);
        int currentOrder = methods.peek().getOrder();

        while (hasNext(currentOrder)) {
            BootstrapMethod currentMethod = methods.pop();
            boolean last = !hasNext(currentOrder);

            T value = delegateNext(context, controller, injector, currentMethod, last);

            if (last) {
                return value;
            }
        }

        return null;
    }

    private T delegateNext(Context context, InjectorController controller, Injector injector, BootstrapMethod method, boolean last) throws Exception {
        GenerationTask<T> callback = (cycle, delegatedContext) -> {
            T value;

            try {
                value = injector.invokeMethod(method.getMethod(), content.getInstance());
            }
            catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof Exception) {
                    throw (Exception) e.getTargetException();
                }

                throw new BootstrapException("Error occurred: " + e.getMessage(), e.getTargetException());
            }
            catch (Exception e) {
                throw e;
            }
            catch (Throwable throwable) {
                throw new BootstrapException("Internal error: " + throwable.getMessage(), throwable);
            }

            if (last && !methods.isEmpty()) {
                schedule(delegatedContext.fork(), controller);
            }

            return value;
        };

        return delegateMethod(context, callback, method);
    }

    private @Nullable T delegateMethod(Context context, GenerationTask<T> callback, BootstrapMethod method) throws Exception {
        Generation generation = context.getComponent(Components.GENERATION);

        GenerationCycle cycle = generation.getCycle(method.getCycle());
        GenerationPhase phase = cycle.currentPhase();
        GenerationPhase nextPhase = cycle.nextPhase();

        switch (method.getDelegation()) {
            case IMMEDIATELY:
                return callback.call(cycle, context);
            case CURRENT_BEFORE:
                phase.delegateBefore(callback, context);
                break;
            case CURRENT_DEFAULT:
                phase.delegate(callback, context);
                break;
            case CURRENT_AFTER:
                phase.delegateAfter(callback, context);
                break;
            case NEXT_BEFORE:
                nextPhase.delegateBefore(callback, context);
                break;
            case NEXT_DEFAULT:
                nextPhase.delegate(callback, context);
                break;
            case NEXT_AFTER:
                nextPhase.delegateAfter(callback, context);
                break;
            default:
                throw new BootstrapException("Unknown delegation: " + method.getDelegation());
        }

        return null;
    }

    private boolean hasNext(int order) {
        return !methods.isEmpty() && methods.peek().getOrder() == order;
    }

}
