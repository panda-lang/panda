package org.panda_lang.panda.utilities.commons.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {

    /**
     * Join collections into the list
     *
     * @param collections collections to merge
     * @param <T> generic type of the collections
     * @return the list with the specified collections
     */
    @SafeVarargs
    public static <T> List<T> listOf(Collection<? extends T>... collections) {
        List<T> list = new ArrayList<>();

        for (Collection<? extends T> collection : collections) {
            list.addAll(collection);
        }

        return list;
    }

}
