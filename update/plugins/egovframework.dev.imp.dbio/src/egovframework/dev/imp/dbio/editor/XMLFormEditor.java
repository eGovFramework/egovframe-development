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


import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

import egovframework.dev.imp.dbio.editor.pages.StructuredTextEditorAdapter;
import egovframework.dev.imp.dbio.util.StructuredModelUtil;

/**
 * XMLFormEditor
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
@SuppressWarnings("restriction")
public class XMLFormEditor extends FormEditor {

	private final IPropertyListener listener = new IPropertyListener() {
		public void propertyChanged(Object source, int propId) {
			firePropertyChange(propId);
		}
	};
	protected StructuredTextEditorAdapter sourceEditor;
	private int sourcePageIndex;

	private IStructuredModel model;
	
	/**
	 * 화면 구성
	 */
	@Override
	protected void createPages() {
		super.createPages();
		updateTitle();
	}
	
	/**
	 * 소스 페이지 추가
	 * @return 페이지의 인덱스
	 * @throws PartInitException
	 */
	protected int addSourcePage() throws PartInitException {
		sourceEditor = new StructuredTextEditorAdapter(this);
		sourcePageIndex = addPage(sourceEditor, getEditorInput());
		setSourcePageName();
		sourceEditor.addPropertyListener(listener);
		return sourcePageIndex;
	}
	
	/**
	 * 소스페이지명 설정
	 */
	private void setSourcePageName() {
		IEditorInput input = getEditorInput();
		String name = "Source";
		if (input != null) {
			name = input.getName();
		}
		setPageText(sourcePageIndex, name);
	}

	/**
	 * 페이지 닫기
	 */
	@Override
	public void dispose() {
		if (model != null) {
			model.releaseFromRead();
			model = null;
		}
		sourceEditor.removePropertyListener(listener);
		super.dispose();
	}
	
	/**
	 * 내용 저장
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		sourceEditor.doSave(monitor);
	}
	
	/**
	 * 다른 이름으로 저장
	 */
	@Override
	public void doSaveAs() {
		sourceEditor.doSaveAs();
		setInput(sourceEditor.getEditorInput());
		if (model != null) {
			model.releaseFromRead();
			model = null;
		}
		setSourcePageName();
		updateTitle();
	}

	/**
	 * 다른 이름저장 허용여부
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return (sourceEditor != null) && sourceEditor.isSaveAsAllowed();
	}
	
	/**
	 * 제목 갱신
	 */
	protected void updateTitle() {
		IEditorInput input = getEditorInput();
		setPartName(input.getName());
		setTitleToolTip(input.getToolTipText());
	}
	
	/**
	 * Document 객체 반환
	 * 
	 * @return Document 객체
	 */
	protected IDocument getDocument() {
		IDocument document = null;
		if (sourceEditor != null) {
			document = sourceEditor.getDocumentProvider().getDocument(sourceEditor.getEditorInput());
		}
		return document;
	}
	
	/**
	 * 모델 반환
	 * @return StructuredModel 객체
	 */
	public IStructuredModel getModel() {
		if (model == null) {
			model = StructuredModelUtil.getModelForRead(sourceEditor.getDocumentProvider(), getEditorInput());
		}
		return model;
	}
	
	/**
	 * marker 이동
	 * @param marker
	 */
	void gotoMarker(IMarker marker) {
		setActivePage(sourcePageIndex);
		IDE.gotoMarker(sourceEditor, marker);
	}
	
	/**
	 * 정보 설정
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		updateTitle();
	}

	/**
	 * 입력 설정
	 */
	@Override
	protected void setInput(IEditorInput input) {
		setInputWithNotify(input);
	}

	/**
	 * 수정여부 확인
	 * @return
	 */
	public boolean isEditable() {
		return sourceEditor.isEditable();
	}
	
	protected void addPages(){
		// DbioLog.logInfo("sham");
	}
}
