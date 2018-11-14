package fm.liu.timo.parser.ast.stmt.compound;

import java.util.List;

import fm.liu.timo.parser.ast.expression.Expression;
import fm.liu.timo.parser.ast.expression.primary.Identifier;
import fm.liu.timo.parser.ast.fragment.ddl.datatype.DataType;
import fm.liu.timo.parser.visitor.Visitor;

/**
 * 
 * @author liuhuanting
 * @date 2017年11月1日 下午4:22:14
 * 
 */
public class DeclareStatement implements CompoundStatement {
    private final List<Identifier> varNames;
    private final DataType dataType;
    private final Expression defaultVal;

    public List<Identifier> getVarNames() {
        return varNames;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Expression getDefaultVal() {
        return defaultVal;
    }

    public DeclareStatement(List<Identifier> varNames, DataType dataType, Expression defaultVal) {
        this.varNames = varNames;
        this.dataType = dataType;
        this.defaultVal = defaultVal;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
