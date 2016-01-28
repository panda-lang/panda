package org.panda_lang.panda.core.memory;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private final Map<String, Object> map;
    private boolean proceed;

    public Cache() {
        this.map = new HashMap<>(0);
    }

    public void proceed(boolean proceed) {
        this.proceed = proceed;
    }

    public boolean isProceed() {
        return proceed;
    }

    public Map<String, Object> getMap() {
        return map;
    }

}
