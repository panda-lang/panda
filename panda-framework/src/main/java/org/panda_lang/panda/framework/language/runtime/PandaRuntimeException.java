package org.panda_lang.panda.framework.language.runtime;

import org.panda_lang.panda.framework.PandaFrameworkException;

public class PandaRuntimeException extends PandaFrameworkException {

    public PandaRuntimeException() {
    }

    public PandaRuntimeException(String message) {
        super(message);
    }

    public PandaRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PandaRuntimeException(Throwable cause) {
        super(cause);
    }

}
