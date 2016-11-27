package org.panda_lang.panda.implementation.structure;

import org.panda_lang.framework.structure.Executable;
import org.panda_lang.framework.structure.Statement;
import org.panda_lang.framework.structure.StatementCell;

public class PandaStatementCell implements StatementCell {

    private Statement statement;
    private boolean manipulated;

    public PandaStatementCell(Statement executable) {
        this.statement = executable;
    }

    @Override
    public void setStatement(Statement executable) {
        if (this.statement != null) {
            this.manipulated = true;
        }

        this.statement = executable;
    }

    public boolean isManipulated() {
        return manipulated;
    }

    @Override
    public boolean isExecutable() {
        return statement != null && statement instanceof Executable;
    }

    @Override
    public Statement getStatement() {
        return statement;
    }

}
