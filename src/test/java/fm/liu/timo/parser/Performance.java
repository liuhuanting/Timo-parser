package fm.liu.timo.parser;

public interface Performance {
	String SQL_BENCHMARK_SELECT = " seLEcT id, member_id , image_path  \t , image_size , STATUS,   gmt_modified from    wp_image wheRe \t\t\n id =  ? AND member_id\t=\t-123.456";
	// String SQL_BENCHMARK_SELECT =
	// "select ID, GMT_CREATE, GMT_MODIFIED, INBOX_FOLDER_ID, MESSAGE_ID,             FEEDBACK_TYPE, TARGET_ID,               TRADE_ID, SUBJECT, SENDER_ID, SENDER_TYPE,              S_DISPLAY_NAME, SENDER_STATUS, RECEIVER_ID, RECEIVER_TYPE,              R_DISPLAY_NAME, RECEIVER_STATUS, SPAM_STATUS, REPLY_STATUS,             ATTACHMENT_STATUS,              SENDER_COUNTRY,                 RECEIVER_COUNTRY,APP_FROM,APP_TO,APP_SOURCE,SENDER_VACOUNT,RECEIVER_VACOUNT,            DISTRIBUTE_STATUS,ORG_RECEIVER_ID,CUSTOMER_ID,OPERATOR_ID,OPERATOR_NAME,FOLLOW_STATUS,DELETE_STATUS,FOLLOW_TIME,BATCH_COUNT             from MESSAGE_REC_RECORD                 where RECEIVER_VACOUNT          =? and ID = ?";
	String SQL_BENCHMARK_EXPR_SELECT = "( seLect id, member_id , image_path  \t , image_size , STATUS,   gmt_modified from    wp_image where \t\t\n id =  ? and member_id\t=\t?)";
}