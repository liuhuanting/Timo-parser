package fm.liu.timo.parser.ast.stmt.compound.cursors;

import fm.liu.timo.parser.ast.expression.primary.Identifier;
import fm.liu.timo.parser.ast.stmt.SQLStatement;
import fm.liu.timo.parser.ast.stmt.compound.CompoundStatement;
import fm.liu.timo.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2017年11月1日 下午4:24:19
 * 
 */
public class CursorDeclareStatement implements CompoundStatement {
    private final Identifier name;
    private final SQLStatement stmt;

    public CursorDeclareStatement(Identifier name, SQLStatement stmt) {
        this.name = name;
        this.stmt = stmt;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Identifier getName() {
        return this.name;
    }

    public SQLStatement getStmt() {
        return this.stmt;
    }
}
