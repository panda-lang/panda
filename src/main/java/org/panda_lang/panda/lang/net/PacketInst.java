package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Structure;
import org.panda_lang.panda.lang.ObjectInst;

public class PacketInst extends ObjectInst {

    static {
        Structure structure = new Structure("Packet");
        structure.group("panda.network");
        structure.constructor(new Constructor() {
            @Override
            public Inst execute(Alice alice) {
                return new PacketInst();
            }
        });
    }

    public PacketInst() {
    }

    @Override
    public Object getJavaValue() {
        return this;
    }

}
