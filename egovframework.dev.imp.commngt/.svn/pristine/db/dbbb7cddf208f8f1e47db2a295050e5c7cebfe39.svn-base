package egovframework.dev.imp.commngt.util;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import egovframework.dev.imp.commngt.common.ComMngtMessages;
import egovframework.dev.imp.commngt.wizards.model.SqlStatementModel;

/**
 * 테이블 생성 유틸 클래스
 * 
 * @author 개발환경 개발팀 최서윤
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  최서윤          최초 생성
 * 
 * 
 * </pre>
 */

public class ExecuteSQLUtil {
	
	/** 테이블 생성 실패시 메시지 */
	private static String errorMessage = null;
	
	/**
	 * 넘어온 script를 execute
	 * @param conn
	 * @param stmt
	 * @param ssm
	 * @return 결과 값이 담긴 SqlStatementModel
	 * 
	 */
	public static SqlStatementModel executeSQL(Connection conn, Statement stmt, SqlStatementModel ssm){
		int result = 1;
		
			if(ssm.getCreateStatement() != null){
			
				try {
					if(!DatabaseUtil.isExistElement(conn, stmt, ssm)) {
						
							result = stmt.executeUpdate(ssm.getCreateStatement());
							result = 0;
						
						
						List <String> insertStatements = ssm.getInsertStatements();
						if(insertStatements.size() > 0) {
							
							for (int j = 0; j < insertStatements.size(); j++) {
								result = stmt.executeUpdate(insertStatements.get(j));
								result = 0;
								
							}
						}
					}
					
					if(result  == 0){
						ssm.setSucceed(true);
						ssm.setResultMessage(ComMngtMessages.executeSQLUtilresult1);
						
					}else{
						ssm.setSucceed(true);
						ssm.setResultMessage(ComMngtMessages.executeSQLUtilresult2);
					}
					
				} catch (Exception e) {
					//CommngtLog.logError(e);
					errorMessage = e.getMessage();
					
					ssm.setErrorCode(errorMessage);
					ssm.setResultMessage(ComMngtMessages.executeSQLUtilresult3);
				}
			
			}
		return ssm;
		
	}


}
