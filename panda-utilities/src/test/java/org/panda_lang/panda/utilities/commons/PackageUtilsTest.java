package org.panda_lang.panda.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PackageUtilsTest {

    @Test
    public void testGetPackageName() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("java.lang", PackageUtils.getPackageName(String.class)),
                () -> Assertions.assertNull(PackageUtils.getPackageName(void.class))
        );
    }

}
