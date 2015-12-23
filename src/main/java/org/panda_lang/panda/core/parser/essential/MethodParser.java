package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.PandaException;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.essential.assistant.MethodAssistant;
import org.panda_lang.panda.core.parser.essential.util.MethodInfo;
import org.panda_lang.panda.core.scheme.ParserScheme;
import org.panda_lang.panda.core.syntax.*;
import org.panda_lang.panda.core.syntax.Runtime;

public class MethodParser implements Parser {

    static {
        ParserScheme parserScheme = new ParserScheme(new MethodParser(), "*(*);", EssentialPriority.METHOD.getPriority());
        ElementsBucket.registerParser(parserScheme);
    }

    @Override
    public Runtime parse(Atom atom) {
        final Block parent = atom.getParent();
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
                Method method = new Method(mi.getMethodName(), new Executable() {
                    @Override
                    public Essence run(Particle particle) {
                        particle = new Particle(mi.getParameters());
                        return vial.getMethod(mi.getMethodName()).run(particle);
                    }
                });
                return new Runtime(method);

            // {instance.method}
            } else {
                final Parameter instance = mi.getInstance();

                if (instance == null) {
                    PandaException exception = new PandaException("MethodParserException: Instance not found", atom.getSourcesDivider());
                    atom.getPandaParser().throwException(exception);
                    return null;
                }

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
                    }, mi.getParameters());
                }

                // {instance.type.undefined}
                return new Runtime(instance, new Executable() {
                    @Override
                    public Essence run(Particle particle) {
                        particle.setInstance(instance);
                        return instance.getValue().call(mi.getMethodName(), particle);
                    }
                }, mi.getParameters());
            }
        }

        return null;
    }



}
