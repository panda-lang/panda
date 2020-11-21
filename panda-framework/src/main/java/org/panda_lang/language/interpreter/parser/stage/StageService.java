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
                selectedPhase.currentLayer().delegateBefore(id, task);
                break;
            case CURRENT_DEFAULT:
                selectedPhase.currentLayer().delegate(id, task);
                break;
            case CURRENT_AFTER:
                selectedPhase.currentLayer().delegateAfter(id, task);
                break;
            case NEXT_BEFORE:
                selectedPhase.nextLayer().delegateBefore(id, task);
                break;
            case NEXT_DEFAULT:
                selectedPhase.nextLayer().delegate(id, task);
                break;
            case NEXT_AFTER:
                selectedPhase.nextLayer().delegateAfter(id, task);
                break;
            default:
                throw new PandaParserException("Unknown layer: " + layer);
        }
    }

}
