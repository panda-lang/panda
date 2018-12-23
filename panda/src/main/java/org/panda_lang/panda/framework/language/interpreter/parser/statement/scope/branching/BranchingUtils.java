package org.panda_lang.panda.framework.language.interpreter.parser.statement.scope.branching;

import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.statement.StatementData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.architecture.statement.PandaStatementData;

import java.util.function.Supplier;

class BranchingUtils {

    static void parseBranchingStatement(Tokens source, Container container, Supplier<Statement> supplier) {
        Statement statement = supplier.get();
        container.addStatement(statement);

        StatementData statementData = new PandaStatementData(source.getCurrentLine());
        statement.setStatementData(statementData);
    }

}
