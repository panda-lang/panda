package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.*;

import java.util.ArrayList;
import java.util.List;

public class PList extends PObject {

    private static final Vial vial;

    static {
        vial = VialCenter.initializeVial("List");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                return new PList();
            }
        });
        vial.method(new Method("add", new Executable() {
            @Override
            public Essence run(Particle particle) {
                PList list = particle.getValueOfInstance();
                list.getList().add(particle.getValueOfFactor(0));
                return null;
            }
        }));
    }

    private final List<Essence> list;

    public PList() {
        super(vial);
        this.list = new ArrayList<>();
    }

    public List<Essence> getList() {
        return list;
    }

    @Override
    public String getType() {
        return "List";
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
