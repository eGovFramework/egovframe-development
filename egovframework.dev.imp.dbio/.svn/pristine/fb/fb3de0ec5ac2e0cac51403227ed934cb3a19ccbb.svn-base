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


import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import egovframework.dev.imp.dbio.DBIOPlugin;
import egovframework.dev.imp.dbio.editor.pages.MapperOutlinePage;
import egovframework.dev.imp.dbio.editor.pages.MapperPage;
import egovframework.dev.imp.dbio.util.EclipseUtil;

/**
 * Mapper Editor
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
public class MapperEditor extends XMLFormEditor implements IMapperEditor {

	private MapperPage mapperPage;
	private MapperOutlinePage fOutlinePage;
	/**
	 * Mapper Page 생성
	 */
	@Override
	protected void addPages() {
		try {
			mapperPage = new MapperPage(this);
			addPage(mapperPage);
			setPageText(0, "Mapper");
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
	 * Mapper Page에 정보 반영
	 */
	private void setModel() {
		mapperPage.setModel(getModel());
	}
	
 	/* (non-Javadoc)
	 * SQL Map Outline 페이지를 호출할 경우 SQL Map 에 대한 outline 페이지를 리턴함. 
 	 * @see org.eclipse.ui.part.MultiPageEditorPart#getAdapter(java.lang.Class)
 	 */
 	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class aClass){

		Object adapter;
			if (aClass.equals(IContentOutlinePage.class)){
				if (fOutlinePage == null){
					fOutlinePage = new MapperOutlinePage(this);
				}
				adapter = fOutlinePage;

			}else{
				adapter = super.getAdapter(aClass);
			}

		return adapter;
	}
 	
	/**
	 * SQL Map Outline 페이지를 refresh 함. 
	 */
	public void refreshOutlinePage(){
		IViewPart view = EclipseUtil.findView("org.eclipse.ui.views.ContentOutline");
		if (fOutlinePage!=null )
			if (view==null )
				fOutlinePage.dispose();
			else 
				fOutlinePage.update();
	}
	
	/* 
	 * pageChange 될 때 source page 에 대한 active 속성을 반영하도록 함. 
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		switch(newPageIndex){
			case 0:
				this.sourceEditor.setActive(false);
				break;
			case 1:
				this.sourceEditor.setActive(true);
				break;
			default:
				break;
		}
		
	}
	
}
