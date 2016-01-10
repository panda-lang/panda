package org.panda_lang.panda.lang.net;

import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.lang.PObject;

public class PPacket extends PObject {

    private static final Vial vial;

    static {
        vial = VialCenter.initializeVial("Packet");
        vial.group("panda.lang.network");
    }

    public PPacket() {
        super(vial);
    }

}
