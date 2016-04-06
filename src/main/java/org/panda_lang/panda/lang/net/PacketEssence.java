package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Vial;
import org.panda_lang.panda.lang.ObjectEssence;

public class PacketEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("Packet");
        vial.group("panda.network");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Alice alice) {
                return new PacketEssence();
            }
        });
    }

    public PacketEssence() {
    }

    @Override
    public Object getJavaValue() {
        return this;
    }

}
