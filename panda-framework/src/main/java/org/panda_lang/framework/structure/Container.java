package org.panda_lang.framework.structure;

import java.util.List;

public interface Container extends Statement {

    /**
     * Adds executable to the current scope
     *
     * @param statement proper statement
     * @return executable cell where executable was placed
     */
    StatementCell addStatement(Statement statement);

    /**
     * @return list of all cells in correct order
     */
    List<StatementCell> getStatementCells();

}
