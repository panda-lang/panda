package org.panda_lang.panda.utilities.commons.iterable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.utilities.commons.collection.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class ResourcesIterableTest {

    @Test
    public void testIterable() {
        Collection<String> mutable = Lists.mutableOf("a");
        Collection<String> immutable = Collections.singletonList("c");
        ResourcesIterable<String> iterable = new ResourcesIterable<>(mutable, immutable);

        mutable.add("b");
        Iterator<String> iterator = iterable.iterator();

        Assertions.assertEquals("a", iterator.next());
        Assertions.assertEquals("b", iterator.next());
        Assertions.assertEquals("c", iterator.next());
    }

}
