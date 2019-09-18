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

package org.panda_lang.framework.language.architecture.statement;

import org.panda_lang.framework.design.architecture.dynamic.LivingFrame;
import org.panda_lang.framework.design.architecture.prototype.PropertyLivingFrame;
import org.panda_lang.framework.design.architecture.statement.Frame;
import org.panda_lang.framework.language.architecture.dynamic.AbstractLivingFrame;

public class ParametrizedLivingFrame<T extends Frame>  extends AbstractLivingFrame<T> implements PropertyLivingFrame {

    private final LivingFrame instance;

    public ParametrizedLivingFrame(T frame, LivingFrame instance) {
        super(frame);
        this.instance = instance;
    }

    @Override
    public LivingFrame getInstance() {
        return instance;
    }

}
