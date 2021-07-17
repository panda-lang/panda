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

package panda.interpreter;

import panda.interpreter.logging.Logger;
import panda.interpreter.logging.SystemLogger;
import panda.interpreter.resource.syntax.PandaSyntax;
import panda.interpreter.bootstrap.PandaBootstrap;
import panda.interpreter.syntax.PandaParsers;

/**
 * Simplify creation of Panda instance
 */
public final class PandaFactory {

    public Panda createPanda() {
        return createPanda(new SystemLogger());
    }

    /**
     * Create default instance of Panda
     *
     * @param logger logger to use by framework
     * @return a new instance of panda
     */
    public Panda createPanda(Logger logger) {
        return PandaBootstrap.initializeBootstrap(logger)
                // load syntax
                .withSyntax(new PandaSyntax())

                // load pipelines
                .initializePipelines()
                    .collect()

                // load parsers and expressions subparsers
                .initializeParsers()
                    .loadParsers(PandaParsers.PARSERS)
                    // .loadParsers(AssignationParsers.SUBPARSERS)
                    .loadDefaultExpressionSubparsers()
                    .collect()

                // load models
                .create();
    }

}
