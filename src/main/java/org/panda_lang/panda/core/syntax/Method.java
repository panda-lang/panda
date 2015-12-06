package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

public class Method implements NamedExecutable {

    /*
    private final Block block;
    private final String method;
    private final Parameter[] parameters;
    private Parameter instance;
    private Executable runnable;
    private PandaScript script;
*/

    private final String methodName;
    private final Executable executable;

    public Method(NamedExecutable executable) {
        this(executable.getName(), executable);
    }

    public Method(String methodName, Executable executable) {
        this.methodName = methodName;
        this.executable = executable;
    }

    @Override
    public Essence run(Particle particle) {
        if(executable != null) {
            executable.run(particle);
        }
        return null;
    }

    @Override
    public String getName() {
        return methodName;
    }


    /*
    public Method(Parameter instance, Block block, String method, Executable runnable, Parameter[] parameters) {
        this.instance = instance;
        this.block = block;
        this.method = method;
        this.runnable = runnable;
        this.parameters = parameters;
    }

    public Method(PandaScript script, Block block, String method, Parameter[] parameters) {
        this.script = script;
        this.block = block;
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public Essence run(Particle particle) {
        particle.setInstance(instance);
        particle.setParameters(parameters);
        if (runnable == null) {
            if (isStatic()) {
                return script.call(MethodBlock.class, method, parameters);
            }
            String type = instance.getDataType();
            if (type == null) {
                instance.getValue();
                type = instance.getDataType();
            }
            if (type != null) {
                for (ObjectScheme os : ElementsBucket.getObjects()) {
                    if (!type.equals(os.getName())) {
                        continue;
                    }
                    for (MethodScheme ms : os.getMethods()) {
                        if (!method.equals(ms.getName())) {
                            continue;
                        }
                        this.runnable = ms.getExecutable();
                        return this.runnable.run(particle);
                    }
                }
            }
        }
        if (this.runnable == null) {
            System.out.println("[Method error] Runnable is null " +
                    (instance == null ? "{static}" : "(object) " +
                            (instance.getValue() == null ? "Unknown type" : instance.getValue().getType())) +
                    "." + method);
            return null;
        }
        return this.runnable.run(particle);
    }

    public boolean isStatic() {
        return instance == null;
    }

    public Block getBlock() {
        return block;
    }

    public Executable getRunnable() {
        return runnable;
    }

    public PandaScript getScript() {
        return script;
    }

    public Parameter getInstance() {
        return instance;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public String getMethod() {
        return method;
    }
    */

}
