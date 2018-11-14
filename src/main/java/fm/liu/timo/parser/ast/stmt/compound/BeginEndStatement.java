package fm.liu.timo.parser.ast.stmt.compound;

import java.util.List;

import fm.liu.timo.parser.ast.expression.primary.Identifier;
import fm.liu.timo.parser.ast.stmt.SQLStatement;
import fm.liu.timo.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2017年11月1日 下午4:30:03
 * 
 */
public class BeginEndStatement implements CompoundStatement {
    private final Identifier label;
    private final List<SQLStatement> statements;

    public BeginEndStatement(Identifier label, List<SQLStatement> statements) {
        this.label = label;
        this.statements = statements;
    }

    public Identifier getLabel() {
        return label;
    }

    public List<SQLStatement> getStatements() {
        return statements;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
