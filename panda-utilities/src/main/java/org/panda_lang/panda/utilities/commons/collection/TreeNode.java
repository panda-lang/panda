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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class TreeNode<T> {

    private final T element;
    private final Set<TreeNode<T>> children;

    public TreeNode(T element) {
        this.element = element;
        this.children = new HashSet<>();
    }

    public Set<T> collectLeafs(Predicate<T> filter) {
        Set<T> leafs = new HashSet<>();

        for (TreeNode<T> child : children) {
            leafs.addAll(child.collectLeafs(filter));
        }

        if (leafs.isEmpty() && filter.test(getElement())) {
            leafs.add(getElement());
        }

        return leafs;
    }

    public void add(TreeNode<T> node) {
        this.children.add(node);
    }

    public void add(Collection<TreeNode<T>> nodes) {
        this.children.addAll(nodes);
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    public Set<TreeNode<T>> getChildren() {
        return children;
    }

    public T getElement() {
        return element;
    }

}
