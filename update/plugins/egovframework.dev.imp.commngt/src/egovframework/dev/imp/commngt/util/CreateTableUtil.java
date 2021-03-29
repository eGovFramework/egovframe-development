/**
 * 
 */
package egovframework.dev.imp.commngt.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.datatools.sqltools.core.profile.NoSuchProfileException;

import egovframework.dev.imp.commngt.common.ComMngtMessages;
import egovframework.dev.imp.commngt.common.CommngtLog;
import egovframework.dev.imp.commngt.wizards.model.NewEgovCommngtContext;
import egovframework.dev.imp.commngt.wizards.model.SqlStatementModel;
import egovframework.dev.imp.commngt.wizards.pages.CustomizeTableCreationPage;
import egovframework.dev.imp.core.common.DataToolsPlatformUtil;

/**
 * 테이블 생성전 유틸 클래스
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
public class CreateTableUtil {
	
	/** 생성 결과 판단 */
	public static int result = 1;
	/** 생성할 특정 테이블 정보 */
	public static SqlStatementModel ssm = null;
	/** 테이블 생성전 항목과 정보 */
	public static List<SqlStatementModel> aList = null;
	/** 테이블 생성후 항목과 정보 */
	public static List<SqlStatementModel> madeList = null;
	
	/**
	 * 넘어온 script를 execute하기 위한 준비
	 * @param context
	 * @param ddlFileContent
	 * @param dmlFileContent
	 * @param tableList
	 * @return 결과 값이 담긴 SqlStatementModel list
	 * 
	 */
	public static List<SqlStatementModel> createTableMethod(NewEgovCommngtContext context, String ddlFileContent,  String dmlFileContent, List<SqlStatementModel> tableList) {
		
		Connection conn = null;
		Statement stmt = null;

		FileUtil fu = new FileUtil();
		
		aList= new ArrayList<SqlStatementModel>();
		if(!ddlFileContent.equals("")) { //$NON-NLS-1$
			aList = fu.setSqlStatementFromDDL(ddlFileContent, tableList);
			
			if(dmlFileContent == null) {
				aList = fu.setSqlStatementFromDML("", aList); //$NON-NLS-1$
			}else if(!dmlFileContent.equals("")){ //$NON-NLS-1$
				aList = fu.setSqlStatementFromDML(dmlFileContent, aList);
			}
		}
			
		try {
			conn = DataToolsPlatformUtil.getConnection(DataToolsPlatformUtil.getDatabaseIdentifier(DataToolsPlatformUtil.getConnectionInfo(DataToolsPlatformUtil.getProfileNames()[CustomizeTableCreationPage.selectedDatasourceNum])));
			
			if(conn != null){
				try {
					stmt = conn.createStatement();
				
					if(null != aList) {
						for(int i = 0; i < aList.size(); i++) {
							
							ssm = aList.get(i);
							
							ssm = ExecuteSQLUtil.executeSQL(conn, stmt, ssm);
												
						}
						
					}
				
				} catch (SQLException e) {
					CommngtLog.logError(e);
				}
				
				
				
			}else{
				
				for(int i =0; i< aList.size(); i++){
					
					aList.get(i).setErrorCode("Connection Fail"); //$NON-NLS-1$
					aList.get(i).setResultMessage(ComMngtMessages.createTableUtilresult);
					aList.get(i).setSucceed(false);
					
				}
			
			
			}
			
			if(conn != null) conn.close();
		} catch (SQLException e) {
			CommngtLog.logError(e);
		} catch (NoSuchProfileException e) {
			CommngtLog.logError(e);
		}finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					CommngtLog.logError(e);
				}
			}
		}
		
		try {
			if(stmt != null) stmt.close();
		} catch (SQLException e) {
			CommngtLog.logError(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					CommngtLog.logError(e);
				}
			}
		}
		
		madeList= new ArrayList<SqlStatementModel>();
		madeList.clear();
		for(int i = 0; i< aList.size(); i++){
			madeList.add(aList.get(i)); 
		}
		
		return madeList;
						
	}

}
