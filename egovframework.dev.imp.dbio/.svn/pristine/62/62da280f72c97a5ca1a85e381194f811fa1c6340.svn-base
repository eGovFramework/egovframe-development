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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import egovframework.dev.imp.dbio.components.fields.ComboField;
import egovframework.dev.imp.dbio.components.fields.FieldEvent;
import egovframework.dev.imp.dbio.components.fields.IFieldListener;
import egovframework.dev.imp.dbio.components.fields.TextField;
import egovframework.dev.imp.dbio.editor.model.SqlMapCacheModelElement;
import egovframework.dev.imp.dbio.util.StringUtil;

/**
 * SqlMap 에디터의 CacheModel 상세부 화면
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
public class SqlMapCacheModelDetailsPart extends AbstractDetailsPage {
		
	private static final String [] cacheTypes = new String[] {"MEMORY", "LRU", "FIFO", "OSCACHE"};
	private static final String [] typeMemoryPropertyValues = new String [] {"WEAK", "SOFT", "STRONG"};
	private SqlMapMasterPart masterPart;
	private SqlMapCacheModelElement currentElement;
	
	private FormToolkit toolkit;
	private TextField idField;
	private TextField intervalField;
	private ComboField typeField;
	private TextField nameField;
	private TextField valueFieldText;
	private ComboField valueFieldCombo;
	private Group typePropertyGrp;
	private Composite cpsForCombo, cpsForText;
	
	private boolean refreshing = false;
	
	private final IFieldListener listener = new IFieldListener() {
		public void eventOccured(FieldEvent event) {
			switch (event.getType()) {
			case ButtonSelected:
				//buttonSelected(event.getSource());
				break;
			case HyperLinkActivated:
				//linkActivated(event.getSource());
				break;
			case TextChanged:
				textChanged(event.getSource());
				break;
			default :
				break;
			}
		}
	};

	public SqlMapCacheModelDetailsPart(SqlMapMasterPart masterPart) {
		super();
		this.setMasterPart(masterPart);
		
		this.setIdField();
		this.setIntervalField();
		this.setTypeField();
		this.setNameField();
		this.setValueField();
	}
	
	private void setMasterPart(SqlMapMasterPart masterPart) {
		this.masterPart = masterPart;
	}

	private void setIdField() {
		this.idField = new TextField("ID*:");
	}
	
	private void setIntervalField() {
		this.intervalField = new TextField("Flush Interval:");
	}
	private void setTypeField() {
		this.typeField = new ComboField("Type:");
	}
	
	private void setNameField() {
		this.nameField = new TextField("Name:");
	}
	
	private void setValueField() {
		this.valueFieldText = new TextField("Value:");
		this.valueFieldCombo = new ComboField("Value:");
	}
	
	/**
	 * ParameterMap 상세부 구성
	 */
	@Override
	protected void createPartContents(IManagedForm managedForm, Composite parent) {
		toolkit = managedForm.getToolkit();
		
		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.FILL);
		td.grabHorizontal = true;
		section.setLayoutData(td);
		section.setText("Cache Model");
		
		Composite composite = toolkit.createComposite(section, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		toolkit.paintBordersFor(composite);
		
		idField.create(toolkit, composite, 2);
		idField.addFieldListener(listener);
		
		intervalField.create(toolkit, composite, 2);
		intervalField.addFieldListener(listener);
		typeField.create(toolkit, composite, 2);
		typeField.setItems(cacheTypes);
		typeField.addFieldListener(listener);
		
		typePropertyGrp = new Group(composite,SWT.NULL);
		typePropertyGrp.setText("Type Property");
		typePropertyGrp.setLayout(new GridLayout(4, false));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		typePropertyGrp.setLayoutData(gridData);
		toolkit.paintBordersFor(typePropertyGrp);

		nameField.create(toolkit, typePropertyGrp, 2);
		nameField.addFieldListener(listener);
		nameField.setEditable(false);
		//nameField.setEnable(false);
		
		Composite propertyValueCps = toolkit.createComposite(typePropertyGrp);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		propertyValueCps.setLayoutData(gridData);
		
		FormLayout formLayout = new FormLayout();
		formLayout.marginBottom = 0;
		formLayout.marginHeight = 0;
		formLayout.marginTop = 0;
		propertyValueCps.setLayout(formLayout);
		
		GridLayout gl = new GridLayout(2,false);
		gl.marginTop = 1;
		gl.marginHeight = 0;
		gl.marginBottom = 1;
		
		cpsForText = toolkit.createComposite(propertyValueCps);
		cpsForText.setLayout(gl);
		FormData fd = new FormData();
		fd.left = new FormAttachment(0,0);
		fd.right = new FormAttachment(100,0);
		fd.top = new FormAttachment(0,3);
		fd.bottom = new FormAttachment(100,0);
		cpsForText.setLayoutData(fd);
		toolkit.paintBordersFor(cpsForText);
		
		cpsForCombo = toolkit.createComposite(propertyValueCps);
		cpsForCombo.setLayout(gl);
		fd = new FormData();
		fd.left = new FormAttachment(0,0);
		fd.right = new FormAttachment(100,0);
		fd.top = new FormAttachment(0,4);
		fd.bottom = new FormAttachment(100,0);
		cpsForCombo.setLayoutData(fd);
		toolkit.paintBordersFor(cpsForCombo);		
		
		valueFieldText.create(toolkit, cpsForText, 2);
		valueFieldText.addFieldListener(listener);
		
		valueFieldCombo.create(toolkit, cpsForCombo, 2);
		valueFieldCombo.addFieldListener(listener);

		section.setClient(composite);
	}
	
	/**
	 * 화면 갱신
	 */
	@Override
	public void refresh() {
		refreshing = true;
		try {
			idField.setText(StringUtil.nvl(currentElement.getId()));
			intervalField.setText(StringUtil.nvl(currentElement.getInterval()));
			typeField.setText(StringUtil.nvl(currentElement.getType()));
			
			if(currentElement.isPropertyNode()) {
				typePropertyGrp.setVisible(true);
				nameField.setText(StringUtil.nvl(currentElement.getPropertyName()));
				
				if ("MEMORY".equals(StringUtil.nvl(currentElement.getType()))) {
					valueFieldCombo.setItems(typeMemoryPropertyValues);
					valueFieldCombo.setText(StringUtil.nvl(currentElement.getPropertyValue()));
					cpsForCombo.setVisible(true);
					cpsForText.setVisible(false);
				} else {
					valueFieldText.setText(StringUtil.nvl(currentElement.getPropertyValue()));
					cpsForCombo.setVisible(false);
					cpsForText.setVisible(true);
				}
			} else {
				typePropertyGrp.setVisible(false);
			}
		} finally {
			refreshing = false;
		}
	}

	/**
	 * tree생성
	 * @param element
	 * @return
	 */
