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
package egovframework.dev.imp.dbio.editor.pages;

import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

import egovframework.dev.imp.dbio.editor.SqlMapEditor;
import egovframework.dev.imp.dbio.util.EclipseUtil;

/**
 * StructuredTextEditor Adapter
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
public class StructuredTextEditorAdapter extends StructuredTextEditor implements ITextEditor{//, IFormPage {
	
	private Control fControl;
	
	private String id;
	private FormEditor editor;
	private boolean active;
	private int index;

	public StructuredTextEditorAdapter(FormEditor editor){
		this.editor = editor;
	}
	
	public boolean canLeaveThePage() {
		return true;
	}

	public FormEditor getEditor() {
		return editor;
	}

	public String getId() {
		return id;
	}

	public int getIndex() {
		return index;
	}

	public IManagedForm getManagedForm() {
		return null;
	}

	/* (non-Javadoc)
	 * outline page 가 없을 경우 outline page를 오픈함. 
	 * @see org.eclipse.wst.sse.ui.StructuredTextEditor#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		Control[] children = parent.getChildren();
		fControl = children[children.length - 1];
	}
	
	public Control getPartControl() {
		return fControl;
	}

	public void initialize(FormEditor editor) {
		this.editor = editor;
		setEditorPart(editor);
	}

	public boolean isActive() {
		return active;
	}

	public boolean isEditor() {
		return false;
	}

	public boolean selectReveal(Object object) {
		return false;
	}

	public void setActive(boolean active) {
		this.active = active;
		if (active)
			viewOutline();
	}
	/**
	 * 소스 페이지를 열면 자동으로 outline view 가 보여지도록 함. 
	 */
	private void viewOutline(){
		try {

			IViewPart view = EclipseUtil.findView("org.eclipse.ui.views.ContentOutline");		
			if (view == null){
				view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.ContentOutline");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public void setIndex(int index) {
		this.index = index;
	}
 	/* (non-Javadoc)
 	 * SQL Map 소스 수정시 outline 에 반영하기 위함 
 	 * @see org.eclipse.wst.sse.ui.StructuredTextEditor#getAdapter(java.lang.Class)
 	 */
 	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class aClass){

		Object adapter;
		adapter = super.getAdapter(aClass);

		if (this.active && aClass.equals(IFindReplaceTarget.class)){
			if (getEditor() instanceof SqlMapEditor)
				((SqlMapEditor) getEditor()).refreshOutlinePage();
			}

		return adapter;
	}	
  }
