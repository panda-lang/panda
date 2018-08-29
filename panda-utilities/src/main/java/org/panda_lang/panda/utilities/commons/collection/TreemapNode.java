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

package org.panda_lang.panda.utilities.commons.collection;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class TreemapNode<T> {

    private final T element;
    private final Function<T, String> mapper;
    private final Map<String, TreemapNode<T>> children;

    public TreemapNode(T element, Function<T, String> mapper) {
        this.element = element;
        this.mapper = mapper;
        this.children = new HashMap<>();
    }

    public Set<T> collectLeafs(Predicate<T> filter) {
        Set<T> leafs = new HashSet<>();

        for (Map.Entry<String, TreemapNode<T>> child : children.entrySet()) {
            leafs.addAll(child.getValue().collectLeafs(filter));
        }

        if (leafs.isEmpty() && filter.test(getElement())) {
            leafs.add(getElement());
        }

        return leafs;
    }

    public void add(Collection<TreemapNode<T>> nodes) {
        nodes.forEach(this::add);
    }

    public void add(TreemapNode<T> node) {
        children.put(mapper.apply(node.getElement()), node);
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    public @Nullable T get(String elementName) {
        TreemapNode<? extends T> element = getNode(elementName);
        return element != null ? element.getElement() : null;
    }

    public Collection<TreemapNode<? extends T>> getNodesStartingWith(String str) {
        Collection<TreemapNode<? extends T>> nodes = new ArrayList<>();

        for (Map.Entry<String, TreemapNode<T>> entry : children.entrySet()) {
            if (entry.getKey().startsWith(str)) {
                nodes.add(entry.getValue());
            }
        }

        return nodes;
    }

    public @Nullable TreemapNode<? extends T> getNode(String nodeName) {
        return children.get(nodeName);
    }

    public Collection<TreemapNode<T>> getChildren() {
        return children.values();
    }

    public String getName() {
        return mapper.apply(element);
    }

    public T getElement() {
        return element;
    }

}