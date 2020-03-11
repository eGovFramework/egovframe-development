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
package egovframework.dev.imp.dbio.editor.parts;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Element;

import egovframework.dev.imp.dbio.editor.model.SqlMapAliasElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapCRUDElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapCacheModelElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapParameterMapElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapResultMapElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapSelectElement;
import egovframework.dev.imp.dbio.editor.pages.SqlMapPage;

/**
 * SqlMap Master Details Block
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
 *   2009.02.20    김형조      최초 생성
 *
 * 
 * </pre>
 */
@SuppressWarnings("restriction")
public class SqlMapMasterDetailsBlock extends AbstractMasterDetailsBlock {

	private SqlMapMasterPart masterPart;
	
	private IStructuredModel model;
	private boolean active;

	/**
	 * 생성자
	 * 
	 * @param page
	 */
	public SqlMapMasterDetailsBlock(SqlMapPage page) {
		//this.masterPart = new SqlMapMasterPart(page);
		this.setMasterPart(page);
	}
	
	/**
	 * Master Part 초기화
	 * 
	 * @param page
	 */
	private void setMasterPart(SqlMapPage page) {
		this.masterPart = new SqlMapMasterPart(page);
	}

	/**
	 * 화면 구성
	 */
	@Override
	protected void createMasterPart(IManagedForm managedForm, Composite parent) {
		masterPart.createContents(managedForm, parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.forms.DetailsPart)
	 */
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(SqlMapSelectElement.class, new SqlMapSelectDetailsPart(masterPart));
		detailsPart.registerPage(SqlMapCRUDElement.class, new SqlMapQueryDetailsPart(masterPart));
		detailsPart.registerPage(SqlMapAliasElement.class, new SqlMapAliasDetailsPart(masterPart));
		detailsPart.registerPage(SqlMapParameterMapElement.class, new SqlMapParameterMapDetailsPart(masterPart));
		detailsPart.registerPage(SqlMapResultMapElement.class, new SqlMapResultMapDetailsPart(masterPart));
		detailsPart.registerPage(SqlMapCacheModelElement.class,new SqlMapCacheModelDetailsPart(masterPart));
	}

	public void setActive(boolean active) {
		this.active = active;
		refresh();
	}

	public void setModel(IStructuredModel model) {
		this.model = model;
		refresh();		
	}

	/**
	 * 화면 갱신
	 */
	private void refresh() {
		if (active) {
			if (model != null && (model instanceof IDOMModel)) {
				IDOMDocument domDoc = ((IDOMModel) model).getDocument();

				Element element = domDoc.getDocumentElement();
				masterPart.setInput(element);
			} else {
				masterPart.setInput(null);
			}
		}		
	}
	/**
	 * 마스터파트를 리턴함
	 * @return 추가 
	 */
	public SqlMapMasterPart getMasterPart(){
		return masterPart;
	}		

}
