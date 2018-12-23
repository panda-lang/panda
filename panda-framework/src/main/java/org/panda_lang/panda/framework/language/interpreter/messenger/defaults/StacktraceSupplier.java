package org.panda_lang.panda.framework.language.interpreter.messenger.defaults;

import org.panda_lang.panda.framework.design.interpreter.Interpreter;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.ReflectionUtils;

import java.util.Objects;
import java.util.function.Supplier;

class StacktraceSupplier implements Supplier<String> {

    private final Exception exception;

    StacktraceSupplier(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String get() {
        StringBuilder content = new StringBuilder();
        StackTraceElement[] elements = exception.getStackTrace();

        String previousClass = null;
        int count = 0;
        int gap = 0;

        for (int i = 0; i < elements.length; i++) {
            StackTraceElement lastElement = ArrayUtils.get(exception.getStackTrace(), i);

            if (lastElement == null) {
                break;
            }

            if (!lastElement.getClassName().startsWith("org.panda_lang") || lastElement.getFileName().contains("Generation") || lastElement.isNativeMethod()) {
                gap++;
                continue;
            }

            if (gap > 0) {
                content.append("(CI: ").append(gap).append(") <- ");
                gap = 0;
            }

            if (lastElement.getFileName().equals(previousClass)) {
                continue;
            }

            previousClass = lastElement.getFileName();
            content.append("(").append(lastElement.getFileName()).append(":").append(lastElement.getLineNumber()).append(") <- ");
            count++;

            if (Interpreter.class.isAssignableFrom(Objects.requireNonNull(ReflectionUtils.forName(lastElement.getClassName())))) {
                break;
            }
        }

        if (gap > 0) {
            content.append("(CI: ").append(gap).append(") <- ");
        }

        return content.append("[...]").toString();
    }

}
