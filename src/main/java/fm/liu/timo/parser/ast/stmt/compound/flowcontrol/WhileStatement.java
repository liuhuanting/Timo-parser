package fm.liu.timo.parser.ast.stmt.compound.flowcontrol;

import fm.liu.timo.parser.ast.expression.Expression;
import fm.liu.timo.parser.ast.expression.primary.Identifier;
import fm.liu.timo.parser.ast.stmt.SQLStatement;
import fm.liu.timo.parser.ast.stmt.compound.CompoundStatement;
import fm.liu.timo.parser.visitor.Visitor;

/**
 * <pre>
 * [begin_label:] WHILE search_condition DO
 *     statement_list
 * END WHILE [end_label]
 * </pre>
 * @author liuhuanting
 * @date 2017年11月1日 下午4:21:17
 * 
 */
public class WhileStatement implements CompoundStatement {
    private final Identifier label;
    private final SQLStatement stmt;
    private final Expression whileCondition;

    public Identifier getLabel() {
        return label;
    }

    public SQLStatement getStmt() {
        return stmt;
    }

    public Expression getWhileCondition() {
        return whileCondition;
    }

    public WhileStatement(Identifier label, SQLStatement stmt, Expression whileCondition) {
        this.label = label;
        this.stmt = stmt;
        this.whileCondition = whileCondition;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
