/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.utilities.commons.collection;

public interface IStack<T> {

    /**
     * Pushes an item onto the top of this stack
     *
     * @param element the item to be pushed onto this stack
     */
    void push(T element);

    /**
     * Check element on top of the stack
     *
     * @return the value on top
     */
    T peek();

    /**
     * Removes the object at the top of this stack
     * and returns that object as the value
     *
     * @return the object at the top of this stack
     */
    T pop();

    /**
     * Clear the stack
     */
    void clear();

    /**
     * Returns the number of items in this stack
     *
     * @return the number of items
     */
    int size();

    /**
     * Tests if stack is empty
     *
     * @return true if and only if this stack contains no items
     */
    boolean isEmpty();

    /**
     * Converts stack into the array,
     * ordered by the push order (the latest items are the first in array)
     *
     * @return the array of items from stack
     */
    T[] toArray(Class<T[]> type);

}
