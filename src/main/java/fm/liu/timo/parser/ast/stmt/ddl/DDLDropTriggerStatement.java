package fm.liu.timo.parser.ast.stmt.ddl;

import fm.liu.timo.parser.ast.expression.primary.Identifier;
import fm.liu.timo.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2017年11月6日 上午11:44:17
 * 
 */
public class DDLDropTriggerStatement implements DDLStatement {
    private final boolean ifExists;
    private final Identifier name;

    public DDLDropTriggerStatement(boolean ifExists, Identifier name) {
        this.ifExists = ifExists;
        this.name = name;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public Identifier getName() {
        return name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


}
