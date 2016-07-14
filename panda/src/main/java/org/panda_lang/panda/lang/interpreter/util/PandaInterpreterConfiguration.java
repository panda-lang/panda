package org.panda_lang.panda.lang.interpreter.util;

import org.panda_lang.core.interpreter.parser.representation.ParserRepresentationPipeline;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.lang.interpreter.PandaInterpreter;

/**
 * PandaInterpreterConfiguration contains the necessary elements for PandaInterpreter.
 *
 * @see PandaInterpreter
 */
public class PandaInterpreterConfiguration {

    private final ParserRepresentationPipeline parserRepresentationPipeline;

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
        this.parserRepresentationPipeline = pandaInterpreterConfiguration.getParserRepresentationPipeline();
    }

    /**
     * Configuration based on configuration builder
     *
     * @param pandaInterpreterConfiguration configuration builder
     * @see PandaInterpreterConfigurationBuilder
     */
    public PandaInterpreterConfiguration(PandaInterpreterConfigurationBuilder pandaInterpreterConfiguration) {
        this.parserRepresentationPipeline = pandaInterpreterConfiguration.pipeline;
    }

    public ParserRepresentationPipeline getParserRepresentationPipeline() {
        return parserRepresentationPipeline;
    }

    /**
     * @return PandaInterpreterConfigurationBuilder with fields filled on the base of the current configuration.
     * @see PandaInterpreterConfigurationBuilder
     */
    public PandaInterpreterConfigurationBuilder toBuilder() {
        return builder().pipeline(parserRepresentationPipeline);
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

        private ParserRepresentationPipeline pipeline;

        public PandaInterpreterConfigurationBuilder pipeline(ParserRepresentationPipeline parserRepresentationPipeline) {
            this.pipeline = parserRepresentationPipeline;
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
