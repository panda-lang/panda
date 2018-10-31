package org.panda_lang.panda.utilities.commons;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListUtils {

    /**
     * Get element of list at the given position without risk of {@link java.lang.ArrayIndexOutOfBoundsException}
     *
     * @param list the list to process
     * @param index the index of element to get
     * @param <T> type of the list
     * @return the element at the index position, null if the index is less than 0 or greater than the size of the specified list
     */
    public static <T> @Nullable T get(List<T> list, int index) {
        return index > -1 && index < list.size() ? list.get(index) : null;
    }

}