//	private List<Element> createCacheModel(SqlMapCacheModelElement element) {
//		List<Element> ret = new LinkedList<Element>();
//		NodeList children = element.getDOMElement().getChildNodes();
//		for (int i = 0; i < children.getLength(); i++) {
//			Node child = children.item(i);
//			if (child instanceof Element
//					&& "cacheModel".equals(((Element) child).getTagName())) { //$NON-NLS-1$
//				ret.add((Element) child);
//			}
//		}
//		return ret;
//	}

	private void textChanged(Object source) {
		if (!refreshing) {
			if (source == idField) {
				currentElement.setId(idField.getText());
				masterPart.refreshViewer(currentElement);
			} else if (source == intervalField) {
				currentElement.setInterval(intervalField.getText());
			} else if (source == typeField) {
				currentElement.setType(typeField.getText());
				currentElement.setProperty(nameField.getText()
						, "MEMORY".equals(StringUtil.nvl(typeField.getText())) 
						? StringUtil.nvl(valueFieldCombo.getText()): StringUtil.nvl(valueFieldText.getText()));

				if ("OSCACHE".equals(typeField.getText())) {
					typePropertyGrp.setVisible(false);
					currentElement.removeProperty();
				} else {
					typePropertyGrp.setVisible(true);
					
					if ("MEMORY".equals(typeField.getText())) {
						nameField.setText("reference-type");
						valueFieldCombo.setItems(typeMemoryPropertyValues);
						valueFieldCombo.setText("WEAK");
						currentElement.setProperty("reference-type", "WEAK");

						cpsForCombo.setVisible(true);
						cpsForText.setVisible(false);
					} else {
						nameField.setText("size");
						valueFieldText.setText("1000");
						currentElement.setProperty("size", "1000");
					
						cpsForCombo.setVisible(false);
						cpsForText.setVisible(true);
					}
				}
			} else if (source == valueFieldCombo) {
				currentElement.setProperty(nameField.getText(), valueFieldCombo.getText());
			} else if (source == valueFieldText) {
				currentElement.setProperty(nameField.getText(), valueFieldText.getText());
			}
		}
	}
	
	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		Object element = selection.getFirstElement();
		if (element instanceof SqlMapCacheModelElement) {
			currentElement = (SqlMapCacheModelElement) element;
			refresh();
		} else {
			currentElement = null;
		}
	}
}
