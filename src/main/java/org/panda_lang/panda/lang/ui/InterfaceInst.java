package org.panda_lang.panda.lang.ui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Structure;
import org.panda_lang.panda.lang.ObjectInst;
import org.panda_lang.panda.lang.ui.util.PandaInterface;

import javax.swing.*;

public class InterfaceInst extends ObjectInst {

    private static final Structure STRUCTURE;

    static {
        STRUCTURE = new Structure("Interface");
        STRUCTURE.group("panda.lang.ui");
        STRUCTURE.constructor((Constructor) alice -> new InterfaceInst());
    }

    private PandaInterface application;

    public InterfaceInst() {
        initialize();
    }

    protected void initialize() {
        SwingUtilities.invokeLater(() -> {
            new JFXPanel();
            Platform.runLater(() -> {
                try {
                    application = new PandaInterface();
                    application.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public PandaInterface getApplication() {
        return application;
    }

}
