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
import egovframework.dev.imp.dbio.editor.pages.MapperConfigurationPage;

/**
 * MapperConfiguration Editor
 * @author 개발환경 개발팀 김효수
 * @since 2019.02.25
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2019.02.25  김효수          MyBatis DBIO  
 *
 * 
 * </pre>
 */
public class MapperConfigurationEditor extends XMLFormEditor implements IMapperConfigurationEditor{

	private MapperConfigurationPage mapperConfigurationPage;

	/**
	 * MapperConfiguration Page 생성
	 */
	@Override
	protected void addPages() {
		try {
			mapperConfigurationPage = new MapperConfigurationPage(this);
			addPage(mapperConfigurationPage);
			setPageText(0, "Mapper Configuration");
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
		mapperConfigurationPage.setModel(getModel());
	}
}
