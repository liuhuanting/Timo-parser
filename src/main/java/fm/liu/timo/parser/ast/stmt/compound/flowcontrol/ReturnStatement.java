package fm.liu.timo.parser.ast.stmt.compound.flowcontrol;

import fm.liu.timo.parser.ast.expression.primary.Identifier;
import fm.liu.timo.parser.ast.stmt.compound.CompoundStatement;
import fm.liu.timo.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2017年11月1日 下午6:43:45
 * 
 */
public class ReturnStatement implements CompoundStatement {
    private final Identifier label;

    public ReturnStatement(Identifier label) {
        this.label = label;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Identifier getLabel() {
        return this.label;
    }

}
