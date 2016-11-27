package org.panda_lang.panda.implementation.structure;

import org.panda_lang.framework.structure.Container;
import org.panda_lang.framework.structure.Statement;
import org.panda_lang.framework.structure.StatementCell;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainer implements Container {

    protected final List<StatementCell> executableCells;

    public AbstractContainer() {
        this.executableCells = new ArrayList<>();
    }

    @Override
    public StatementCell addStatement(Statement executable) {
        StatementCell executableCell = new PandaStatementCell(executable);
        executableCells.add(executableCell);
        return executableCell;
    }

    @Override
    public List<StatementCell> getStatementCells() {
        return executableCells;
    }

}
