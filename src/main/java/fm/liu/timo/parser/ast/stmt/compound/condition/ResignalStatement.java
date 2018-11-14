package fm.liu.timo.parser.ast.stmt.compound.condition;

import java.util.List;

import fm.liu.timo.parser.ast.expression.primary.literal.Literal;
import fm.liu.timo.parser.ast.stmt.compound.CompoundStatement;
import fm.liu.timo.parser.ast.stmt.compound.condition.SignalStatement.ConditionInfoItemName;
import fm.liu.timo.parser.util.Pair;
import fm.liu.timo.parser.visitor.Visitor;

/**
 * TODO https://dev.mysql.com/doc/refman/5.7/en/resignal.html
 * @author liuhuanting
 * @date 2017年11月1日 下午4:26:03
 * 
 */
public class ResignalStatement implements CompoundStatement {
    private final ConditionValue conditionValue;
    private final List<Pair<ConditionInfoItemName, Literal>> informationItems;

    public ResignalStatement(ConditionValue conditionValue,
            List<Pair<ConditionInfoItemName, Literal>> informationItems) {
        super();
        this.conditionValue = conditionValue;
        this.informationItems = informationItems;
    }

    public ConditionValue getConditionValue() {
        return conditionValue;
    }

    public List<Pair<ConditionInfoItemName, Literal>> getInformationItems() {
        return informationItems;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
