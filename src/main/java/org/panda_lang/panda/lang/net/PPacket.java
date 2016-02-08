package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.lang.PObject;

public class PPacket extends PObject {

    static {
        Vial vial = new Vial("Packet");
        vial.group("panda.network");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                return new PPacket();
            }
        });
    }

    public PPacket() {
    }

    @Override
    public Object getJavaValue() {
        return this;
    }

}
