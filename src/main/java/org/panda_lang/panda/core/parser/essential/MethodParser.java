package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.PandaException;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.essential.assistant.MethodAssistant;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.parser.essential.util.MethodInfo;
import org.panda_lang.panda.core.statement.*;
import org.panda_lang.panda.core.statement.Runtime;
import org.panda_lang.panda.core.statement.block.MethodBlock;

public class MethodParser implements Parser {

    public static void initialize(Panda panda) {
        MethodParser methodParser = new MethodParser();
        ParserLayout parserLayout = new ParserLayout(methodParser, "*(*);", EssentialPriority.METHOD.getPriority());
        panda.getPandaCore().registerParser(parserLayout);
    }

    @Override
    public Runtime parse(final ParserInfo parserInfo) {
        final String source = parserInfo.getSourcesDivider().getLine();
        final MethodInfo mi = MethodAssistant.getMethodIndication(parserInfo, source);

        if (mi == null) {
            System.out.println("[MethodParser] Indication failed");
            return null;
        }

        // {method.external}
        if (mi.isExternal()) {

            // {method.static}
            if (mi.isStatic()) {
                final Vial vial = mi.getVial();
                return new Runtime(new Method(mi.getMethodName(), new Executable() {
                    @Override
                    public Essence execute(Alice alice) {
                        Alice fork = alice
                                .fork()
                                .pandaScript(parserInfo.getPandaScript())
                                .factors(mi.getRuntimeValues());
                        return vial.call(mi.getMethodName(), fork);
                    }
                }));

                // {instance.method}
            }
            else {
                final RuntimeValue instance = mi.getInstance();

                if (instance == null) {
                    PandaException exception = new PandaException("MethodParserException: Instance not found", parserInfo.getSourcesDivider());
                    return parserInfo.getPandaParser().throwException(exception);
                }

                String instanceOf = instance.getDataType();

                // {instance.type.defined}
                if (instanceOf != null) {
                    Vial vial = parserInfo.getDependencies().getVial(instanceOf);
                    final Method method = vial.getMethod(mi.getMethodName());

                    if (method == null) {
                        PandaException exception = new PandaException("MethodParserException: Method not found", parserInfo.getSourcesDivider());
                        return parserInfo.getPandaParser().throwException(exception);
                    }

                    return new Runtime(instance, new Executable() {
                        @Override
                        public Essence execute(Alice alice) {
                            alice.setInstance(instance);
                            Essence essence = instance.getValue(alice);
                            alice = essence.particle(alice);
                            return method.execute(alice);
                        }
                    }, mi.getRuntimeValues());
                }

                // {instance.type.undefined}
                return new Runtime(instance, new Executable() {
                    @Override
                    public Essence execute(Alice alice) {
                        alice.setInstance(instance);
                        Essence essence = instance.getValue(alice);
                        String methodName = mi.getMethodName();
                        return essence.call(methodName, alice);
                    }
                }, mi.getRuntimeValues());
            }

            // {local}
        }
        else {
            return new Runtime(new Method(mi.getMethodName(), new Executable() {
                @Override
                public Essence execute(Alice alice) {
                    return parserInfo.getPandaScript().call(MethodBlock.class, mi.getMethodName(), mi.getRuntimeValues());
                }
            }));
        }
    }

}
