package org.panda_lang.language.interpreter.parser.stage;

import org.panda_lang.language.interpreter.parser.PandaParserException;

public class StageService {

    private final StageManager stageManager;

    public StageService(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    public void delegate(String id, Phase phase, Layer layer, StageTask task) {
        StagePhase selectedPhase = stageManager.getPhase(phase);

        switch (layer) {
            case CURRENT_BEFORE:
                selectedPhase.currentPhase().delegateBefore(task);
                break;
            case CURRENT_DEFAULT:
                selectedPhase.currentPhase().delegate(task);
                break;
            case CURRENT_AFTER:
                selectedPhase.currentPhase().delegateAfter(task);
                break;
            case NEXT_BEFORE:
                selectedPhase.nextPhase().delegateBefore(task);
                break;
            case NEXT_DEFAULT:
                selectedPhase.nextPhase().delegate(task);
                break;
            case NEXT_AFTER:
                selectedPhase.nextPhase().delegateAfter(task);
                break;
            default:
                throw new PandaParserException("Unknown layer: " + layer);
        }
    }

}
