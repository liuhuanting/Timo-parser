package fm.liu.timo.parser.ast.stmt.compound.cursors;

import fm.liu.timo.parser.ast.expression.primary.Identifier;
import fm.liu.timo.parser.ast.stmt.compound.CompoundStatement;
import fm.liu.timo.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2017年11月1日 下午4:24:52
 * 
 */
public class CursorOpenStatement implements CompoundStatement {
    private final Identifier name;

    public CursorOpenStatement(Identifier name) {
        this.name = name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Identifier getName() {
        return this.name;
    }

}
