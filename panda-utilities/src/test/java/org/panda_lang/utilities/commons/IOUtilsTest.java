/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class IOUtilsTest {

    @SuppressWarnings("SpellCheckingInspection")
    private static final String MESSAGE = "ZAŻÓŁĆ GĘŚLĄ JAŹŃ";

    @Test
    void getURLContent() {
        String urlContent = IOUtils.getURLContent("https://panda-lang.org/");
        Assertions.assertNotNull(urlContent);
        Assertions.assertTrue(urlContent.contains("<html"));
    }

    @Test
    void convertStringToStream() throws IOException {
        InputStream stream = IOUtils.convertStringToStream(MESSAGE);
        Assertions.assertNotNull(stream);
        Assertions.assertTrue(stream.available() > 0);
    }

    @Test
    void convertStreamToString() {
        Assertions.assertEquals(MESSAGE, IOUtils.convertStreamToString(IOUtils.convertStringToStream(MESSAGE)));
    }

    @Test
    void close() {
        ByteArrayOutputStream stubStream = new ByteArrayOutputStream(0) {
            private boolean closed;

            @Override
            public synchronized void write(int b) {
                if (closed) {
                    throw new RuntimeException("Stream is already closed");
                }
                super.write(b);
            }

            @Override
            public synchronized void close() throws IOException {
                this.closed = true;
                super.close();
            }
        };

        IOUtils.close(stubStream);
        Assertions.assertThrows(RuntimeException.class, () -> stubStream.write(0x00));
    }

}