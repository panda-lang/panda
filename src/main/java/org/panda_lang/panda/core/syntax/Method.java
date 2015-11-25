package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.scheme.MethodScheme;
import org.panda_lang.panda.core.scheme.ObjectScheme;
import org.panda_lang.panda.core.syntax.block.MethodBlock;
import org.panda_lang.panda.lang.PObject;

public class Method implements Executable {

    private final Block block;
    private final String method;
    private final Parameter[] parameters;
    private Parameter instance;
    private IExecutable runnable;
    private PandaScript script;

    public Method(Parameter instance, Block block, String method, IExecutable runnable, Parameter[] parameters) {
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
    public PObject run(Parameter... vars) {
        if (runnable == null) {
            if (instance == null) return script.call(MethodBlock.class, method, parameters);
            String type = instance.getDataType();
            if (type == null) {
                instance.getValue();
                type = instance.getDataType();
            }
            if (type != null) {
                for (ObjectScheme os : ElementsBucket.getObjects()) {
                    if (!type.equals(os.getName())) continue;
                    for (MethodScheme ms : os.getMethods()) {
                        if (!method.equals(ms.getName())) continue;
                        this.runnable = ms.getExecutable();
                        return this.runnable.run(instance, parameters);
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
        return this.runnable.run(instance, parameters);
    }

    public Block getBlock() {
        return block;
    }

    public IExecutable getRunnable() {
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

    @Override
    public String getName() {
        return method;
    }

}
