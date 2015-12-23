package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;

import java.util.HashMap;
import java.util.Map;

public class Vial {

    private final String vialName;
    private final Map<String, Method> methods;
    private Executable constructor;
    private String extension;

    public Vial(String vialName) {
        this.vialName = vialName;
        this.methods = new HashMap<>();
        this.extension = "Object";
    }

    public Essence call(String name, Particle particle) {
        Method method = getMethod(name);
        if (method == null) {
            System.out.println("Method '" + name + "' not found in instance of " + vialName);
            return null;
        }
        return method.run(particle);
    }

    public Essence initializeInstance(Particle particle) {
        Essence essence = new Essence(this);
        if(constructor != null) {
            Essence ce = constructor.run(particle);
            if (ce != null) {
                essence = ce;
            }
        }
        return essence;
    }

    public Vial constructor(Executable executable) {
        this.constructor = executable;
        return this;
    }

    public Vial method(final Method method) {
        if (constructor == null && method.getName().equals(vialName)) {
           this.constructor(new Constructor() {
               @Override
               public Essence run(Particle particle) {
                   method.run(particle);
                   return null;
               }
           });
        } else {
            methods.put(method.getName(), method);
        }
        return this;
    }

    public Vial extension(String s) {
        this.extension = s;
        return this;
    }

    public Method getMethod(String name) {
        Method method = methods.get(name);
        if (method == null && extension != null) {
            Vial vial = VialCenter.getVial(extension);
            if (vial != null) {
                method = vial.getMethod(name);
                if (method == null) {
                    extension = null;
                }
            }
        }
        return method;
    }

    public String getName() {
        return vialName;
    }

}
