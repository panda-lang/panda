package org.panda_lang.panda.utilities.commons.objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.utilities.commons.collection.Sets;

import java.util.List;

class CollectionUtilsTest {

    @Test
    public void testListOf() {
        List<String> list = CollectionUtils.listOf(Sets.newHashSet("a"), Sets.newHashSet("b"));

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, list.size()),
                () -> Assertions.assertTrue(list.contains("a")),
                () -> Assertions.assertTrue(list.contains("b"))
        );
    }

}
