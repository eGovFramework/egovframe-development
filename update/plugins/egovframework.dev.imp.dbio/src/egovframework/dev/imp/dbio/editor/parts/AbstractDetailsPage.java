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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * 상세페이지 추상 객체
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
public abstract class AbstractDetailsPage implements IDetailsPage {

	private IManagedForm form;
	private IStructuredSelection selection;

	protected IManagedForm getManagedForm() {
		return form;
	}
	
	protected IStructuredSelection getSelection() {
		return selection;
	}
	
	public void createContents(Composite parent) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.topMargin = 5;
		layout.leftMargin = 5;
		layout.rightMargin = 5;
		layout.bottomMargin = 2;
		parent.setLayout(layout);
		createPartContents(form, parent);
		
		form.getToolkit().paintBordersFor(parent);
	}
	
	protected abstract void createPartContents(IManagedForm managedForm, Composite parent);

	public void commit(boolean onSave) {
		// DbioLog.logInfo("sham");
	}

	public void dispose() {
		// DbioLog.logInfo("sham");
	}

	public void initialize(IManagedForm form) {
		this.form = form;
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		// DbioLog.logInfo("sham");
	}

	public void setFocus() {
		// DbioLog.logInfo("sham");
	}

	public boolean setFormInput(Object input) {
		return false;
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		ISelection tmpSelection = selection;
		
		if (!(tmpSelection instanceof IStructuredSelection)) {
			tmpSelection = StructuredSelection.EMPTY;
		}
		this.selection = (IStructuredSelection) tmpSelection;
		selectionChanged((IStructuredSelection) tmpSelection);
	}
	
	protected abstract void selectionChanged(IStructuredSelection selection);

}
