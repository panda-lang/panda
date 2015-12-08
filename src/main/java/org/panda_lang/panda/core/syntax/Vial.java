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

    public Essence call(String name, Essence essence) {
        Method method = getMethod(name);
        if (method == null) {
            System.out.println("Method '" + name + "' not found in instance of " + vialName);
            return null;
        }
        return method.run(essence.getParticle());
    }

    public Essence initializeInstance(Particle particle) {
        Essence essence = new Essence(this);
        if (constructor != null) {
            constructor.run(particle);
        }
        return essence;
    }

    public Vial constructor(Executable executable) {
        this.constructor = executable;
        return this;
    }

    public Vial method(Method method) {
        if (constructor == null && method.getName().equals(vialName)) {
            constructor = method;
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
