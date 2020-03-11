/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.dev.imp.dbio.editor.actions;


import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.datatools.sqltools.core.DatabaseIdentifier;
import org.eclipse.datatools.sqltools.editor.core.connection.ISQLEditorConnectionInfo;
import org.eclipse.datatools.sqltools.sqleditor.result.GroupSQLResultRunnable;
import org.eclipse.jface.action.Action;

import egovframework.dev.imp.dbio.util.DTPUtil;

/**
 * ExecuteSQL Action
 * @author 개발환경 개발팀 김형조
 * @since 2009.02.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20  김형조          최초 생성
 *
 * 
 * </pre>
 */
public class ExecuteSQLAction extends Action {

	private String sql;
	private ISQLEditorConnectionInfo connectionInfo;

	/**
	 * 생성자
	 */
	public ExecuteSQLAction() {
		super();
	}
	
	/**
	 * SQL 구문 설정
	 * @param sql
	 */
	public void setSQLStatements(String sql) {
		this.sql = sql;
	}
	
	/**
	 * connectionInfo 값 설정
	 * @param connectionInfo
	 */
	public void setConnectionInfo(ISQLEditorConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}
	
	/**
	 * SQL 실행
	 */
	@Override
	public void run() {
        String sql = getSQLStatements();
        DatabaseIdentifier databaseIdentifier = DTPUtil.getDatabaseIdentifier(connectionInfo); 
        if (databaseIdentifier == null || sql == null) return;


        Job job = new GroupSQLResultRunnable(null, new String[] {sql},
        		null, null, databaseIdentifier, false, null); 
        job.setUser(true);
        job.schedule();
	}

	/**
	 * SQL구문 반환
	 * @return
	 */
	private String getSQLStatements() {
		return sql;
	}


}
