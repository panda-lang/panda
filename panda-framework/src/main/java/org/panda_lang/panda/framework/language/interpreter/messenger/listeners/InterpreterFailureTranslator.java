package org.panda_lang.panda.framework.language.interpreter.messenger.listeners;

import org.panda_lang.panda.framework.design.interpreter.*;
import org.panda_lang.panda.framework.design.interpreter.messenger.*;
import org.panda_lang.panda.framework.language.interpreter.messenger.*;
import org.panda_lang.panda.utilities.redact.format.*;

public class InterpreterFailureTranslator implements MessengerMessageTranslator<InterpreterFailure> {

    private final Interpretation interpretation;

    public InterpreterFailureTranslator(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    @Override
    public void handle(Messenger messenger, InterpreterFailure element) {
        interpretation.getFailures().add(element);

        MessageFormatter formatter = new MessageFormatter()
                .register("{{newline}}", System.lineSeparator())
                .register("{{line}}", element.getLine() + 1)
                .register("{{location}}", element.getLocation());

        String content = "{{newline}}Caused by: " + element.getMessage() + "{{newline}}";

        if (element.getDetails() != null) {
            content += "{{newline}}Details: " + element.getDetails() + "{{newline}}";
        }

        content += "{{newline}}End of Failure {{newline}} ";

        String formattedContent = formatter.format(content);
        String[] lines = formattedContent.split(System.lineSeparator());

        for (int i = 0; i < lines.length; ++i) {
            lines[i] = "[InterpreterFailure] #!# " + lines[i];
        }

        PandaMessengerMessage message = new PandaMessengerMessage(MessengerMessage.Level.FAILURE, lines);
        messenger.sendMessage(message);
    }

    @Override
    public Class<InterpreterFailure> getType() {
        return InterpreterFailure.class;
    }

}
