package egovframework.mdev.imp.commngt.wizards.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 데이터 베이스 클래스
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

public class SqlStatementModel {
	/** 테이블 명 */
	private String stmtName = null;
	/** 테이블 종류 */
	private String stmtType = null;
	/** DDL 문 */
	private String createStatement = null;
	/** DML 문 */
	private List <String> insertStatements = null;
	/** 테이블이 속한 컴포넌트 명*/
	private String component = null;
	/** 생성여부 */
	private boolean isSucceed = false;
	/** viewer 메세지*/
	private String resultMessage = null;
	/** 실패시 에러 메세지 */
	private String errorCode = null;


	/**
	 * @return the resultMessage
	 */
	public String getResultMessage() {
		return resultMessage;
	}

	/**
	 * @param resultMessage the resultMessage to set
	 */
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @param insertStatements the insertStatements to set
	 */
	public void setInsertStatements(List<String> insertStatements) {
		this.insertStatements = insertStatements;
	}
	/**
	 * model
	 * @param insertStatements the insertStatements to set
	 */
	public SqlStatementModel() {
		this.insertStatements = new ArrayList<String>();
	}
	
	/**
	 * @return the stmtName
	 */
	public String getStmtName() {
		return stmtName;
	}
	/**
	 * setter
	 * @param stmtName the stmtName to set
	 */
	public void setStmtName(String stmtName) {
		this.stmtName = stmtName;
	}
	/**
	 * @return the stmtType
	 */
	public String getStmtType() {
		return stmtType;
	}
	/**
	 * setter
	 * @param stmtType the stmtType to set
	 */
	public void setStmtType(String stmtType) {
		this.stmtType = stmtType;
	}
	/**
	 * @return the createStatement
	 */
	public String getCreateStatement() {
		return createStatement;
	}
	/**
	 * setter
	 * @param statement the statement to set
	 */
	public void setCreateStatement(String statement) {
		this.createStatement = statement;
	}
	/**
	 * @return the isSucceed
	 */
	public boolean isSucceed() {
		return isSucceed;
	}
	/**
	 * setter
	 * @param isSucceed the isSucceed to set
	 */
	public void setSucceed(boolean isSucceed) {
		this.isSucceed = isSucceed;
	}
	/**
	 * @return the component
	 */
	public String getComponent() {
		return component;
	}
	/**
	 * setter
	 * @param component the component to set
	 */
	public void setComponent(String component) {
		this.component = component;
	}
	/**
	 * setter
	 * @param statement the statement to set
	 */
	public void addInsertStatement(String statement) {
		this.insertStatements.add(statement);
	}
	/**
	 * @return the insertStatements
	 */
	public List <String> getInsertStatements() {
		return insertStatements;
	}
}
