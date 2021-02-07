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

package org.panda_lang.panda.examples.performance.matmul;

import org.panda_lang.utilities.commons.TimeUtils;

/**
 * Better alternative to MatmulJAva
 */
final class MatmulPrimitivesJava {

    public double[][] matgen(int n) {
        double[][] a = new double[n][n];
        double tmp = 1. / n / n;

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                a[i][j] = tmp * (i - j) * (i + j);
            }
        }

        return a;
    }

    public double[][] matmul(double[][] a, double[][] b) {
        int m = a.length,
                n = a[0].length,
                p = b[0].length;

        double[][] x = new double[m][p];
        double[][] c = new double[p][n];

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < p; ++j) {
                c[j][i] = b[i][j];
            }
        }

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < p; ++j) {
                double s = 0.0;

                for (int k = 0; k < n; ++k) {
                    s += a[i][k] * c[j][k];
                }

                x[i][j] = s;
            }
        }

        return x;
    }

    public static void main(String... args) {
        int n = 100;
        long time = System.nanoTime();

        MatmulPrimitivesJava m = new MatmulPrimitivesJava();
        double[][] a, b, x;
        a = m.matgen(n);
        b = m.matgen(n);
        x = m.matmul(a, b);

        System.out.println(x[n / 2][n / 2]);
        System.out.println(TimeUtils.toMilliseconds(System.nanoTime() - time));
    }

}

