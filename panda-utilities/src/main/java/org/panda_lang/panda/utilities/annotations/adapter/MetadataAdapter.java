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

package org.panda_lang.panda.utilities.annotations.adapter;

import org.panda_lang.panda.utilities.annotations.AnnotationsScanner;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerFile;

import java.util.List;

public interface MetadataAdapter<C, F, M> {

    boolean acceptsInput(String file);

    boolean isPublic(Object o);

    String getClassName(C cls);

    String getSuperclassName(C cls);

    List<? extends String> getInterfacesNames(C cls);

    List<? extends F> getFields(C cls);

    List<? extends M> getMethods(C cls);

    String getMethodName(M method);

    List<? extends String> getParameterNames(M method);

    List<? extends String> getClassAnnotationNames(C aClass);

    List<? extends String> getFieldAnnotationNames(F field);

    List<? extends String> getMethodAnnotationNames(M method);

    List<? extends String> getParameterAnnotationNames(M method, int parameterIndex);

    String getReturnTypeName(M method);

    String getFieldName(F field);

    C getOfCreateClassObject(AnnotationsScanner scanner, AnnotationsScannerFile file) throws Exception;

    String getMethodModifier(M method);

    String getMethodKey(C cls, M method);

    String getMethodFullKey(C cls, M method);

}
