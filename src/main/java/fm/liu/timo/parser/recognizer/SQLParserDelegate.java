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
 * (created at 2011-6-17)
 */
package fm.liu.timo.parser.recognizer;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

import fm.liu.timo.parser.ast.stmt.SQLStatement;
import fm.liu.timo.parser.ast.stmt.ddl.DDLAlterEventStatement;
import fm.liu.timo.parser.ast.stmt.ddl.DDLAlterViewStatement;
import fm.liu.timo.parser.ast.stmt.ddl.DDLCreateEventStatement;
import fm.liu.timo.parser.ast.stmt.ddl.DDLCreateFunctionStatement;
import fm.liu.timo.parser.ast.stmt.ddl.DDLCreateIndexStatement;
import fm.liu.timo.parser.ast.stmt.ddl.DDLCreateProcedureStatement;
import fm.liu.timo.parser.ast.stmt.ddl.DDLCreateTriggerStatement;
import fm.liu.timo.parser.ast.stmt.ddl.DDLCreateViewStatement;
import fm.liu.timo.parser.ast.stmt.ddl.DDLStatement;
import fm.liu.timo.parser.ast.stmt.mts.MTSStartTransactionStatement;
import fm.liu.timo.parser.recognizer.mysql.MySQLToken;
import fm.liu.timo.parser.recognizer.mysql.lexer.MySQLLexer;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLCmpdParser;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLDALParser;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLDDLParser;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLDMLCallParser;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLDMLDeleteParser;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLDMLInsertParser;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLDMLReplaceParser;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLDMLSelectParser;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLDMLUpdateParser;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLExprParser;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLMTSParser;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLParser;
import fm.liu.timo.parser.recognizer.mysql.syntax.MySQLPrepareParser;

/**
 * @author <a href="mailto:shuo.qius@alibaba-inc.com">QIU Shuo</a>
 */
public final class SQLParserDelegate {
    public static enum SpecialIdentifier {
        ROLLBACK, SAVEPOINT, TRUNCATE, START, DEALLOCATE, PREPARE, EXECUTE, BEGIN, OPEN, CLOSE, COMMIT
    }

    public static final Map<String, SpecialIdentifier> specialIdentifiers =
            new HashMap<String, SpecialIdentifier>();

    static {
        specialIdentifiers.put("TRUNCATE", SpecialIdentifier.TRUNCATE);
        specialIdentifiers.put("SAVEPOINT", SpecialIdentifier.SAVEPOINT);
        specialIdentifiers.put("ROLLBACK", SpecialIdentifier.ROLLBACK);
        specialIdentifiers.put("START", SpecialIdentifier.START);
        specialIdentifiers.put("DEALLOCATE", SpecialIdentifier.DEALLOCATE);
        specialIdentifiers.put("PREPARE", SpecialIdentifier.PREPARE);
        specialIdentifiers.put("EXECUTE", SpecialIdentifier.EXECUTE);
        specialIdentifiers.put("BEGIN", SpecialIdentifier.BEGIN);
        specialIdentifiers.put("OPEN", SpecialIdentifier.OPEN);
        specialIdentifiers.put("CLOSE", SpecialIdentifier.CLOSE);
        specialIdentifiers.put("COMMIT", SpecialIdentifier.COMMIT);

    }

    private static boolean isEOFedDDL(SQLStatement stmt) {
        if (stmt instanceof DDLStatement) {
            // DDLCreateViewStatement/DDLAlterViewStatement 目前暂实现至 view 名为止
            // DDLCreateEventStatement/DDLAlterEventStatement 目前暂实现至 Event 名为止
            // DDLCreateFunctionStatement 目前暂实现至 Function 名为止
            // DDLCreateProcedureStatement 目前暂实现至 Procedure 名为止
            // DDLCreateTriggerStatement 目前暂实现至 Trigger 名为止
            // 方便禁用语句，因此在此处跳过后续EOF判断
            if (stmt instanceof DDLCreateIndexStatement || stmt instanceof DDLAlterViewStatement
                    || stmt instanceof DDLCreateViewStatement
                    || stmt instanceof DDLAlterEventStatement
                    || stmt instanceof DDLCreateEventStatement
                    || stmt instanceof DDLCreateFunctionStatement
                    || stmt instanceof DDLCreateProcedureStatement
                    || stmt instanceof DDLCreateTriggerStatement)
                return false;
        }
        return true;
    }

