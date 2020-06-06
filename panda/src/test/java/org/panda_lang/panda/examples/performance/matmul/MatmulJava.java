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

package org.panda_lang.panda.examples.performance.matmul;

import org.panda_lang.utilities.commons.TimeUtils;

/**
 * Java equivalent to matmul.panda
 */
@SuppressWarnings("WrapperTypeMayBePrimitive")
final class MatmulJava {

    Number[][] matgen(Number n) {
        Number[][] a = new Number[n.intValue()][n.intValue()];
        Double tmp = 1.0 / n.intValue() / n.intValue();

        for (Number i = 0; i.intValue() < n.intValue(); i = i.intValue() + 1) {
            for (Number j = 0; j.intValue() < n.intValue(); j = j.intValue() + 1) {
                a[i.intValue()][j.intValue()] = tmp * (i.intValue() - j.intValue()) * (i.intValue() + j.intValue());
            }
        }

        return a;
    }

    Number[][] matmul(Number[][] a, Number[][] b) {
        Number m = a.length,
                n = a[0].length,
                p = b[0].length;

        Number[][] x = new Double[m.intValue()][p.intValue()];
        Number[][] c = new Double[p.intValue()][n.intValue()];

        for (Number i = 0; i.intValue() < n.intValue(); i = i.intValue() + 1) {
            for (Number j = 0; j.intValue() < p.intValue(); j = j.intValue() + 1) {
                c[j.intValue()][i.intValue()] = b[i.intValue()][j.intValue()];
            }
        }

        for (Number i = 0; i.intValue() < m.intValue(); i = i.intValue() + 1) {
            for (Number j = 0; j.intValue() < p.intValue(); j = j.intValue() + 1) {
                Number s = 0.0;

                for (Number k = 0; k.intValue() < n.intValue(); k = k.intValue() + 1) {
                    s = s.doubleValue() + a[i.intValue()][k.intValue()].doubleValue() * c[j.intValue()][k.intValue()].doubleValue();
                }

                x[i.intValue()][j.intValue()] = s.doubleValue();
            }
        }

        return x;
    }

    public static void main(String... args) {
        Integer n = 100;
        Long time = System.nanoTime();

        MatmulJava m = new MatmulJava();
        Number[][] a, b, x;
        a = m.matgen(n);
        b = m.matgen(n);
        x = m.matmul(a, b);

        System.out.println(x[n / 2][n / 2]);
        System.out.println(TimeUtils.toMilliseconds(System.nanoTime() - time));
    }

}

