package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.essential.GroupCenter;
import org.panda_lang.panda.core.statement.block.VialBlock;

import java.util.HashMap;
import java.util.Map;

public class Vial {

    private final String name;
    private final Map<String, Method> methods;
    private final Map<String, Field> fields;
    private Executable constructor;
    private VialBlock vialBlock;
    private String extension;
    private Class clazz;
    private Group group;

    public Vial(String vialName) {
        this(vialName, null);
    }

    public Vial(String vialName, Class clazz) {
        this.name = vialName;
        this.methods = new HashMap<>();
        this.fields = new HashMap<>();
        this.extension = "Object";
        this.clazz = null;
    }

    public Vial clazz(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    public Vial group(String groupName) {
        return group(GroupCenter.getGroup(groupName));
    }

    public Vial group(Group group) {
        this.group = group;
        this.group.registerVial(this);
        return this;
    }

    public Vial constructor(Executable executable) {
        this.constructor = executable;
        return this;
    }

    public Vial field(Field field) {
        this.fields.put(field.getName(), field);
        return this;
    }

    public Vial method(final Method method) {
        if (constructor == null && method.getName().equals(name)) {
            this.constructor(new Constructor() {
                @Override
                public Essence run(Alice alice) {
                    return method.run(alice);
                }
            });
        }
        else {
            methods.put(method.getName(), method);
        }
        return this;
    }

    public Vial extension(String s) {
        this.extension = s;
        return this;
    }

    public Essence call(String name, Alice alice) {
        Method method = getMethod(name);
        if (method == null) {
            System.out.println("Method '" + name + "' not found in instance of " + name);
            return null;
        }
        return method.run(alice);
    }

    public Essence initializeInstance(Alice alice) {
        Essence essence = new Essence(this);
        essence.initializeParticle(alice);
        alice = essence.particle(alice);

        if (constructor != null) {
            Essence instance = constructor.run(alice);
            if (instance != null) {
                essence = instance;
                instance.setVial(this);
            }
        }
        return essence;
    }

    public boolean isVeritableVial() {
        return vialBlock != null;
    }

    public Method getMethod(String name) {
        Method method = methods.get(name);
        if (method == null && extension != null) {
            //TODO: extension
        }
        return method;
    }

    public Map<String, Field> getFields() {
        return fields;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public VialBlock getVialBlock() {
        return vialBlock;
    }

    public void setVialBlock(VialBlock vialBlock) {
        this.vialBlock = vialBlock;
    }

    public Group getGroup() {
        return group;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

}
