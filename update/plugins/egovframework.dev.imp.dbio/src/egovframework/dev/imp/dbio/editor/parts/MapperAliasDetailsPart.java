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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import egovframework.dev.imp.dbio.components.fields.ComboField;
import egovframework.dev.imp.dbio.components.fields.FieldEvent;
import egovframework.dev.imp.dbio.components.fields.HyperLinkComboButtonField;
import egovframework.dev.imp.dbio.components.fields.IFieldListener;
import egovframework.dev.imp.dbio.components.fields.TextField;
import egovframework.dev.imp.dbio.editor.model.MapperAliasElement;
import egovframework.dev.imp.dbio.util.StringUtil;

/**
 * Alias 상세화면
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
public class MapperAliasDetailsPart extends AbstractDetailsPage {
	private MapperMasterPart masterPart;
	
	private TextField idField;
	private ComboField classField;
	
	private boolean refreshing = false;
	private MapperAliasElement currentElement;

	public MapperAliasDetailsPart(MapperMasterPart masterPart) {
		super();
		this.setMasterPart(masterPart);
		this.setMasterPart(masterPart);
		
		//this.idField = new TextField("ID*:");
		this.setIdField();
		this.setClassField();
	}
	
	private void setMasterPart(MapperMasterPart masterPart) {
		this.masterPart = masterPart;
	}
	
	private void setIdField() {
		this.idField = new TextField("Alias*:");
	}

	private void setClassField() {
		this.classField = new HyperLinkComboButtonField("Class*:", "Browse");
	}	
	
	private final IFieldListener listener = new IFieldListener() {
		public void eventOccured(FieldEvent event) {
			switch (event.getType()) {
			case ButtonSelected:
				buttonSelected(event.getSource());
				break;
			case HyperLinkActivated:
				linkActivated(event.getSource());
				break;
			case TextChanged:
				textChanged(event.getSource());
				break;
			default :
				break;
			}
		}
	};

	@Override
	protected void createPartContents(IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();

		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.FILL);
		td.grabHorizontal = true;
		section.setLayoutData(td);
		section.setText("Alias");

		Composite composite = toolkit.createComposite(section, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		toolkit.paintBordersFor(composite);

		idField.create(toolkit, composite, 3);
		idField.addFieldListener(listener);
		classField.create(toolkit, composite, 3);
		classField.addFieldListener(listener);
		
		section.setClient(composite);
	}

	/**
	 * 화면 갱신
	 */
	@Override
	public void refresh() {
		refreshing = true;
		try {
			idField.setText(StringUtil.nvl(currentElement.getAlias()));
			classField.setItems(masterPart.getInitialTypes());
			classField.setText(StringUtil.nvl(currentElement.getClassName()));
		} finally {
			refreshing = false;
		}
	}
	
	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		Object element = selection.getFirstElement();
		if (element instanceof MapperAliasElement) {
			currentElement = (MapperAliasElement) element;
			refresh();
		} else {
			currentElement = null;
		}

	}

	private void buttonSelected(Object source) {
		if (source == classField) {
			String newClass = masterPart.getPage().selectClass();
			if (newClass != null && !newClass.equals(classField.getText())) {
				classField.setText(newClass);
				currentElement.setClassName(newClass);
			}
		}
	}
	
	private void linkActivated(Object source) {
		if (source == classField) {
			String newClass = masterPart.getPage().openOrCreateNewJavaClass(classField.getText());
			if (newClass != null && !newClass.equals(classField.getText())) {
				classField.setText(newClass);
				currentElement.setClassName(newClass);
			}
		}
	}
	
	private void textChanged(Object source) {
		if (!refreshing) {
			if (source == idField) {
				currentElement.setAlias(idField.getText());
				masterPart.refreshViewer(currentElement);
			} else if (source == classField) {
				currentElement.setClassName(classField.getText());
			}
		}
	}

}
