/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationType;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.Delegation;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.InterceptorData;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LayerMethod;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LocalData;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UnifiedBootstrapParser implements UnifiedParser {

    private final PandaParserBootstrap bootstrap;
    private final List<? extends LayerMethod> layers;
    private final ParserLayerGenerator generator;
    private final int index;

    public UnifiedBootstrapParser(PandaParserBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.layers = bootstrap.getLayers();
        this.generator = new ParserLayerGenerator(this);

        this.index = layers.stream()
                .min(Comparator.comparingInt(LayerMethod::getOrder))
                .map(LayerMethod::getOrder)
                .get();
    }

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) throws Exception {
        InterceptorData interceptorData = bootstrap.hasInterceptor() ? bootstrap.getInterceptor().handle(this, data) : new InterceptorData();
        return delegate(data, nextLayer, interceptorData, new LocalData(), index);
    }

    protected boolean delegate(ParserData data, GenerationLayer nextLayer, InterceptorData interceptorData, LocalData localData, int lastIndex) throws Exception {
        CasualParserGeneration generation = data.getComponent(UniversalComponents.GENERATION);
        GenerationLayer currentLayer = generation.getLayer(CasualParserGenerationType.CURRENT);

        List<LayerMethod> methods = layers.stream()
                .filter((method) -> method.getOrder() == lastIndex)
                .sorted(Comparator.comparingInt(method -> method.getDelegation().getPriority()))
                .collect(Collectors.toList());

        for (int i = 0; i < methods.size(); i++) {
            CasualParserGenerationCallback callback = generator.callback(interceptorData, localData, methods.get(i), lastIndex + methods.size(), methods.size() == i + 1);
            delegate(data, callback, methods.get(i).getDelegation(), currentLayer, nextLayer);
        }

        return true;
    }

    private void delegate(ParserData data, CasualParserGenerationCallback callback, Delegation delegation, GenerationLayer currentLayer, GenerationLayer nextLayer) throws Exception {
        switch (delegation) {
            case IMMEDIATELY:
                callback.call(data, nextLayer);
                break;
            case CURRENT_BEFORE:
                currentLayer.delegateBefore(callback, data);
                break;
            case CURRENT_DEFAULT:
                currentLayer.delegate(callback, data);
                break;
            case CURRENT_AFTER:
                currentLayer.delegate(callback, data);
                break;
            case NEXT_BEFORE:
                nextLayer.delegateBefore(callback, data);
                break;
            case NEXT_DEFAULT:
                nextLayer.delegate(callback, data);
                break;
            case NEXT_AFTER:
                nextLayer.delegateAfter(callback, data);
                break;
        }
    }

    protected int getIndex() {
        return index;
    }

    protected List<? extends LayerMethod> getLayers() {
        return layers;
    }

    protected PandaParserBootstrap getBootstrap() {
        return bootstrap;
    }

}
