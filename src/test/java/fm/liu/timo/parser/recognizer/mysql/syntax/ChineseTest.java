package fm.liu.timo.parser.recognizer.mysql.syntax;

import java.io.UnsupportedEncodingException;
import java.sql.SQLSyntaxErrorException;

import org.junit.Assert;

import fm.liu.timo.parser.ast.stmt.dml.DMLSelectStatement;
import fm.liu.timo.parser.recognizer.mysql.lexer.MySQLLexer;

/**
 * 
 * @author liuhuanting
 * @date 2018年9月6日 下午5:03:22
 * 
 */
public class ChineseTest extends AbstractSyntaxTest {
    public void testChinese() throws SQLSyntaxErrorException, UnsupportedEncodingException {
        byte[] data = {(byte) 0x73, (byte) 0x65, (byte) 0x6c, (byte) 0x65, (byte) 0x63, (byte) 0x74,
                (byte) 0x20, (byte) 0x27, (byte) 0xe4, (byte) 0xbd, (byte) 0xa0, (byte) 0xe5,
                (byte) 0xa5, (byte) 0xbd, (byte) 0xe5, (byte) 0x95, (byte) 0x8a, (byte) 0xe4,
                (byte) 0xb8, (byte) 0xad, (byte) 0xe5, (byte) 0x9b, (byte) 0xbd, (byte) 0xef,
                (byte) 0xbc, (byte) 0x81, (byte) 0x27};
        String sql = new String(data).toUpperCase();
        data = sql.getBytes("GBK");
        MySQLLexer lexer = new MySQLLexer(data);
        MySQLDMLSelectParser parser = new MySQLDMLSelectParser(lexer, new MySQLExprParser(lexer));
        DMLSelectStatement select = parser.select();
        Assert.assertNotNull(select);
        String output = output2MySQL(select, sql);
        Assert.assertEquals(sql, output);
    }

}
