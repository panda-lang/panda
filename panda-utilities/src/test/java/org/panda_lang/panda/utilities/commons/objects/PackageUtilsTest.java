package org.panda_lang.panda.utilities.commons.objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PackageUtilsTest {

    @Test
    public void testGetPackageName() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("java.lang", PackageUtils.getPackageName(String.class)),
                () -> Assertions.assertNull(PackageUtils.getPackageName(void.class))
        );
    }

}
