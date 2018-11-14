/*
 * Copyright 1999-2012 Alibaba Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/**
 * (created at 2011-7-5)
 */
package fm.liu.timo.parser.ast.stmt.ddl;

import java.util.ArrayList;
import java.util.List;

import fm.liu.timo.parser.ast.ASTNode;
import fm.liu.timo.parser.ast.expression.Expression;
import fm.liu.timo.parser.ast.expression.primary.Identifier;
import fm.liu.timo.parser.ast.fragment.ddl.ColumnDefinition;
import fm.liu.timo.parser.ast.fragment.ddl.TableOptions;
import fm.liu.timo.parser.ast.fragment.ddl.index.IndexColumnName;
import fm.liu.timo.parser.ast.fragment.ddl.index.IndexDefinition;
import fm.liu.timo.parser.ast.stmt.dml.DMLQueryStatement;
import fm.liu.timo.parser.util.Pair;
import fm.liu.timo.parser.visitor.Visitor;

/**
 * NOT FULL AST: foreign key, ... not supported
 * 
 * @author <a href="mailto:shuo.qius@alibaba-inc.com">QIU Shuo</a>
 */
public class DDLCreateTableStatement implements DDLStatement {
    public static enum SelectOption {
        IGNORED, REPLACE
    }

    private final boolean temporary;
    private final boolean ifNotExists;
    private final Identifier table;
    private final List<Pair<Identifier, ColumnDefinition>> colDefs;
    private IndexDefinition primaryKey;
    private final List<Pair<Identifier, IndexDefinition>> uniqueKeys;
    private final List<Pair<Identifier, IndexDefinition>> keys;
    private final List<Pair<Identifier, IndexDefinition>> fullTextKeys;
    private final List<Pair<Identifier, IndexDefinition>> spatialKeys;
    private final List<Pair<Identifier, IndexDefinition>> foreignKeys;
    private final List<Expression> checks;
    private TableOptions tableOptions;
    private Pair<SelectOption, DMLQueryStatement> select;
    private final List<ForeignKeyDefinition> foreignKeyDefs;

    public DDLCreateTableStatement(boolean temporary, boolean ifNotExists, Identifier table) {
        this.table = table;
        this.temporary = temporary;
        this.ifNotExists = ifNotExists;
        this.colDefs = new ArrayList<Pair<Identifier, ColumnDefinition>>(4);
        this.uniqueKeys = new ArrayList<Pair<Identifier, IndexDefinition>>(1);
        this.keys = new ArrayList<Pair<Identifier, IndexDefinition>>(2);
        this.fullTextKeys = new ArrayList<Pair<Identifier, IndexDefinition>>(1);
        this.spatialKeys = new ArrayList<Pair<Identifier, IndexDefinition>>(1);
        this.foreignKeys = new ArrayList<Pair<Identifier, IndexDefinition>>(1);
        this.checks = new ArrayList<Expression>(1);
        this.foreignKeyDefs = new ArrayList<>();
    }

    public DDLCreateTableStatement setTableOptions(TableOptions tableOptions) {
        this.tableOptions = tableOptions;
        return this;
    }

    public DDLCreateTableStatement addColumnDefinition(Identifier colname, ColumnDefinition def) {
        colDefs.add(new Pair<Identifier, ColumnDefinition>(colname, def));
        return this;
    }

    public DDLCreateTableStatement setPrimaryKey(IndexDefinition def) {
        primaryKey = def;
        return this;
    }

    public DDLCreateTableStatement addUniqueIndex(Identifier colname, IndexDefinition def) {
        uniqueKeys.add(new Pair<Identifier, IndexDefinition>(colname, def));
        return this;
    }

    public DDLCreateTableStatement addIndex(Identifier colname, IndexDefinition def) {
        keys.add(new Pair<Identifier, IndexDefinition>(colname, def));
        return this;
    }

    public DDLCreateTableStatement addFullTextIndex(Identifier colname, IndexDefinition def) {
        fullTextKeys.add(new Pair<Identifier, IndexDefinition>(colname, def));
        return this;
    }

    public DDLCreateTableStatement addSpatialIndex(Identifier colname, IndexDefinition def) {
        spatialKeys.add(new Pair<Identifier, IndexDefinition>(colname, def));
        return this;
    }

    public DDLCreateTableStatement addForeignIndex(Identifier colname, IndexDefinition def) {
        foreignKeys.add(new Pair<Identifier, IndexDefinition>(colname, def));
        return this;
    }

    public DDLCreateTableStatement addCheck(Expression check) {
        checks.add(check);
        return this;
    }

    public TableOptions getTableOptions() {
        return tableOptions;
    }

    public Pair<SelectOption, DMLQueryStatement> getSelect() {
        return select;
    }

    public void setSelect(SelectOption option, DMLQueryStatement select) {
        this.select =
                new Pair<DDLCreateTableStatement.SelectOption, DMLQueryStatement>(option, select);
    }

    public boolean isTemporary() {
        return temporary;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public Identifier getTable() {
        return table;
    }

    /**
     * @return key := columnName
     */
    public List<Pair<Identifier, ColumnDefinition>> getColDefs() {
        return colDefs;
    }

    public IndexDefinition getPrimaryKey() {
        return primaryKey;
    }

    public List<Pair<Identifier, IndexDefinition>> getUniqueKeys() {
        return uniqueKeys;
    }

    public List<Pair<Identifier, IndexDefinition>> getKeys() {
        return keys;
    }

    public List<Pair<Identifier, IndexDefinition>> getFullTextKeys() {
        return fullTextKeys;
    }

    public List<Pair<Identifier, IndexDefinition>> getSpatialKeys() {
        return spatialKeys;
    }

    public List<Pair<Identifier, IndexDefinition>> getForeignKeys() {
        return foreignKeys;
    }

    public List<Expression> getChecks() {
        return checks;
    }

    public List<ForeignKeyDefinition> getForeignKeyDefs() {
        return foreignKeyDefs;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public static class ForeignKeyDefinition implements ASTNode {
        public enum REFERENCE_OPTION {
            RESTRICT, CASCADE, SET_NULL, NO_ACTION
        }

        private final Identifier indexName;
        private final List<IndexColumnName> columns;
        private final Identifier referenceTable;
        private final List<IndexColumnName> referenceColumns;
        private REFERENCE_OPTION onDelete;
        private REFERENCE_OPTION onUpdate;
        private Identifier symbol;

        public ForeignKeyDefinition(Identifier indexName, List<IndexColumnName> columns,
                Identifier referenceTable, List<IndexColumnName> referenceColumns) {
            this.indexName = indexName;
            this.columns = columns;
            this.referenceTable = referenceTable;
            this.referenceColumns = referenceColumns;
        }

        public REFERENCE_OPTION getOnDelete() {
            return onDelete;
        }

        public void setOnDelete(REFERENCE_OPTION onDelete) {
            this.onDelete = onDelete;
        }

        public REFERENCE_OPTION getOnUpdate() {
            return onUpdate;
        }

        public void setOnUpdate(REFERENCE_OPTION onUpdate) {
            this.onUpdate = onUpdate;
        }

        public Identifier getSymbol() {
            return symbol;
        }

        public void setSymbol(Identifier symbol) {
            this.symbol = symbol;
        }

        public Identifier getIndexName() {
            return indexName;
        }

        public List<IndexColumnName> getColumns() {
            return columns;
        }

        public Identifier getReferenceTable() {
            return referenceTable;
        }

        public List<IndexColumnName> getReferenceColumns() {
            return referenceColumns;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }
}