    private static String buildErrorMsg(Exception e, MySQLLexer lexer, String sql) {
        StringBuilder sb = new StringBuilder(
                "You have an error in your SQL syntax; Error occurs around this fragment: ");
        final int ch = lexer.getCurrentIndex();
        int from = ch - 16;
        if (from < 0) {
            from = 0;
        }
        int to = from + 25;
        if (to >= sql.length()) {
            to = sql.length() - 1;
        }
        if (to <= from) {
            from = 0;
            to = sql.length();
        }
        String fragment = sql.substring(from, to + 1);
        sb.append('{').append(fragment).append('}').append(". Error cause: " + e.getMessage());
        return sb.toString();
    }

    public static SQLStatement parse(MySQLLexer lexer, String charset, boolean inCompoundStatement)
            throws SQLSyntaxErrorException {
        SQLStatement stmt = null;
        boolean isEOF = true;
        MySQLExprParser exprParser = new MySQLExprParser(lexer, charset);
        stmtSwitch: switch (lexer.token()) {
            case KW_DESC:
            case KW_DESCRIBE:
            case KW_EXPLAIN:
                stmt = new MySQLDALParser(lexer, exprParser).explain();
                break stmtSwitch;
            case KW_SELECT:
            case PUNC_LEFT_PAREN:
                stmt = new MySQLDMLSelectParser(lexer, exprParser).selectUnion(false);
                break stmtSwitch;
            case KW_DELETE:
                stmt = new MySQLDMLDeleteParser(lexer, exprParser).delete();
                break stmtSwitch;
            case KW_INSERT:
                stmt = new MySQLDMLInsertParser(lexer, exprParser).insert();
                break stmtSwitch;
            case KW_REPLACE:
                stmt = new MySQLDMLReplaceParser(lexer, exprParser).replace();
                break stmtSwitch;
            case KW_UPDATE:
                stmt = new MySQLDMLUpdateParser(lexer, exprParser).update();
                break stmtSwitch;
            case KW_CALL:
                stmt = new MySQLDMLCallParser(lexer, exprParser).call();
                break stmtSwitch;
            case KW_SET:
                stmt = new MySQLDALParser(lexer, exprParser).set();
                break stmtSwitch;
            case KW_SHOW:
                stmt = new MySQLDALParser(lexer, exprParser).show();
                break stmtSwitch;
            case KW_ALTER:
            case KW_CREATE:
            case KW_DROP:
            case KW_RENAME:
                stmt = new MySQLDDLParser(lexer, exprParser).ddlStmt();
                isEOF = isEOFedDDL(stmt);
                break stmtSwitch;
            case KW_RELEASE:
                stmt = new MySQLMTSParser(lexer).release();
                break stmtSwitch;
            case KW_IF:
                stmt = new MySQLCmpdParser(lexer, exprParser).ifStmt();
                break stmtSwitch;
            case KW_LOOP:
                stmt = new MySQLCmpdParser(lexer, exprParser).loop(null);
                break stmtSwitch;
            case KW_ITERATE:
                stmt = new MySQLCmpdParser(lexer, exprParser).iterate();
                break stmtSwitch;
            case KW_LEAVE:
                stmt = new MySQLCmpdParser(lexer, exprParser).leave();
                break stmtSwitch;
            case KW_RETURN:
                stmt = new MySQLCmpdParser(lexer, exprParser).returnStmt();
                break stmtSwitch;
            case KW_REPEAT:
                stmt = new MySQLCmpdParser(lexer, exprParser).repeat(null);
                break stmtSwitch;
            case KW_CASE:
                stmt = new MySQLCmpdParser(lexer, exprParser).caseStmt();
                break stmtSwitch;
            case KW_DECLARE:
                stmt = new MySQLCmpdParser(lexer, exprParser).declare();
                break stmtSwitch;
            case KW_FETCH:
                stmt = new MySQLCmpdParser(lexer, exprParser).fetch();
                break stmtSwitch;
            case KW_SIGNAL:
                stmt = new MySQLCmpdParser(lexer, exprParser).signal();
                break stmtSwitch;
            case KW_RESIGNAL:
                stmt = new MySQLCmpdParser(lexer, exprParser).resignal();
                break stmtSwitch;
            case KW_GET:
                stmt = new MySQLCmpdParser(lexer, exprParser).diagnostics();
                break stmtSwitch;
            case IDENTIFIER:
                SpecialIdentifier si = null;
                if ((si = specialIdentifiers.get(lexer.stringValueUppercase())) != null) {
                    switch (si) {
                        case TRUNCATE:
                            stmt = new MySQLDDLParser(lexer, exprParser).truncate();
                            break stmtSwitch;
                        case SAVEPOINT:
                            stmt = new MySQLMTSParser(lexer).savepoint();
                            break stmtSwitch;
                        case ROLLBACK:
                            stmt = new MySQLMTSParser(lexer).rollback();
                            break stmtSwitch;
                        case START:
                            stmt = new MySQLMTSParser(lexer).start();
                            break stmtSwitch;
                        case DEALLOCATE: {
                            stmt = new MySQLDDLParser(lexer, exprParser).ddlStmt();
                            isEOF = isEOFedDDL(stmt);
                            break stmtSwitch;
                        }
                        case PREPARE: {
                            stmt = new MySQLPrepareParser(lexer, exprParser).prepare();
                            break stmtSwitch;
                        }
                        case EXECUTE: {
                            stmt = new MySQLPrepareParser(lexer, exprParser).execute();
                            break stmtSwitch;
                        }
                        case BEGIN: {
                            lexer.nextToken();
                            if (lexer.token() == MySQLToken.EOF) {
                                stmt = new MTSStartTransactionStatement(null);
                            } else {
                                stmt = new MySQLCmpdParser(lexer, exprParser).beginEnd(null);
                            }
                            break stmtSwitch;
                        }
                        case OPEN: {
                            lexer.nextToken();
                            stmt = new MySQLCmpdParser(lexer, exprParser).open();
                            break stmtSwitch;
                        }
                        case CLOSE: {
                            lexer.nextToken();
                            stmt = new MySQLCmpdParser(lexer, exprParser).close();
                            break stmtSwitch;
                        }
                        case COMMIT: {
                            stmt = new MySQLMTSParser(lexer).commit();
                            break stmtSwitch;
                        }
                    }
                }
                stmt = new MySQLCmpdParser(lexer, exprParser).parseWithIdentifier();
                if (stmt != null) {
                    break stmtSwitch;
                }
            default:
                throw new SQLSyntaxErrorException("sql is not a supported statement");
        }
        if (isEOF) {
            while (lexer.token() == MySQLToken.PUNC_SEMICOLON) {
                lexer.nextToken();
            }
            if (lexer.token() != MySQLToken.EOF && !inCompoundStatement) {
                throw new SQLSyntaxErrorException("SQL syntax error!");
            }
        }
        return stmt;
    }

    public static SQLStatement parse(byte[] data, String charset) throws SQLSyntaxErrorException {
        MySQLLexer lexer = new MySQLLexer(data);
        try {
            return parse(lexer, charset, false);
        } catch (Exception e) {
            throw new SQLSyntaxErrorException(buildErrorMsg(e, lexer, new String(data)), e);
        }
    }

    public static SQLStatement parse(String sql, String charset) throws SQLSyntaxErrorException {
        MySQLLexer lexer = new MySQLLexer(sql);
        try {
            return parse(lexer, charset, false);
        } catch (Exception e) {
            throw new SQLSyntaxErrorException(buildErrorMsg(e, lexer, sql), e);
        }
    }

    public static SQLStatement parse(String sql) throws SQLSyntaxErrorException {
        return parse(sql, MySQLParser.DEFAULT_CHARSET);
    }
}
