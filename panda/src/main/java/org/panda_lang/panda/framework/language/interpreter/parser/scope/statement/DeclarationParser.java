package org.panda_lang.panda.framework.language.interpreter.parser.scope.statement;

import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.AutowiredParameters;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Type;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.interpreter.parser.scope.statement.assignation.subparsers.variable.VariableInitializer;

@ParserRegistration(target = PandaPipelines.SCOPE_LABEL, priority = PandaPriorities.SCOPE_DECLARATION_PARSER)
public class DeclarationParser extends UnifiedParserBootstrap {

    @Override
    protected BootstrapParserBuilder initialize(BootstrapParserBuilder defaultBuilder) {
        return defaultBuilder.pattern(VariableInitializer.DECLARATION_PARSER);
    }

    @Autowired
    @AutowiredParameters(skip = 2, value = {
            @Type(with = Component.class),
            @Type(with = Src.class, value = "type"),
            @Type(with = Src.class, value = "name")
    })
    public void parse(ParserData data, ExtractorResult result, ScopeLinker linker, Tokens type, Tokens name) {
        PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
        boolean mutable = result.hasIdentifier("mutable");
        boolean nullable = result.hasIdentifier("nullable");

        VariableInitializer initializer = new VariableInitializer();
        initializer.createVariable(script.getModuleLoader(), linker.getCurrentScope(), mutable, nullable, type.asString(), name.asString());
    }

}
