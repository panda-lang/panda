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

package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.data.InterceptorData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.data.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.GenerationCycle;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.GenerationPhase;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.GenerationTask;
import org.panda_lang.panda.utilities.inject.DependencyInjection;
import org.panda_lang.panda.utilities.inject.Injector;
import org.panda_lang.panda.utilities.inject.InjectorController;

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

    protected T schedule(ParserData data, InterceptorData interceptorData, LocalData localData) throws Exception {
        return schedule(data, new BootstrapInjectorController(data, interceptorData, localData));
    }

    private T schedule(ParserData data, InjectorController controller) throws Exception {
        Injector injector = DependencyInjection.createInjector(controller);
        int currentOrder = methods.peek().getOrder();

        while (hasNext(currentOrder)) {
            BootstrapMethod currentMethod = methods.pop();
            boolean last = !hasNext(currentOrder);

            T value = delegateNext(data, controller, injector, currentMethod, last);

            if (last) {
                return value;
            }
        }

        return null;
    }

    private T delegateNext(ParserData data, InjectorController controller, Injector injector, BootstrapMethod method, boolean last) throws Exception {
        GenerationTask<T> callback = (cycle, delegatedData) -> {
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

            if (last && !methods.isEmpty()) {
                schedule(delegatedData.fork(), controller);
            }

            return value;
        };

        return delegateMethod(data, callback, method);
    }

    private T delegateMethod(ParserData data, GenerationTask<T> callback, BootstrapMethod method) throws Exception {
        Generation generation = data.getComponent(UniversalComponents.GENERATION);

        GenerationCycle cycle = generation.getCycle(method.getCycle());
        GenerationPhase phase = cycle.currentPhase();
        GenerationPhase nextPhase = cycle.nextPhase();

        switch (method.getDelegation()) {
            case IMMEDIATELY:
                return callback.call(cycle, data);
            case CURRENT_BEFORE:
                phase.delegateBefore(callback, data);
                break;
            case CURRENT_DEFAULT:
                phase.delegate(callback, data);
                break;
            case CURRENT_AFTER:
                phase.delegateAfter(callback, data);
                break;
            case NEXT_BEFORE:
                nextPhase.delegateBefore(callback, data);
                break;
            case NEXT_DEFAULT:
                nextPhase.delegate(callback, data);
                break;
            case NEXT_AFTER:
                nextPhase.delegateAfter(callback, data);
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
