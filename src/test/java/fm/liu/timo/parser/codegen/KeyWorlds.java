package fm.liu.timo.parser.codegen;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author liuhuanting
 * @date 2018年9月7日 上午10:51:50
 * 
 */
public class KeyWorlds {
    public static void main(String[] args) {
        String[] keyWorlds = {"ACCESSIBLE", "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC",
                "ASENSITIVE", "BEFORE", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL",
                "CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN",
                "CONDITION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CURRENT_DATE",
                "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE",
                "DATABASES", "DAY_HOUR", "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC",
                "DECIMAL", "DECLARE", "DEFAULT", "DELAYED", "DELETE", "DESC", "DESCRIBE",
                "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL", "EACH",
                "ELSE", "ELSEIF", "ENCLOSED", "ESCAPED", "EXISTS", "EXIT", "EXPLAIN", "FETCH",
                "FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE", "FOREIGN", "FROM", "FROM", "FULLTEXT",
                "GENERATED", "GET", "GRANT", "GROUP", "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND",
                "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER",
                "INOUT", "INSENSITIVE", "INSERT", "INT", "INT1", "INT2", "INT3", "INT4", "INT8",
                "INTEGER", "INTERVAL", "INTO", "IO_AFTER_GTIDS", "IO_BEFORE_GTIDS", "IS", "ITERATE",
                "JOIN", "KEY", "KEYS", "KILL", "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT",
                "LINEAR", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "LONG",
                "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MASTER_BIND",
                "MASTER_SSL_VERIFY_SERVER_CERT", "MATCH", "MAXVALUE", "MEDIUMBLOB", "MEDIUMINT",
                "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES",
                "NATURAL", "NOT", "NO_WRITE_TO_BINLOG", "NUMERIC", "ON", "OPTIMIZE",
                "OPTIMIZER_COSTS", "OPTION", "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER", "OUTFILE",
                "PARTITION", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE", "RANGE", "READ", "READS",
                "READ_WRITE", "REAL", "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT",
                "REPLACE", "REQUIRE", "RESIGNAL", "RESTRICT", "RETURN", "REVOKE", "RIGHT", "RLIKE",
                "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", "SENSITIVE", "SEPARATOR",
                "SET", "SHOW", "SIGNAL", "SMALLINT", "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION",
                "SQLSTATE", "SQLWARNING", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS",
                "SQL_SMALL_RESULT", "SSL", "STARTING", "STORED", "STRAIGHT_JOIN", "TABLE",
                "TERMINATED", "THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING",
                "TRIGGER", "UNDO", "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE",
                "USE", "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY",
                "VARCHAR", "VARCHARACTER", "VARYING", "VIRTUAL", "WHEN", "WHERE", "WHILE", "WITH",
                "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL"};
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        for (String key : keyWorlds) {
            queue.add(key);
        }
        System.out.println("switch (keyUpperCase[0]){");
        while (!queue.isEmpty()) {
            String first = queue.poll();
            char firstc = first.charAt(0);
            LinkedBlockingQueue<String> iqueue = new LinkedBlockingQueue<>();
            iqueue.add(first);
            int minL = first.length();
            int maxL = minL;
            while (!queue.isEmpty()) {
                if (queue.peek().startsWith(String.valueOf(firstc))) {
                    String i = queue.poll();
                    if (minL == 0) {
                        minL = i.length();
                    }
                    if (maxL == 0) {
                        maxL = i.length();
                    }
                    if (i.length() < minL) {
                        minL = i.length();
                    }
                    if (i.length() > maxL) {
                        maxL = i.length();
                    }
                    iqueue.add(i);
                } else {
                    break;
                }
            }
            System.out.println("case '" + firstc + "':{");
            if (minL == maxL) {
                System.out.println("if(keyUpperCase.length!=" + minL + "){\nreturn null;\n}");
            } else {
                System.out.println("if(keyUpperCase.length<" + minL + "||keyUpperCase.length>"
                        + maxL + "){\nreturn null;\n}");
            }
            output(iqueue, 1);
            System.out.println("return null;");
            System.out.println("}");
        }
        System.out.println("}");
        System.out.println("return null;");
    }

    private static void output(LinkedBlockingQueue<String> queue, int index) {
        if (queue.isEmpty()) {
            return;
        }
        if (queue.size() == 1) {
            String key = queue.poll();
            System.out.print("if(");
            if (index == key.length()) {
                System.out.println("keyUpperCase.length==" + key.length());
            } else {
                System.out.println("keyUpperCase.length==" + key.length() + "&&");
                for (int i = index; i < key.length(); i++) {
                    if (i != index) {
                        System.out.print("&&");
                    }
                    System.out.print("keyUpperCase[" + i + "]" + "=='" + key.charAt(i) + "'");
                }
            }
            System.out.println("){");
            System.out.println("return MySQLToken.KW_" + key + ";");
            System.out.println("}");
        } else {
            while (!queue.isEmpty()) {
                String first = queue.poll();
                if (queue.isEmpty()) {
                    LinkedBlockingQueue<String> iqueue = new LinkedBlockingQueue<>();
                    iqueue.add(first);
                    output(iqueue, index);
                    continue;
                }
                String next = queue.peek();
                if (first.length() > index && next.length() > index
                        && first.charAt(index) == next.charAt(index)) {
                    System.out.println(
                            "if(keyUpperCase[" + index + "] =='" + first.charAt(index) + "'){");
                    LinkedBlockingQueue<String> iqueue = new LinkedBlockingQueue<>();
                    iqueue.add(first);
                    int minL = first.length();
                    int maxL = minL;
                    while (!queue.isEmpty()) {
                        if (queue.peek().charAt(index) == first.charAt(index)) {
                            String i = queue.poll();
                            if (minL == 0) {
                                minL = i.length();
                            }
                            if (maxL == 0) {
                                maxL = i.length();
                            }
                            if (i.length() < minL) {
                                minL = i.length();
                            }
                            if (i.length() > maxL) {
                                maxL = i.length();
                            }
                            iqueue.add(i);
                        } else {
                            break;
                        }
                    }
                    if (minL == maxL) {
                        System.out
                                .println("if(keyUpperCase.length!=" + minL + "){\nreturn null;\n}");
                    } else {
                        System.out.println("if(keyUpperCase.length<" + minL
                                + "||keyUpperCase.length>" + maxL + "){\nreturn null;\n}");
                    }
                    output(iqueue, index + 1);
                    //                    System.out.println("return null;");
                    System.out.println("}");
                } else {
                    LinkedBlockingQueue<String> iqueue = new LinkedBlockingQueue<>();
                    iqueue.add(first);
                    output(iqueue, index);
                    continue;
                }
            }
        }
    }
}
