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

import egovframework.dev.imp.dbio.editor.model.MapperAliasElement;
import egovframework.dev.imp.dbio.editor.model.MapperCRUDElement;
import egovframework.dev.imp.dbio.editor.model.MapperCacheModelElement;
import egovframework.dev.imp.dbio.editor.model.MapperParameterMapElement;
import egovframework.dev.imp.dbio.editor.model.MapperResultMapElement;
import egovframework.dev.imp.dbio.editor.model.MapperSelectElement;
import egovframework.dev.imp.dbio.editor.pages.MapperPage;

/**
 * Mapper Master Details Block
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
@SuppressWarnings("restriction")
public class MapperMasterDetailsBlock extends AbstractMasterDetailsBlock {

	private MapperMasterPart masterPart;
	
	private IStructuredModel model;
	private boolean active;

	/**
	 * 생성자
	 * 
	 * @param page
	 */
	public MapperMasterDetailsBlock(MapperPage page) {
		//this.masterPart = new MapperMasterPart(page);
		this.setMasterPart(page);
	}
	
	/**
	 * Master Part 초기화
	 * 
	 * @param page
	 */
	private void setMasterPart(MapperPage page) {
		this.masterPart = new MapperMasterPart(page);
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
		detailsPart.registerPage(MapperSelectElement.class, new MapperSelectDetailsPart(masterPart));
		detailsPart.registerPage(MapperCRUDElement.class, new MapperQueryDetailsPart(masterPart));
		detailsPart.registerPage(MapperAliasElement.class, new MapperAliasDetailsPart(masterPart));
		detailsPart.registerPage(MapperParameterMapElement.class, new MapperParameterMapDetailsPart(masterPart));
		detailsPart.registerPage(MapperResultMapElement.class, new MapperResultMapDetailsPart(masterPart));
		detailsPart.registerPage(MapperCacheModelElement.class,new MapperCacheModelDetailsPart(masterPart));
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
	public MapperMasterPart getMasterPart(){
		return masterPart;
	}		

}
