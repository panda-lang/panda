package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.*;

import java.util.ArrayList;
import java.util.List;

public class ListEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("List");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                return new ListEssence();
            }
        });
        vial.method(new Method("add", new Executable() {
            @Override
            public Essence run(Particle particle) {
                ListEssence list = particle.getValueOfInstance();
                list.getList().add(particle.getValueOfFactor(0));
                return null;
            }
        }));
    }

    private final List<Essence> list;

    public ListEssence() {
        this.list = new ArrayList<>();
    }

    public List<Essence> getList() {
        return list;
    }

    @Override
    public Object getJavaValue() {
        return list;
    }

    @Override
    public String toString() {
        StringBuilder node = new StringBuilder();
        for (Essence o : list) {
            if (node.length() != 0) {
                node.append(", ");
            }
            node.append(o);
        }
        return node.toString();
    }

}
