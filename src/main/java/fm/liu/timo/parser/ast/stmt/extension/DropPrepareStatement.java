package fm.liu.timo.parser.ast.stmt.extension;

import fm.liu.timo.parser.ast.stmt.ddl.DDLStatement;
import fm.liu.timo.parser.visitor.Visitor;

public class DropPrepareStatement implements DDLStatement {
    private final String preparedName;

    public DropPrepareStatement(String preparedName) {
        this.preparedName = preparedName;
    }

    @Override
    public void accept(Visitor visitor) {}

    public String getPreparedName() {
        return preparedName;
    }

}
