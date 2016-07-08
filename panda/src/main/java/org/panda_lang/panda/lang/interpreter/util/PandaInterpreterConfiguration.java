package org.panda_lang.panda.lang.interpreter.util;

import org.panda_lang.core.interpreter.parser.ParserRepresentationRegistry;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.lang.interpreter.PandaInterpreter;

/**
 * PandaInterpreterConfiguration contains the necessary elements for PandaInterpreter.
 *
 * @see PandaInterpreter
 */
public class PandaInterpreterConfiguration {

    private final ParserRepresentationRegistry parserRepresentationRegistry;

    /**
     * Configuration based on default panda configuration.
     *
     * @param panda necessary to get the default configuration
     * @see org.panda_lang.panda.PandaComposition
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
        this.parserRepresentationRegistry = pandaInterpreterConfiguration.getParserRepresentationRegistry();
    }

    /**
     * Configuration based on configuration builder
     *
     * @param pandaInterpreterConfiguration configuration builder
     * @see PandaInterpreterConfigurationBuilder
     */
    public PandaInterpreterConfiguration(PandaInterpreterConfigurationBuilder pandaInterpreterConfiguration) {
        this.parserRepresentationRegistry = pandaInterpreterConfiguration.parserRepresentationRegistry;
    }

    public ParserRepresentationRegistry getParserRepresentationRegistry() {
        return parserRepresentationRegistry;
    }

    /**
     * @return PandaInterpreterConfigurationBuilder with fields filled on the base of the current configuration.
     * @see PandaInterpreterConfigurationBuilder
     */
    public PandaInterpreterConfigurationBuilder toBuilder() {
        return builder().parserPool(parserRepresentationRegistry);
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

        private ParserRepresentationRegistry parserRepresentationRegistry;

        public PandaInterpreterConfigurationBuilder parserPool(ParserRepresentationRegistry parserRepresentationRegistry) {
            this.parserRepresentationRegistry = parserRepresentationRegistry;
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
