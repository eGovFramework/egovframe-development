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

import java.util.HashMap;

import org.eclipse.datatools.sqltools.core.services.UIComponentService;
import org.eclipse.datatools.sqltools.editor.core.connection.ISQLEditorConnectionInfo;
import org.eclipse.datatools.sqltools.editor.ui.core.SQLDevToolsUIConfiguration;
import org.eclipse.datatools.sqltools.editor.ui.core.SQLToolsUIFacade;
import org.eclipse.datatools.sqltools.sql.ui.dialogs.SQLPainterDlg;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * OpenSQLBuilderDialog Action
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
public class OpenSQLBuilderDialogAction extends Action {

	private ISQLEditorConnectionInfo connectionInfo;
	private IWorkbenchPartSite site;
	private String sqlStatement;
	private String generatedSQLStatement;

	/**
	 * 생성자 
	 * 
	 * @param site
	 * @param connectionInfo
	 */
	public OpenSQLBuilderDialogAction(IWorkbenchPartSite site,
			ISQLEditorConnectionInfo connectionInfo) {
		this.site = site;
		this.connectionInfo = connectionInfo;
	}

	/**
	 * 생성자
	 * 
	 * @param site
	 * @param connectionInfo
	 * @param sqlStatement
	 */
	public OpenSQLBuilderDialogAction(IWorkbenchPartSite site,
			ISQLEditorConnectionInfo connectionInfo, String sqlStatement) {
		this(site, connectionInfo);
		this.sqlStatement = sqlStatement;
	}
	
	/**
	 * 다이얼로그 실행
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void run() {
		
		SQLDevToolsUIConfiguration conf = SQLToolsUIFacade.getConfigurationByProfileName(connectionInfo.getConnectionProfileName());
    	UIComponentService componentService = conf.getUIComponentService();
		if (componentService.supportsDMLDialog())
    	{
			HashMap<?, ?> map = new HashMap();
//			if (getFile() != null){
//				map.put(UIComponentService.KEY_FILE, getFile());
//			}
			SQLPainterDlg dlg = componentService.getDMLDialog(
					site.getShell(),
					null, 
					sqlStatement,
					connectionInfo.getConnectionProfileName(),
					null, null, null, null, map);
    	
			generatedSQLStatement = dlg.load();
    	}
	}
	
	/**
	 * 작성된 SQL문장 반환
	 * @return SQL문
	 */
	public String getGeneratedSQLStatement() {
		return generatedSQLStatement;
	}
}
