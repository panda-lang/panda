/*
 * Copyright (c) 2020 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package org.panda_lang.utilities.commons.collection

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import java.util.stream.Collectors

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class NodeTest {

    private static final Node<String> NODE = new Node<String>("A") {{
        Node<String> b = add(new Node<>("B"))
        Node<String> c = b.add(new Node<>("C"))
        Node<String> d = c.add(new Node<>("D"))
        d.add(new Node<>("E"))
    }};

    @Test
    void find() {
        Node<String> e = NODE.find("E")

        assertNotNull e
        assertEquals "E", e.getElement()
    }

    @Test
    void trace() {
        List<Node<String>> trace = NODE.trace("D")

        assertNotNull trace
        assertEquals Arrays.asList("A", "B", "C", "D"), trace.stream()
                .map(node -> node.getElement())
                .collect(Collectors.toList())
    }

    @Test
    void add() {
        assertEquals 1, NODE.getChildren().size()
    }

    @Test
    void isEmpty() {
        assertFalse NODE.isEmpty()
    }

    @Test
    void getElement() {
        assertEquals "A", NODE.getElement()
    }

}