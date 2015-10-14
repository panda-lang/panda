package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.lang.PObject;

public class Runtime implements Executable {

    private Parameter instance;
    private IExecutable executable;
    private Parameter[] parameters;
    private Equality equality;
    private Method method;
    private Math math;

    public Runtime(Equality equality){
        this.equality = equality;
    }

    public Runtime(Method method){
        this.method = method;
    }

    public Runtime(Math math){
        this.math = math;
    }

    public Runtime(Parameter instance, IExecutable executable, Parameter[] parameters){
        this.instance = instance;
        this.executable = executable;
        this.parameters = parameters;
    }

    @Override
    public PObject run(Parameter... parameters) {
        if(method != null) return method.run(parameters);
        else if(math != null) return math.run(null, parameters);
        else if(equality != null) return equality.run(null, parameters);
        return executable.run(instance, this.parameters);
    }

    @Override
    public String getName() {
        return "Runtime";
    }

}
