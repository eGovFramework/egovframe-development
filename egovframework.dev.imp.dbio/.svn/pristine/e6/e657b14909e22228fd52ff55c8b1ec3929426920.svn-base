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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import egovframework.dev.imp.dbio.components.TableTextCellEditor;
import egovframework.dev.imp.dbio.components.fields.ComboField;
import egovframework.dev.imp.dbio.components.fields.FieldEvent;
import egovframework.dev.imp.dbio.components.fields.HyperLinkComboButtonField;
import egovframework.dev.imp.dbio.components.fields.IFieldListener;
import egovframework.dev.imp.dbio.components.fields.TextField;
import egovframework.dev.imp.dbio.editor.model.SqlMapResultMapElement;
import egovframework.dev.imp.dbio.util.StringUtil;

/**
 * SqlMap 에디터의 ResultMap 상세부 화면
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
 *   2009.04.22    윤수열      클래스 필드 추출 기능 추가 
 *
 * 
 * </pre>
 */
public class SqlMapResultMapDetailsPart extends AbstractDetailsPage {

	private SqlMapMasterPart masterPart;
	
	private TextField idField;
	private ComboField classField;
	private TableViewer viewer;
	
	private boolean refreshing = false;
	private SqlMapResultMapElement currentElement;
	

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

	private Button addPropertyButton;

	private Button removePropertyButton;
	
	private Button setPropertyButton;
	
	/** 
	 * 생성자
	 * @param masterPart
	 */
	public SqlMapResultMapDetailsPart(SqlMapMasterPart masterPart) {
		super();
		//this.masterPart = masterPart;
		this.setMasterPart(masterPart);
		
		//this.idField = new TextField("ID*:");
		this.setIdField();
		this.setClassField();
	}
	
	private void setClassField() {
		this.classField = new HyperLinkComboButtonField("Class*:", "Browse");
	}
	
	private void setMasterPart(SqlMapMasterPart masterPart) {
		this.masterPart = masterPart;
	}
	
	private void setIdField() {
		this.idField = new TextField("ID*:");
	}

	/**
	 * 화면 구성
	 */
	@Override
	protected void createPartContents(IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		
		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.FILL);
		td.grabHorizontal = true;
		section.setLayoutData(td);
		section.setText("ResultMap");
		
