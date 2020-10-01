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

package org.panda_lang.panda.language.interpreter.parser.autowired;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.stage.StageManager;
import org.panda_lang.language.interpreter.parser.stage.StagePhase;
import org.panda_lang.language.interpreter.parser.stage.StageLayer;
import org.panda_lang.language.interpreter.parser.stage.StageTask;
import org.panda_lang.utilities.commons.UnsafeUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

final class TaskScheduler<T> {

    protected final AutowiredContent<?> content;
    protected final Stack<? extends AutowiredMethod> methods;

    TaskScheduler(AutowiredContent<?> content, Stack<? extends AutowiredMethod> methods) {
        if (methods.isEmpty()) {
            throw new IllegalArgumentException("Methods stack cannot be empty");
        }

        this.content = content;
        this.methods = methods;
    }

    protected  @Nullable T schedule(Context context) {
        int currentOrder = methods.peek().getOrder();

        while (hasNext(currentOrder)) {
            AutowiredMethod currentMethod = methods.pop();
            boolean last = !hasNext(currentOrder);

            T value = delegateNext(context, currentMethod, last);

            if (last) {
                return value;
            }
        }

        return null;
    }

    private T delegateNext(Context context, AutowiredMethod method, boolean last) {
        StageTask<T> callback = (cycle, delegatedContext) -> {
            T value = null;

            try {
                value = method.getGeneratedMethod().invoke(content.getInstance(), delegatedContext);
            } catch (InvocationTargetException targetException) {
                UnsafeUtils.throwException(targetException.getTargetException());
            } catch (Throwable throwable) {
                UnsafeUtils.throwException(throwable);
            }

            if (last && !methods.isEmpty()) {
                schedule(delegatedContext.fork());
            }

            return value;
        };

        return delegateMethod(context, callback, method);
    }

    private @Nullable T delegateMethod(Context context, StageTask<T> callback, AutowiredMethod method) {
        StageManager stageManager = context.getComponent(Components.STAGE);

        StagePhase cycle = stageManager.getPhase(method.getCycle());
        StageLayer phase = cycle.currentPhase();
        StageLayer nextPhase = cycle.nextPhase();

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
                throw new AutowiredException("Unknown delegation: " + method.getDelegation());
        }

        return null;
    }

    private boolean hasNext(int order) {
        return !methods.isEmpty() && methods.peek().getOrder() == order;
    }

}
