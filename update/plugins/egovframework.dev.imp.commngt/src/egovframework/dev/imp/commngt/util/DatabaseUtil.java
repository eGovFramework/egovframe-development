package egovframework.dev.imp.commngt.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import egovframework.dev.imp.commngt.wizards.model.SqlStatementModel;

/**
 * 데이터 베이스 테이블 조회 유틸 클래스
 * 
 * @author 개발환경 개발팀 박진성
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  박진성          최초 생성
 * 
 * 
 * </pre>
 */

public class DatabaseUtil {
	
	/**
	 * 넘어온 script를 execute하기 위한 준비
	 * @param context
	 * @param ddlFileContent
	 * @param dmlFileContent
	 * @param tableList
	 * @return 결과 값이 담긴 SqlStatementModel list
	 * 
	 */
	public static boolean isExistElement(Connection conn, Statement stmt, SqlStatementModel ssm) {

		try {

			String sql = null;
			
			if(ssm.getStmtType().equalsIgnoreCase("table") || ssm.getStmtType().equalsIgnoreCase("view")) {
				sql = "select * from " + ssm.getStmtName();
			} else if(ssm.getStmtType().equalsIgnoreCase("index")){
				return false;
			} else if(ssm.getStmtType().equalsIgnoreCase("sequence")) {
				sql = "SELECT SEQUENCE_NAME FROM USER_SEQUENCES\n" +
				      "WHERE SEQUENCE_NAME = '" + ssm.getStmtName() + "'";
			} else {
				return false;
			}
			stmt.executeQuery(sql);
			return true;
		} catch (SQLException e) {

			//CommngtLog.logError(e);
			return false;
		} 
		
		
	}
}
