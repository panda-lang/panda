package org.panda_lang.panda.lang.interpreter.util;

import org.panda_lang.core.interpreter.parser.ParserRepresentationPool;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.lang.interpreter.PandaInterpreter;

/**
 * PandaInterpreterConfiguration contains the necessary elements for PandaInterpreter.
 *
 * @see PandaInterpreter
 */
public class PandaInterpreterConfiguration {

    private final ParserRepresentationPool parserRepresentationPool;

    /**
     * Configuration based on default panda configuration.
     *
     * @param panda necessary to get the default configuration
     * @see         org.panda_lang.panda.PandaComposition
     */
    public PandaInterpreterConfiguration(Panda panda) {
        this(panda.getPandaComposition().getPandaInterpreterConfiguration());
    }

    /**
     * Configuration based on other configuration
     *
     * @param pandaInterpreterConfiguration configuration to be copied
     */
    public PandaInterpreterConfiguration(PandaInterpreterConfiguration pandaInterpreterConfiguration) {
        this.parserRepresentationPool = pandaInterpreterConfiguration.getParserRepresentationPool();
    }

    /**
     * Configuration based on configuration builder
     *
     * @param pandaInterpreterConfiguration configuration builder
     * @see PandaInterpreterConfigurationBuilder
     */
    public PandaInterpreterConfiguration(PandaInterpreterConfigurationBuilder pandaInterpreterConfiguration) {
        this.parserRepresentationPool = pandaInterpreterConfiguration.parserRepresentationPool;
    }

    public ParserRepresentationPool getParserRepresentationPool() {
        return parserRepresentationPool;
    }

    /**
     * @return PandaInterpreterConfigurationBuilder with fields filled on the base of the current configuration.
     * @see PandaInterpreterConfigurationBuilder
     */
    public PandaInterpreterConfigurationBuilder toBuilder() {
        return builder().parserPool(parserRepresentationPool);
    }

    /**
     * @return a new instance of configuration builder
     */
    public static PandaInterpreterConfigurationBuilder builder() {
        return new PandaInterpreterConfigurationBuilder();
    }

    /**
     * PandaInterpreterConfigurationBuilder is a configuration builder for PandaInterpreterConfiguration.
     *
     * @see PandaInterpreterConfiguration
     */
    public static class PandaInterpreterConfigurationBuilder {

        private ParserRepresentationPool parserRepresentationPool;

        public PandaInterpreterConfigurationBuilder parserPool(ParserRepresentationPool parserRepresentationPool) {
            this.parserRepresentationPool = parserRepresentationPool;
            return this;
        }

        /**
         * @return a new instance of PandaInterpreterConfiguration based on the current builder
         * @see PandaInterpreterConfiguration
         */
        public PandaInterpreterConfiguration build() {
            return new PandaInterpreterConfiguration(this);
        }

    }

}
