package org.panda_lang.panda.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LoggingUtilsTest {

    @Test
    public void testSkipJansi() {
        LoggingUtils.skipJansi();
        Assertions.assertEquals("true", System.getProperty("log4j.skipJansi"));
    }

}
