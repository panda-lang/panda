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
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationPipeline;
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
    public boolean parse(ParserData data) throws Throwable {
        InterceptorData interceptorData = bootstrap.hasInterceptor() ? bootstrap.getInterceptor().handle(this, data) : new InterceptorData();
        return delegate(data, data.getComponent(UniversalComponents.GENERATION), interceptorData, new LocalData(), index);
    }

    protected boolean delegate(ParserData data, Generation generation, InterceptorData interceptorData, LocalData localData, int lastIndex) throws Throwable {
        List<LayerMethod> methods = layers.stream()
                .filter((method) -> method.getOrder() == lastIndex)
                .sorted(Comparator.comparingInt(method -> method.getDelegation().getPriority()))
                .collect(Collectors.toList());

        for (int i = 0; i < methods.size(); i++) {
            GenerationCallback callback = generator.callback(interceptorData, localData, methods.get(i), lastIndex + methods.size(), methods.size() == i + 1);
            delegate(generation, data, callback, methods.get(i));
        }

        return true;
    }

    private void delegate(Generation generation, ParserData data, GenerationCallback callback, LayerMethod method) throws Throwable {
        GenerationPipeline pipeline = generation.pipeline(method.getType());
        GenerationLayer currentLayer = pipeline.currentLayer();
        GenerationLayer nextLayer = pipeline.nextLayer();

        switch (method.getDelegation()) {
            case IMMEDIATELY:
                callback.call(pipeline, data);
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
            default:
                throw new ParserBootstrapException("Unknown delegation: " + method.getDelegation());
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
