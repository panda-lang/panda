package org.panda_lang.panda.core.memory;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private final Map<String, Object> map;
    public Object slotA;
    public Object slotB;
    public Object slotC;
    public Object slotD;
    public Object slotE;
    private boolean proceed;
    private boolean skipped;

    public Cache() {
        this.map = new HashMap<>(0);
        this.proceed = true;
    }

    public void proceed(boolean proceed) {
        this.proceed = proceed;
    }

    public void skipped(boolean skipped) {
        this.skipped = skipped;
    }

    public boolean isProceed() {
        return proceed;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public Map<String, Object> getMap() {
        return map;
    }

}
