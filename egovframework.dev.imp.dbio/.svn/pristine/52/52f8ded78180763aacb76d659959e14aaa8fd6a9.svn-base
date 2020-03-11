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
package egovframework.dev.imp.dbio.editor;


import org.eclipse.ui.PartInitException;

import egovframework.dev.imp.dbio.DBIOPlugin;
import egovframework.dev.imp.dbio.editor.pages.SqlMapConfigPage;

/**
 * SqlMapConfig Editor
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
public class SqlMapConfigEditor extends XMLFormEditor implements ISqlMapConfigEditor{

	private SqlMapConfigPage sqlMapConfigPage;

	/**
	 * SqlMapConfig Page 생성
	 */
	@Override
	protected void addPages() {
		try {
			sqlMapConfigPage = new SqlMapConfigPage(this);
			addPage(sqlMapConfigPage);
			setPageText(0, "SQL Map Config");
			addSourcePage();

			setModel();
		} catch (PartInitException e) {
			DBIOPlugin.getDefault().getLog().log(e.getStatus());
		}
	}

	/**
	 * 정보 저장
	 */
	@Override
	public void doSaveAs() {
		super.doSaveAs();
		setModel();
	}
	
	/**
	 * 페이지에 정보 반영
	 */
	private void setModel() {
		sqlMapConfigPage.setModel(getModel());
	}
}
