package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.*;
import org.panda_lang.panda.core.parser.essential.assistant.MethodAssistant;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.parser.essential.util.MethodInfo;
import org.panda_lang.panda.core.syntax.*;
import org.panda_lang.panda.core.syntax.Runtime;
import org.panda_lang.panda.core.syntax.block.MethodBlock;

public class MethodParser implements Parser {

    static {
        ParserLayout parserLayout = new ParserLayout(new MethodParser(), "*(*);", EssentialPriority.METHOD.getPriority());
        ParserCenter.registerParser(parserLayout);
    }

    @Override
    public Runtime parse(final Atom atom) {
        final String source = atom.getSourcesDivider().getLine();
        final MethodInfo mi = MethodAssistant.getMethodIndication(atom, source);

        if (mi == null) {
            System.out.println("[MethodParser] Indication failed");
            return null;
        }

        // {method.external}
        if (mi.isExternal()) {

            // {method.static}
            if (mi.isStatic()) {
                final Vial vial = mi.getVial();
                final Method method = vial.getMethod(mi.getMethodName());
                return new Runtime(new Method(mi.getMethodName(), new Executable() {
                    @Override
                    public Essence run(Particle particle) {
                        particle = new Particle(mi.getFactors());
                        return method != null ? method.run(particle) : vial.getMethod(mi.getMethodName()).run(particle);
                    }
                }));

                // {instance.method}
            } else {
                final Factor instance = mi.getInstance();

                if (instance == null) {
                    PandaException exception = new PandaException("MethodParserException: Instance not found", atom.getSourcesDivider());
                    atom.getPandaParser().throwException(exception);
                    return null;
                }

                /*

                #TODO: type from field
                String instanceOf = instance.getDataType();

                // {instance.type.defined}
                if (instanceOf != null) {
                    Vial vial = VialCenter.getVial(instanceOf);
                    final Method method = vial.getMethod(mi.getMethodName());

                    if (method == null) {
                        PandaException exception = new PandaException("MethodParserException: Method not found", atom.getSourcesDivider());
                        atom.getPandaParser().throwException(exception);
                        return null;
                    }

                    return new Runtime(instance, new Executable() {
                        @Override
                        public Essence run(Particle particle) {
                            particle.setInstance(instance);
                            return method.run(particle);
                        }
                    }, mi.getFactors());
                }
                */

                // {instance.type.undefined}
                return new Runtime(instance, new Executable() {
                    @Override
                    public Essence run(Particle particle) {
                        particle.setInstance(instance);
                        return instance.getValue().call(mi.getMethodName(), particle);
                    }
                }, mi.getFactors());
            }

            // {local}
        } else {
            return new Runtime(new Method(mi.getMethodName(), new Executable() {
                @Override
                public Essence run(Particle particle) {
                    return atom.getPandaScript().call(MethodBlock.class, mi.getMethodName(), mi.getFactors());
                }
            }));
        }
    }

}
