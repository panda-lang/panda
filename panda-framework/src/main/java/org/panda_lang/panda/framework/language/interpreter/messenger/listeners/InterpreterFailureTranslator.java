package org.panda_lang.panda.framework.language.interpreter.messenger.listeners;

import org.fusesource.jansi.*;
import org.panda_lang.panda.framework.*;
import org.panda_lang.panda.framework.design.interpreter.*;
import org.panda_lang.panda.framework.design.interpreter.messenger.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.language.interpreter.messenger.*;
import org.panda_lang.panda.utilities.commons.objects.*;
import org.panda_lang.panda.utilities.redact.format.*;

public class InterpreterFailureTranslator implements MessengerMessageTranslator<InterpreterFailure> {

    private final Interpretation interpretation;

    public InterpreterFailureTranslator(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    @Override
    public void handle(Messenger messenger, InterpreterFailure element) {
        interpretation.getFailures().add(element);

        ParserData data = element.getData();
        String source = data.getComponent(UniversalComponents.SOURCE).selectLine(element.getLine()).asString();

        MessageFormatter formatter = new MessageFormatter()
                .register("{{newline}}", System.lineSeparator())
                .register("{{line}}", element.getLine() + 1)
                .register("{{location}}", element.getLocation())
                .register("{{message}}", element.getMessage())
                .register("{{details}}", indentation(element.getDetails()))
                .register("{{source}}", source)
                .register("{{os}}", System.getProperty("os.name"))
                .register("{{java.version}}", System.getProperty("java.version"))
                .register("{{panda.version}}", PandaFrameworkConstants.VERSION);

        String content = "{{newline}}Caused by: {{message}} [in {{location}} at line {{line}}]{{newline}}";

        if (element.getDetails() != null) {
            content += "{{newline}}Details:{{newline}}  {{details}}{{newline}}";
        }

        TokenRepresentation currentToken = data.getComponent(UniversalComponents.SOURCE_STREAM).read();
        int index = source.indexOf(currentToken.getTokenValue());

        if (index > -1) {
            Ansi ansi = Ansi.ansi();
            formatter.register("{{source}}", ansi.a(source.substring(0, index)).fgRed().a(source.substring(index, source.length())).reset().toString());
        }

        content += "{{newline}}Source:{{newline}}  {{source}}{{newline}}";

        if (index > -1) {
            content += "  " + StringUtils.createIndentation(index - 2) + "^{{newline}}";
        }

        content += "{{newline}}Environment:{{newline}}  OS: {{os}}{{newline}}  Panda: {{panda.version}}{{newline}}  Java: {{java.version}}{{newline}}";
        content += "{{newline}}End of Failure {{newline}} ";

        String formattedContent = formatter.format(content);
        String[] lines = formattedContent.split(System.lineSeparator());

        for (int i = 0; i < lines.length; ++i) {
            lines[i] = "[InterpreterFailure] #!# " + lines[i];
        }

        PandaMessengerMessage message = new PandaMessengerMessage(MessengerMessage.Level.FAILURE, lines);
        messenger.sendMessage(message);
    }

    private String indentation(String message) {
        return message == null ? null : message.replace(System.lineSeparator(), System.lineSeparator() + "  ");
    }

    @Override
    public Class<InterpreterFailure> getType() {
        return InterpreterFailure.class;
    }

    static {
        // System.setProperty("log4j.skipJansi", "true");
        // AnsiConsole.systemInstall();
    }

}