		Composite composite = toolkit.createComposite(section, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		toolkit.paintBordersFor(composite);
		
		idField.create(toolkit, composite, 3);
		idField.addFieldListener(listener);
		classField.create(toolkit, composite, 3);
		classField.addFieldListener(listener);

		createPropertyTable(toolkit, composite);
		
		addPropertyButton = toolkit.createButton(composite, "Add", SWT.NONE);
		removePropertyButton = toolkit.createButton(composite, "Remove", SWT.NONE);
		setPropertyButton =  toolkit.createButton(composite, "Set Property", SWT.NONE);
		
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.BEGINNING;
		addPropertyButton.setLayoutData(gd);
		removePropertyButton.setLayoutData(gd);
		setPropertyButton.setLayoutData(gd);
		addPropertyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewResult();
			}
		});
		removePropertyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeResult();
			}
		});
		setPropertyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setResult();
			}
		});		
		section.setClient(composite);
	}
	
	private void createPropertyTable(FormToolkit toolkit, Composite parent) {
		Table table = toolkit.createTable(parent, SWT.FULL_SELECTION | SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 450;
		gd.horizontalSpan = 2;
		gd.verticalSpan = 3;
		table.setLayoutData(gd);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableLayout tl = new TableLayout();
		table.setLayout(tl);
		
		tl.addColumnData(new ColumnWeightData(0, true));
		TableColumn column = new TableColumn(table, SWT.LEFT, 0);
		column.setText("Property");
		
		tl.addColumnData(new ColumnWeightData(0, true));
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Column");
		
		viewer = new TableViewer(table);
		viewer.setColumnProperties(new String[] {"property", "column"}); //$NON-NLS-1$ //$NON-NLS-2$
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new ResultMapLabelProvider());
		viewer.setCellEditors(new CellEditor[] {
			new TableTextCellEditor(viewer, 0),
			new TableTextCellEditor(viewer, 1)
		});
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (!(element instanceof Element)) return false;
				return "result".equals(((Element) element).getTagName()); //$NON-NLS-1$
			}

			public Object getValue(Object element, String property) {
				if (element instanceof Element) {
					return StringUtil.nvl(((Element) element).getAttribute(property)); //$NON-NLS-1$
				} else {
					return null;
				}
			}

			public void modify(Object element, String property, Object value) {
				if (element instanceof Element) {
					((Element) element).setAttribute(property, (String) value);
					viewer.refresh(element, true);
				}
			}
		});
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				toggleButtons();
			}
		});
	}

	private void toggleButtons() {
		removePropertyButton.setEnabled(!viewer.getSelection().isEmpty());
	}
	/**
	 * 화면 갱신
	 */
	@Override
	public void refresh() {
		refreshing = true;
		try {
			idField.setText(StringUtil.nvl(currentElement.getId()));
			classField.setItems(masterPart.getInitialTypes());
			classField.setText(StringUtil.nvl(currentElement.getClassName()));
			viewer.setInput(createResult(currentElement));
//			Table table = viewer.getTable();
//			Rectangle area = table.getParent().getClientArea();
//			table.setSize(area.width-96, area.height);
			
			toggleButtons();
		} finally {
			refreshing = false;
		}
	}
	
	private List<Element> createResult(SqlMapResultMapElement element) {
		List<Element> ret = new LinkedList<Element>();
		NodeList children = element.getDOMElement().getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element
					&& "result".equals(((Element) child).getTagName())) { //$NON-NLS-1$
				ret.add((Element) child);
			}
		}
		return ret;
	}
	
	private void addNewResult() {
		Document document = currentElement.getDOMElement().getOwnerDocument();
		Element newElement = document.createElement("result"); //$NON-NLS-1$
		currentElement.getDOMElement().appendChild(newElement);
		currentElement.getDOMElement().appendChild(document.createTextNode(System.getProperty("line.separator"))); //$NON-NLS-1$
		viewer.setInput(createResult(currentElement));
		viewer.setSelection(new StructuredSelection(newElement));
	}
	
	private void removeResult() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		Iterator<?> iter = selection.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			element.getParentNode().removeChild(element);
		}
		viewer.setInput(createResult(currentElement));
	}
	
	/**
	 * parameter Class 에 따른 Property 세팅
	 */
	private void setResult() {
		String [] properties = null;
		Element newElement = null;
		if (classField.getText()!=null){
			properties = masterPart.getPage().getSelectedClassProperty(currentElement.getClassName());
		
			Document document = currentElement.getDOMElement().getOwnerDocument();
			if (properties!=null){
				while(document.getElementsByTagName("result")!=null && currentElement.getDOMElement().getChildNodes().getLength()>0){
					currentElement.getDOMElement().removeChild(currentElement.getDOMElement().getChildNodes().item(0));
				}
				viewer.setInput(createResult(currentElement));
				for(int i=0;i<properties.length;i++){
					newElement = document.createElement("result"); //$NON-NLS-1$
					newElement.setAttribute("property", properties[i]);				
					currentElement.getDOMElement().appendChild(document.createTextNode(System.getProperty("line.separator"))); //$NON-NLS-1$
					currentElement.getDOMElement().appendChild(newElement);
				}
			}
		}	
		viewer.setInput(createResult(currentElement));
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
				currentElement.setId(idField.getText());
				masterPart.refreshViewer(currentElement);
			} else if (source == classField) {
				currentElement.setClassName(classField.getText());
			}
		}
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		Object element = selection.getFirstElement();
		if (element instanceof SqlMapResultMapElement) {
			currentElement = (SqlMapResultMapElement) element;
		} else {
			currentElement = null;
		}
		refresh();
	}
	
	private static class ResultMapLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Element
					&&  "result".equals(((Element) element).getTagName())) { //$NON-NLS-1$
				Element domElement = (Element) element;
				switch (columnIndex) {
				case 0:
					return StringUtil.nvl(domElement.getAttribute("property")); //$NON-NLS-1$
				case 1:
					return StringUtil.nvl(domElement.getAttribute("column")); //$NON-NLS-1$
				default :
					return "";
				}
			}
			return null;
		}
		
	}
}
