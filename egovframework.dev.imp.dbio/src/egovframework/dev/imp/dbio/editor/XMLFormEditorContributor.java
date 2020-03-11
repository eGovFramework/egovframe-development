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

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

/**
 * XMLFormEditorContributor
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
public class XMLFormEditorContributor extends MultiPageEditorActionBarContributor {

	private StatusLineContributionItem readOnlyStatus;
	
	/**
	 * 생성자
	 */
	public XMLFormEditorContributor() {
		super();
		this.setReadOnlyStatus();
	}
	
	private void setReadOnlyStatus() {
		readOnlyStatus = new StatusLineContributionItem("Read-Only");
	}
	
	/**
	 * StatusLine 에 읽기전용상태 등록
	 */
	@Override
	public void contributeToStatusLine(IStatusLineManager statusLineManager) {
		statusLineManager.add(readOnlyStatus);
	}
	
	/**
	 * 활성화 에디터 설정
	 */
	public void setActiveEditor(IEditorPart targetEditor) {
		super.setActiveEditor(targetEditor);
		if (!(targetEditor instanceof XMLFormEditor)) return;
		
		XMLFormEditor editor = (XMLFormEditor) targetEditor;
		setReadOnlyStatus(!editor.isEditable());
	}
	
	@Override
	public void setActivePage(IEditorPart activeEditor) {
		// DbioLog.logInfo("sham");
	}
	

	/**
	 * 읽기전용설정
	 * @param readOnly
	 */
	private void setReadOnlyStatus(boolean readOnly) {
		if ( readOnly ) {
			readOnlyStatus.setText("ReadOnly");
		} else {
			readOnlyStatus.setText("Writable");
		}
	}


}
