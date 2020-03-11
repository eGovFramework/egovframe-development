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
import egovframework.dev.imp.dbio.editor.model.MapperParameterMapElement;
import egovframework.dev.imp.dbio.util.StringUtil;

/**
 * Mapper 에디터의 ParameterMap 상세부 화면
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
public class MapperParameterMapDetailsPart extends AbstractDetailsPage {
	
//	private static final String[] JDBCTYPES = new String[] {
//		"ARRAY", "BIGINT", "BINARY", "BIT", "BLOB", "BOOLEAN", "CHAR", "CLOB",
//		"DATALINK", "DATE", "DECIMAL", "DISTINCT", "DOUBLE", "FLOAT", "INTEGER",
//		"JAVA_OBJECT", "LONGVARBINARY", "LONGVARCHAR", "NULL", "NUMERIC",
//		"OTHER", "REAL", "REF", "SMALLINT", "STRUCT", "TIME", "TIMESTAMP",
//		"TINYINT", "VARBINARY", "VARCHAR"
//	};
	
	private MapperMasterPart masterPart;
	private MapperParameterMapElement currentElement;
	
	private TextField idField;
	private ComboField classField;
	private TableViewer viewer;
	
	private boolean refreshing = false;
	
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

	public MapperParameterMapDetailsPart(MapperMasterPart masterPart) {
		super();
		this.setMasterPart(masterPart);
		
		this.setIdField();
		this.setClassField();
	}
	
	private void setMasterPart(MapperMasterPart masterPart) {
		this.masterPart = masterPart;
	}

	private void setIdField() {
		this.idField = new TextField("ID*:");
	}
	
	private void setClassField() {
		this.classField = new HyperLinkComboButtonField("Class*:", "Browse");
	}
	/**
	 * ParameterMap 상세부 구성
	 */
	@Override
	protected void createPartContents(IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		
		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.FILL);
		td.grabHorizontal = true;
		section.setLayoutData(td);
		section.setText("ParameterMap");
		
		Composite composite = toolkit.createComposite(section);
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
				addNewParameter();
			}
		});
		removePropertyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeParameter();
			}
		});
		setPropertyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setParameter();
			}
		});		
		section.setClient(composite);
	}
	
	private void createPropertyTable(FormToolkit toolkit, Composite parent) {
		Table table = toolkit.createTable(parent, SWT.FULL_SELECTION | SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 450;
		gd.horizontalSpan = 2;
		gd.verticalSpan = 3;
		table.setLayoutData(gd);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableLayout tl = new TableLayout();
		table.setLayout(tl);
		
		tl.addColumnData(new ColumnWeightData(30, true));
		TableColumn column = new TableColumn(table, SWT.LEFT, 0);
		column.setText("Property");
		
//		tl.addColumnData(new ColumnWeightData(30, true));
//		column = new TableColumn(table, SWT.LEFT, 1);
//		column.setText("Type Name");
//		
//		tl.addColumnData(new ColumnWeightData(30, true));
//		column = new TableColumn(table, SWT.LEFT, 2);
//		column.setText("JDBC Type");
		
		viewer = new TableViewer(table);
		viewer.setColumnProperties(new String[] {"property", "typeName", "jdbcType"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new ParameterMapLabelProvider(viewer));
		viewer.setCellEditors(new CellEditor[] {
			new TableTextCellEditor(viewer, 0),
//			new TableTextCellEditor(viewer, 1),
//			new TableComboBoxCellEditor(viewer, 2, JDBCTYPES, SWT.READ_ONLY)
		});
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (!(element instanceof Element)) return false;
				return "parameter".equals(((Element) element).getTagName()); //$NON-NLS-1$
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
			viewer.setInput(createParameter(currentElement));
			toggleButtons();
		} finally {
			refreshing = false;
		}
	}

	private List<Element> createParameter(MapperParameterMapElement element) {
		List<Element> ret = new LinkedList<Element>();
		NodeList children = element.getDOMElement().getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element
					&& "parameter".equals(((Element) child).getTagName())) { //$NON-NLS-1$
				ret.add((Element) child);
			}
		}
		return ret;
	}
	
	/**
	 * 파라메타 맵 추가
	 */
	private void addNewParameter() {
		Document document = currentElement.getDOMElement().getOwnerDocument();
		Element newElement = document.createElement("parameter"); //$NON-NLS-1$
		currentElement.getDOMElement().appendChild(newElement);
		currentElement.getDOMElement().appendChild(document.createTextNode(System.getProperty("line.separator"))); //$NON-NLS-1$
		viewer.setInput(createParameter(currentElement));
		viewer.setSelection(new StructuredSelection(newElement));
	}
	
	/**
	 * 파라메타 맵 삭제
	 */
	private void removeParameter() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		Iterator<?> iter = selection.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			element.getParentNode().removeChild(element);
		}
		viewer.setInput(createParameter(currentElement));
	}
	
	/**
	 * parameter Class 에 따른 Property 세팅
	 */
	private void setParameter() {
		String [] properties = null;
		Element newElement = null;
		if (classField.getText()!=null){
			properties = masterPart.getPage().getSelectedClassProperty(currentElement.getClassName());
		
			Document document = currentElement.getDOMElement().getOwnerDocument();
			if (properties!=null){
				while(document.getElementsByTagName("parameter")!=null && currentElement.getDOMElement().getChildNodes().getLength()>0){
					currentElement.getDOMElement().removeChild(currentElement.getDOMElement().getChildNodes().item(0));
				}
				viewer.setInput(createParameter(currentElement));
				for(int i=0;i<properties.length;i++){
					newElement = document.createElement("parameter"); //$NON-NLS-1$
					newElement.setAttribute("property", properties[i]);				
					currentElement.getDOMElement().appendChild(document.createTextNode(System.getProperty("line.separator"))); //$NON-NLS-1$
					currentElement.getDOMElement().appendChild(newElement);
				}
			}
		}	
		viewer.setInput(createParameter(currentElement));
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
		if (element instanceof MapperParameterMapElement) {
			currentElement = (MapperParameterMapElement) element;
			refresh();
		} else {
			currentElement = null;
		}
	}

	private static class ParameterMapLabelProvider extends LabelProvider implements ITableLabelProvider {
		private TableViewer viewer;

		public ParameterMapLabelProvider(TableViewer viewer) {
			this.setViewer(viewer);
		}
		
		private void setViewer(TableViewer viewer) {
			this.viewer = viewer;
		}
		
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Element
					&& "parameter".equals(((Element) element).getTagName())) { 
				return ((Element) element).getAttribute((String) viewer.getColumnProperties()[columnIndex]);
			}
			return null;
		}
		
	}
}
